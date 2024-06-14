package se2.group3.gameoflife.frontend.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;

import se2.group3.gameoflife.frontend.activities.GameActivity;
import se2.group3.gameoflife.frontend.dto.CellDTO;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;

import se2.group3.gameoflife.frontend.dto.cards.CareerCardDTO;
import se2.group3.gameoflife.frontend.dto.cards.HouseCardDTO;
import se2.group3.gameoflife.frontend.fragments.choiceFragments.CareerChoiceFragment;
import se2.group3.gameoflife.frontend.fragments.choiceFragments.HouseChoiceFragment;
import se2.group3.gameoflife.frontend.fragments.choiceFragments.StopCellFragment;
import se2.group3.gameoflife.frontend.networking.ConnectionService;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class OverlayFragment extends Fragment {
    private GameViewModel gameViewModel;
    private View rootView;
    public final String TAG = "Networking";
    private boolean playerName;
    ConnectionService connectionService;
    CompositeDisposable compositeDisposable;

    public OverlayFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_overlay, container, false);
        compositeDisposable  = new CompositeDisposable();

        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        Button spinButton = rootView.findViewById(R.id.spinButton);
        Button cheatButton = rootView.findViewById(R.id.cheatButton);

        GameActivity activity = (GameActivity) getActivity();
        assert activity != null;
        activity.getConnectionService(cs -> {
            connectionService = cs;
            assert connectionService != null;

            connectionService.getLiveData(LobbyDTO.class).observe(getViewLifecycleOwner(), lobby -> {
                String uuid = connectionService.getUuidLiveData().getValue();
                if(uuid != null && uuid.equals(lobby.getCurrentPlayer().getPlayerUUID())){
                    rootView.findViewById(R.id.spinButton).setVisibility(View.VISIBLE);
                }

                //todo: move this into game activity
                if (lobby.isHasDecision()) {
                    makeDecision(lobby);
                } else {
                    handleCell(lobby);
                }

                updateStatistics(lobby);
            });

            cheatButton.setOnClickListener(view -> {
                compositeDisposable.add(connectionService.send("/app/cheat", "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                        }, error -> Log.e(TAG, "Error cheating: " + error)));
            });

            spinButton.setOnClickListener(view -> {
                compositeDisposable.add(connectionService.send("/app/lobby/spin", "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                        }, error -> Log.e(TAG, "Error spin the wheel:  " + error)));
            });
        });

        return rootView;
    }

    private void updateStatistics(LobbyDTO lobby) {
        try {
            List<PlayerDTO> players = lobby.getPlayers();

            if (players == null || players.isEmpty()) {
                Log.e(TAG, "Player list is null or empty");
                return;
            }

            Log.d(TAG, "start buttons");
            Button[] playerButtons = new Button[4];
            playerButtons[0] = rootView.findViewById(R.id.player1Button);
            playerButtons[1] = rootView.findViewById(R.id.player2Button);
            playerButtons[2] = rootView.findViewById(R.id.player3Button);
            playerButtons[3] = rootView.findViewById(R.id.player4Button);

            setPlayerNamesButton(players, playerButtons);

            for (int i = 0; i < players.size() && i < playerButtons.length; i++) {
                PlayerDTO player = players.get(i);
                Button playerButton = playerButtons[i];
                playerButton.setVisibility(View.VISIBLE);
                playerButton.setText(player.getPlayerName());
            }

            gameViewModel.getLobby().observe(getViewLifecycleOwner(), lobbyDTO1 -> {
                List<PlayerDTO> playersChanged = lobbyDTO1.getPlayers();

                playerButtons[0].setOnClickListener(v -> {
                    if(playerName){
                        updateButton(playerButtons, playersChanged, playersChanged.get(0));
                    } else{
                        setPlayerNamesButton(players, playerButtons);
                    }
                });

                playerButtons[0].setOnLongClickListener(v -> {
                    report(0);
                    return true;
                });

                playerButtons[1].setOnClickListener(v -> {
                    if(playerName){
                        updateButton(playerButtons, playersChanged, playersChanged.get(1));
                    } else{
                        setPlayerNamesButton(players, playerButtons);
                    }
                });

                playerButtons[1].setOnLongClickListener(v -> {
                    report(1);
                    return true;
                });

                playerButtons[2].setOnClickListener(v -> {
                    if(playerName){
                        updateButton(playerButtons, playersChanged, playersChanged.get(2));
                    } else{
                        setPlayerNamesButton(players, playerButtons);
                    }
                });

                playerButtons[2].setOnLongClickListener(v -> {
                    report(2);
                    return true;
                });

                playerButtons[3].setOnClickListener(v -> {
                    if(playerName){
                        updateButton(playerButtons, playersChanged, playersChanged.get(3));
                    } else{
                        setPlayerNamesButton(players, playerButtons);
                    }
                });

                playerButtons[3].setOnLongClickListener(v -> {
                    report(3);
                    return true;
                });
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage());
        }
    }

    private void setPlayerNamesButton(List<PlayerDTO> players, Button[] playerButtons){
        for (int i = 0; i < players.size() && i < playerButtons.length; i++) {
            PlayerDTO player = players.get(i);
            Button playerButton = playerButtons[i];
            playerButton.setText(player.getPlayerName());
        }
        playerName = true;
    }


    private void updateButton(Button[] playerButtons, List<PlayerDTO> players, PlayerDTO playerDTO){
        setPlayerNamesButton(players, playerButtons);

        for (Button playerButton : playerButtons){
            if(playerButton.getText().equals(playerDTO.getPlayerName())){
                if(playerDTO.getCareerCard() == null){
                    playerButton.setText("Money: " + playerDTO.getMoney() + "\nCollege: " + playerDTO.isCollegeDegree() + "\nJob: none "+ "\n#Pegs: " + playerDTO.getNumberOfPegs() +
                            "\n#houses: " + playerDTO.getHouses().size());
                } else{
                    playerButton.setText("Money: " + playerDTO.getMoney() + "\nCollege: " + playerDTO.isCollegeDegree() + "\nJob: " +playerDTO.getCareerCard().getName()+
                            "\nSalary: " + playerDTO.getCareerCard().getSalary() + "\nBonus: " + playerDTO.getCareerCard().getBonus() + "\n#Pegs: " + playerDTO.getNumberOfPegs() +
                            "\n#houses: " + playerDTO.getHouses().size());
                }
            }
        }
        playerName = false;
    }


    private void handleCell(LobbyDTO lobbyDTO){
        HashMap<Integer, CellDTO> cellDTOHashMap = gameViewModel.getCellDTOHashMap();
        PlayerDTO currentPlayer = lobbyDTO.getCurrentPlayer();
        int currentCellPosition = currentPlayer.getCurrentCellPosition();
        Log.d(TAG, "Current cell position: " + currentCellPosition);
        String cellType;

        try{
            cellType = cellDTOHashMap.get(currentCellPosition).getType();
        } catch(NullPointerException e){
            Log.e(TAG, "CellDTO error in handleCell: " + e.getMessage());
            return;
        }

        Log.d(TAG, cellType);
        switch(cellType) {
            case "CASH":
                Toast.makeText(requireContext(), "You got your bonus salary, yey!", Toast.LENGTH_LONG).show();
                break;
            case "ACTION":
                Toast.makeText(requireContext(), "Action cell", Toast.LENGTH_LONG).show();
                break;
            case "FAMILY":
                Toast.makeText(requireContext(), "You got an additional peg!", Toast.LENGTH_LONG).show();
                break;
            case "MID_LIFE":
                Toast.makeText(requireContext(), "Life is not that easy...", Toast.LENGTH_LONG).show();
                break;
            case "RETIREMENT":
                Toast.makeText(requireContext(), "Welcome to retirement!", Toast.LENGTH_LONG).show();
                retire();
                break;
            case "GRADUATE":
                Toast.makeText(requireContext(), "Time for your exams...", Toast.LENGTH_LONG).show();
                if(currentPlayer.isCollegeDegree()){
                    Toast.makeText(requireContext(), "You aced your exams. Congrats to your degree!", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(requireContext(), "You messed up your exams... You did not pass college, maybe in another life?", Toast.LENGTH_LONG).show();
                }
                break;
            case "NOTHING":
                handleCellNOTHING(currentCellPosition,currentPlayer);
                break;
            case "HOUSE":
                Log.d(TAG, ""+ lobbyDTO.getHouseCardDTOS().size());
                Log.d(TAG, ""+ lobbyDTO.isHasDecision());
                Log.d(TAG, "House Case");
                break;
            case "CAREER":
                Log.d(TAG, ""+ lobbyDTO.getCareerCardDTOS().size());
                Log.d(TAG, ""+ lobbyDTO.isHasDecision());
                Log.d(TAG, "Career Case");
                break;
            default:
                Log.d(TAG, "Something went wrong in handleCell");
        }
    }

    private void handleCellNOTHING(int currentCellPosition, PlayerDTO player){
        switch(currentCellPosition){
            case 1:
                Toast.makeText(requireContext(), "Welcome to college!", Toast.LENGTH_LONG).show();
                break;
            case 13:
                if(player.isCollegeDegree()){
                    Toast.makeText(requireContext(), "You aced your exams. Congrats to your degree!", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(requireContext(), "You messed up your exams... You did not pass college, maybe in another life?", Toast.LENGTH_LONG).show();
                }
                break;
            case 14:
                Toast.makeText(requireContext(), "Welcome to the working life!", Toast.LENGTH_LONG).show();
                break;
            case 35:
                Toast.makeText(requireContext(), "You got married - yey congrats!", Toast.LENGTH_LONG).show();
                break;
            case 45:
                Toast.makeText(requireContext(), "No marriage? Okay.", Toast.LENGTH_LONG).show();
                break;
            case 58:
                Toast.makeText(requireContext(), "Welcome to the family path.", Toast.LENGTH_LONG).show();
                break;
            case 74:
                Toast.makeText(requireContext(), "No family? Okay.", Toast.LENGTH_LONG).show();
                break;
            case 92:
                Toast.makeText(requireContext(), "You have slipped into a mid-life crisis.", Toast.LENGTH_LONG).show();
                break;
            case 102:
                Toast.makeText(requireContext(), "Mid life crisis? Not for you!", Toast.LENGTH_LONG).show();
                break;
            case 113:
                Toast.makeText(requireContext(), "You are on the fastest path to retirement.", Toast.LENGTH_LONG).show();
                break;
            case 116:
                Toast.makeText(requireContext(), "You are not on the fastest path to retirement...", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void makeDecision(LobbyDTO lobbyDTO) {
        List<CareerCardDTO> careerCardDTOS = lobbyDTO.getCareerCardDTOS();
        List<HouseCardDTO> houseCardDTOS = lobbyDTO.getHouseCardDTOS();
        HashMap<Integer, CellDTO> cellDTOHashMap = gameViewModel.getCellDTOHashMap();
        PlayerDTO currentPlayer = lobbyDTO.getCurrentPlayer();
        int currentCellPosition = currentPlayer.getCurrentCellPosition();
        Log.d(TAG, "Current cell position - make decision " + currentCellPosition);
        String cellType;

        try {
            cellType = cellDTOHashMap.get(currentCellPosition).getType();
        } catch (NullPointerException e) {
            Log.e(TAG, "CellDTO error in makeDecision: " + e.getMessage());
            return;
        }


        FragmentTransaction transactionOverLay = requireActivity().getSupportFragmentManager().beginTransaction();

        if (careerCardDTOS.isEmpty() && houseCardDTOS.isEmpty()) {
            StopCellFragment stopCellFragment = StopCellFragment.newInstance(cellType);
            transactionOverLay.replace(R.id.fragmentContainerView2, stopCellFragment);
        } else {
            if (!careerCardDTOS.isEmpty()) {
                Log.d(TAG, "CareerCardList is not empty in LobbyDTO");
                if (careerCardDTOS.size() != 2) {
                    Log.e(TAG, "Not 2 careers provided...");
                } else {
                    CareerChoiceFragment careerChoiceFragment = new CareerChoiceFragment();
                    transactionOverLay.replace(R.id.fragmentContainerView2, careerChoiceFragment);
                }
            }
            if (!houseCardDTOS.isEmpty()) {
                Log.d(TAG, "HouseCardList is not empty in LobbyDTO");
                if (houseCardDTOS.size() != 2) {
                    Toast.makeText(requireContext(), "Not enough money to buy a house.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Not 2 houses provided...");
                } else {
                    HouseChoiceFragment houseChoiceFragment = new HouseChoiceFragment();
                    transactionOverLay.replace(R.id.fragmentContainerView2, houseChoiceFragment);
                }
            }
        }
        transactionOverLay.commit();
    }

    private void report(int player){
        LobbyDTO lobby = connectionService.getLiveData(LobbyDTO.class).getValue();
        if(lobby == null) {
            Log.e("Networking", "Report has failed: lobbyDTO is not initialized yet!");
            return;
        }

        String playerUUID = lobby.getPlayers().get(player).getPlayerUUID();
        compositeDisposable.add(connectionService.send("/app/report", playerUUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, error -> Log.e(TAG, "Error Sending Create Lobby: " + error)));

    }

    private void retire(){
        LobbyDTO lobbyDTO = connectionService.getLiveData(LobbyDTO.class).getValue();

        if (lobbyDTO != null && !lobbyDTO.isHasStarted()){
            FragmentTransaction transactionOverLay = requireActivity().getSupportFragmentManager().beginTransaction();
            WinScreenFragment winScreenFragment = new WinScreenFragment();
            transactionOverLay.replace(R.id.fragmentContainerView2, winScreenFragment);
            transactionOverLay.commit();
        }
    }

}