package se.group3.gameoflife.frontend.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import se.group3.gameoflife.frontend.dto.cards.CareerCardDTO;
import se.group3.gameoflife.frontend.dto.cards.HouseCardDTO;
import se.group3.gameoflife.frontend.fragments.choice_fragments.ActionCardFragment;
import se.group3.gameoflife.frontend.fragments.choice_fragments.CareerChoiceFragment;
import se.group3.gameoflife.frontend.fragments.choice_fragments.HouseChoiceFragment;
import se.group3.gameoflife.frontend.fragments.choice_fragments.StopCellFragment;
import se.group3.gameoflife.frontend.fragments.choice_fragments.TeleportChoiceFragment;
import se.group3.gameoflife.frontend.networking.ConnectionService;
import se.group3.gameoflife.frontend.networking.VibrationCallback;
import se.group3.gameoflife.frontend.viewmodels.GameBoardViewModel;
import se2.group3.gameoflife.frontend.R;
import se.group3.gameoflife.frontend.dto.CellDTO;
import se.group3.gameoflife.frontend.dto.LobbyDTO;
import se.group3.gameoflife.frontend.dto.PlayerDTO;
import se.group3.gameoflife.frontend.fragments.ChoosePathFragment;
import se.group3.gameoflife.frontend.fragments.WinScreenFragment;


public class GameActivity extends AppCompatActivity {

    private GameBoardViewModel gameViewModel;
    private boolean gameHasStarted = false;
    private static final int MIN_INTERVAL = 60000;
    private static final int MAX_INTERVAL = 100000;
    private final Random random = new Random();
    private final Handler handler = new Handler();

    private final Runnable vibrateTask = new Runnable() {
        @Override
        public void run() {
            vibrate();
            int nextInterval = random.nextInt(MAX_INTERVAL - MIN_INTERVAL + 1) + MIN_INTERVAL;
            handler.postDelayed(this, nextInterval);
        }
    };

    private static final String TAG = "Networking";
    private ConnectionService connectionService;
    private CompositeDisposable compositeDisposable;
    private boolean isBound = false;
    private final MutableLiveData<Boolean> serviceBound = new MutableLiveData<>();

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ConnectionService.ConnectionServiceBinder binder = (ConnectionService.ConnectionServiceBinder) service;
            connectionService = binder.getService();
            isBound = true;
            serviceBound.setValue(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            serviceBound.setValue(false);
        }
    };

    @Override
    protected void onStart() {
        Intent intent = new Intent(this, ConnectionService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(vibrateTask);
        compositeDisposable.dispose();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        gameViewModel = new ViewModelProvider(this).get(GameBoardViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        serviceBound.observe(this, isConnectionServiceBound -> {
            if (Boolean.TRUE.equals(isConnectionServiceBound)) {
                connectionService.getLiveData(LobbyDTO.class).observe(this, v -> startVibrationFeature(this::vibrate));
                Log.d(TAG, "Attempting to load Decision fragment!");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new ChoosePathFragment())
                        .commit();


                connectionService.getLiveData(LobbyDTO.class).observe(this, lobby -> {
                    String uuid = connectionService.getUuidLiveData().getValue();
                    if(lobby.isHasDecision()){
                        if(uuid != null && uuid.equals(lobby.getCurrentPlayer().getPlayerUUID())){
                            makeDecision(lobby);
                        } else{
                            makeDecisionOtherPlayers(lobby);
                        }
                    } else{
                        handleCell(lobby);
                    }

                });
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //deactivate back button
            }
        });
    }

    public void setFragmentVisibility() {
        View fragment = findViewById(R.id.fragmentContainerView2);
        if (fragment != null) {
            fragment.setVisibility(View.VISIBLE);
        }
    }

    private void startVibrationFeature(VibrationCallback callback) {
        vibrateTask.run();
        LobbyDTO lobby = connectionService.getLiveData(LobbyDTO.class).getValue();
        assert lobby != null;
        compositeDisposable.add(connectionService.subscribeEvent("/topic/lobbies/" + lobby.getLobbyID() + "/vibrate", callback));
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    public ConnectionService getService() {
        return connectionService;
    }

    public MutableLiveData<Boolean> getIsBound() {
        return this.serviceBound;
    }


    private void makeDecision(LobbyDTO lobbyDTO) {
        List<CareerCardDTO> careerCardDTOS = lobbyDTO.getCareerCardDTOS();
        List<HouseCardDTO> houseCardDTOS = lobbyDTO.getHouseCardDTOS();
        Map<Integer, CellDTO> cellDTOHashMap = gameViewModel.getCellDTOHashMap();
        PlayerDTO currentPlayer = lobbyDTO.getCurrentPlayer();
        int currentCellPosition = currentPlayer.getCurrentCellPosition();
        Log.d(TAG, "Current cell position - make decision " + currentCellPosition);
        String cellType;

        try {
            CellDTO cell = cellDTOHashMap.get(currentCellPosition);
            assert cell != null;
            cellType = cell.getType();
        } catch (NullPointerException e) {
            Log.e(TAG, "CellDTO error in makeDecision: " + e.getMessage());
            return;
        }


        if (careerCardDTOS.isEmpty() && houseCardDTOS.isEmpty() && (!cellType.equals("TELEPORT"))) {
            StopCellFragment stopCellFragment = StopCellFragment.newInstance(cellType);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView2, stopCellFragment);
            transaction.commitAllowingStateLoss();
        } else if (cellType.equals("TELEPORT")) {
            TeleportChoiceFragment teleportChoiceFragment = TeleportChoiceFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView2, teleportChoiceFragment);
            transaction.commitAllowingStateLoss();
        } else {
            if (!careerCardDTOS.isEmpty()) {
                careerDecision(careerCardDTOS);
            }
            if (!houseCardDTOS.isEmpty()) {
                houseDecision(houseCardDTOS);
            }
        }
    }

    private void houseDecision(List<HouseCardDTO> houseCardDTOS){
        Log.d(TAG, "HouseCardList is not empty in LobbyDTO");
        if (houseCardDTOS.size() != 2) {
            Toast.makeText(this, "Not enough money to buy a house.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Not 2 houses provided...");
        } else {
            HouseChoiceFragment houseChoiceFragment = new HouseChoiceFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView2, houseChoiceFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void careerDecision(List<CareerCardDTO> careerCardDTOS){
        Log.d(TAG, "CareerCardList is not empty in LobbyDTO");
        if (careerCardDTOS.size() != 2) {
            Log.e(TAG, "Not 2 careers provided...");
        } else {
            CareerChoiceFragment careerChoiceFragment = new CareerChoiceFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView2, careerChoiceFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void retire() {
        LobbyDTO lobbyDTO = connectionService.getLiveData(LobbyDTO.class).getValue();

        if (lobbyDTO != null && !lobbyDTO.isHasStarted()) {
            FragmentTransaction transactionOverLay = getSupportFragmentManager().beginTransaction();
            WinScreenFragment winScreenFragment = new WinScreenFragment();
            transactionOverLay.replace(R.id.fragmentContainerView2, winScreenFragment);
            transactionOverLay.commit();
        }
    }

    private void handleCell(LobbyDTO lobbyDTO){
        Map<Integer, CellDTO> cellDTOHashMap = gameViewModel.getCellDTOHashMap();
        PlayerDTO previousPlayer = findPreviousPlayer(lobbyDTO);
        int currentCellPosition = previousPlayer.getCurrentCellPosition();
        Log.d(TAG, "Current cell position: " + currentCellPosition);
        String cellType;

        try {
            CellDTO cell = cellDTOHashMap.get(currentCellPosition);
            cellType = cell.getType();
        } catch (NullPointerException e) {
            Log.e(TAG, "CellDTO error in handleCell: " + e.getMessage());
            return;
        }
        String playerName = previousPlayer.getPlayerName();

        Log.d(TAG, cellType);
        switch (cellType) {
            case "CASH":
                Toast.makeText(this, playerName + " got a bonus salary...", Toast.LENGTH_LONG).show();
                break;
            case "ACTION":
                if(!lobbyDTO.getActionCardDTOs().isEmpty()) {
                ActionCardFragment actionCardFragment = ActionCardFragment.newInstance(playerName);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView2, actionCardFragment);
                transaction.commitAllowingStateLoss();
                Toast.makeText(this, playerName + " got an action card...", Toast.LENGTH_LONG).show();
                }
                break;
            case "FAMILY":
                Toast.makeText(this, playerName+ "  got an additional peg!", Toast.LENGTH_LONG).show();
                break;
            case "MID_LIFE":
                Toast.makeText(this, "Will " + playerName + " get an mid life crisis...?", Toast.LENGTH_LONG).show();
                break;
            case "RETIREMENT":
                Toast.makeText(this, playerName + " retired!", Toast.LENGTH_LONG).show();
                if(!lobbyDTO.isHasStarted()){
                    retire();
                }
                break;
            case "GRADUATE":
                graduateCase(playerName, previousPlayer);
                break;
            case "NOTHING":
                handleToastsNOTHING(currentCellPosition, previousPlayer, lobbyDTO);
                break;
            case "HOUSE":
                Log.d(TAG, "" + lobbyDTO.getHouseCardDTOS().size());
                Log.d(TAG, "" + lobbyDTO.isHasDecision());
                Log.d(TAG, "House Case");
                break;
            case "CAREER":
                Log.d(TAG, "" + lobbyDTO.getCareerCardDTOS().size());
                Log.d(TAG, "" + lobbyDTO.isHasDecision());
                Log.d(TAG, "Career Case");
                break;
            default:
                Log.d(TAG, "Something went wrong in handleCell");
        }
    }

    private void graduateCase(String playerName, PlayerDTO previousPlayer){
        Toast.makeText(this, playerName + " gets ready for exams...", Toast.LENGTH_LONG).show();
        if (previousPlayer.isCollegeDegree()) {
            Toast.makeText(this, playerName + " aced the exams!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, playerName + " messed up the exams and did not pass college, maybe in another life?", Toast.LENGTH_LONG).show();
        }
    }

    private void handleToastsNOTHING(int currentCellPosition, PlayerDTO previousPlayer, LobbyDTO lobbyDTO){
        String playerName = previousPlayer.getPlayerName();
        switch (currentCellPosition) {
            case 1:
                Log.d(TAG, "College");
                if(!gameHasStarted){
                    handleBeginningToasts(lobbyDTO);
                    gameHasStarted = true;
                }
                break;
            case 13:
                if (previousPlayer.isCollegeDegree()) {
                    Toast.makeText(this, playerName + " aced his exams. Congrats to your degree!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, playerName + " messed up the exams... You did not pass college, maybe in another life?", Toast.LENGTH_LONG).show();
                }
                break;
            case 14:
                Log.d(TAG, "Career");
                if(!gameHasStarted){
                    handleBeginningToasts(lobbyDTO);
                    gameHasStarted = true;
                }
                break;
            case 35:
                Toast.makeText(this, playerName + " got married - yey congrats!", Toast.LENGTH_LONG).show();
                break;
            case 45:
                Toast.makeText(this, "No marriage for " + playerName + "? Okay.", Toast.LENGTH_LONG).show();
                break;
            case 58:
                Toast.makeText(this, "Welcome to the family path, " + playerName + ".", Toast.LENGTH_LONG).show();
                break;
            case 74:
                Toast.makeText(this, "No family for " + playerName + "? Okay.", Toast.LENGTH_LONG).show();
                break;
            case 92:
                Toast.makeText(this, playerName + " has slipped into a mid-life crisis.", Toast.LENGTH_LONG).show();
                break;
            case 102:
                Toast.makeText(this, "Mid life crisis? Not for " + playerName + "!", Toast.LENGTH_LONG).show();
                break;
            case 113:
                Toast.makeText(this, playerName + " is on the fastest path to retirement.", Toast.LENGTH_LONG).show();
                break;
            case 116:
                Toast.makeText(this, playerName + " is not on the fastest path to retirement...", Toast.LENGTH_LONG).show();
                break;
            default:
                Log.e(TAG, "Something happened with the cell types.");
        }
    }

    private void handleBeginningToasts(LobbyDTO lobbyDTO){
        List<PlayerDTO> playerDTOS = lobbyDTO.getPlayers();
        for(PlayerDTO playerDTO : playerDTOS){
            int currentCellPosition = playerDTO.getCurrentCellPosition();
            String playerName = playerDTO.getPlayerName();
            switch(currentCellPosition){
                case 1:
                    Toast.makeText(this, "Welcome to college, " + playerName + "!", Toast.LENGTH_LONG).show();
                    break;
                case 14:
                    Toast.makeText(this, "Welcome to the working life, " + playerName + "!", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }

    private void makeDecisionOtherPlayers(LobbyDTO lobbyDTO) {
        List<CareerCardDTO> careerCardDTOS = lobbyDTO.getCareerCardDTOS();
        List<HouseCardDTO> houseCardDTOS = lobbyDTO.getHouseCardDTOS();
        PlayerDTO currentPlayer = lobbyDTO.getCurrentPlayer();
        int currentCellPosition = currentPlayer.getCurrentCellPosition();
        Log.d(TAG, "Current cell position - make decision " + currentCellPosition);
        String playerName = currentPlayer.getPlayerName();

        if (careerCardDTOS.isEmpty() && houseCardDTOS.isEmpty()) {
            Toast.makeText(this, playerName + " makes a life-deciding decision...", Toast.LENGTH_LONG).show();
        } else {
            if (!careerCardDTOS.isEmpty()) {
                Toast.makeText(this, playerName + " chooses a new career...", Toast.LENGTH_LONG).show();
            }
            if (!houseCardDTOS.isEmpty()) {
                Toast.makeText(this, playerName + " buys a house...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private PlayerDTO findPreviousPlayer(LobbyDTO lobbyDTO){
        List<PlayerDTO> players = lobbyDTO.getPlayers();
        PlayerDTO currentPlayer = lobbyDTO.getCurrentPlayer();

        int index = 0;
        for(int i = 0; i < players.size(); i++){
            if(currentPlayer.getPlayerUUID().equals(players.get(i).getPlayerUUID())){
                index = i;
            }
        }

        Log.d(TAG, "Current index: "+index);
        int previousIndex;
        if (index >= 1){
            previousIndex = index-1;
        } else{
            previousIndex = players.size()-1;
        }

        Log.d(TAG, "previous index: " + previousIndex);
        return players.get(previousIndex);
    }

}