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

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.activities.GameActivity;
import se2.group3.gameoflife.frontend.dto.CellDTO;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.fragments.choiceFragments.CareerChoiceFragment;
import se2.group3.gameoflife.frontend.fragments.choiceFragments.HouseChoiceFragment;
import se2.group3.gameoflife.frontend.fragments.choiceFragments.CareerChoiceFragment;
import se2.group3.gameoflife.frontend.fragments.choiceFragments.StopCellFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class OverlayFragment extends Fragment {
    private GameViewModel gameViewModel;
    private View rootView;
    public final String TAG = "Networking";
    private boolean playerName;


    public OverlayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_overlay, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        updateStatistics();
        PlayerDTO currentPlayer = gameViewModel.getLobbyDTO().getCurrentPlayer();
        if(currentPlayer.getCurrentCellPosition() == 1 || currentPlayer.getCurrentCellPosition() == 14){
            handleCellNOTHING(currentPlayer.getCurrentCellPosition(), currentPlayer);
        }

        Button spinButton = rootView.findViewById(R.id.spinButton);
        Button cheatButton = rootView.findViewById(R.id.cheatButton);

        cheatButton.setOnClickListener(view -> {
            gameViewModel.cheat();
            vibratePhone();
        });
        cheatButton.setOnLongClickListener(v -> {
            //todo: implement fake cheat
            vibratePhone();
            return true;
        });
        spinButton.setOnClickListener(view -> {
            gameViewModel.spinWheel();
            handleCell();
        });

        return rootView;
    }

    private void updateStatistics() {
        try {
            LobbyDTO lobbyDTO = gameViewModel.getLobbyDTO();
            List<PlayerDTO> players = lobbyDTO.getPlayers();

            if (players == null || players.isEmpty()) {
                Log.e(TAG, "Playerlist is null or empty");
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
                    gameViewModel.report(0);
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
                    gameViewModel.report(1);
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
                    gameViewModel.report(2);
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
                    gameViewModel.report(3);
                    return true;
                });
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage());
        }
    }

    private void vibratePhone() {
        //todo: implement vibrations
    }

    private void setPlayerNamesButton(List<PlayerDTO> players, Button[] playerButtons){
        for (int i = 0; i < players.size() && i < playerButtons.length; i++) {
            PlayerDTO player = players.get(i);
            Button playerButton = playerButtons[i];
            playerButton.setText(player.getPlayerName());
        }
        playerName = true;
    }
    private void updateLobby(LobbyDTO lobbyDTO){
        gameViewModel.setLobbyDTO(lobbyDTO);
    }

    private void updateButton(Button[] playerButtons, List<PlayerDTO> players, PlayerDTO playerDTO){
        setPlayerNamesButton(players, playerButtons);

        for (Button playerButton : playerButtons){
            if(playerButton.getText().equals(playerDTO.getPlayerName())){
                if(playerDTO.getCareerCard() == null){
                    playerButton.setText("Money: " + playerDTO.getMoney() + "\nCollege: " + playerDTO.isCollegeDegree() + "\nJob: none "+
                            "\n#houses: " + playerDTO.getHouses().size());
                } else{
                    playerButton.setText("Money: " + playerDTO.getMoney() + "\nCollege: " + playerDTO.isCollegeDegree() + "\nJob: " +playerDTO.getCareerCard().toString()+
                            "]\n#houses: " + playerDTO.getHouses().size());
                }
            }
        }
        playerName = false;
    }


    private void handleCell(){
        HashMap<Integer, CellDTO> cellDTOHashMap = gameViewModel.getCellDTOHashMap();
        PlayerDTO currentPlayer = gameViewModel.getLobbyDTO().getCurrentPlayer();
        int currentCellPosition = currentPlayer.getCurrentCellPosition();
        String cellType;
        try{
            cellType = cellDTOHashMap.get(currentCellPosition).getType();
        } catch(NullPointerException e){
            Log.e(TAG, "CellDTO error: " + e.getMessage());
            return;
        }

        FragmentTransaction transactionOverLay = requireActivity().getSupportFragmentManager().beginTransaction();
        OverlayFragment overlayFragment = new OverlayFragment();
        transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);

        StopCellFragment stopCellFragment;

        Log.d(TAG, cellType);
        switch(cellType) {
            case "CASH":
                Toast.makeText(requireContext(), "You got your salary!", Toast.LENGTH_LONG).show();
                break;
            case "ACTION":

                break;
            case "FAMILY":
                Toast.makeText(requireContext(), "You got an additional peg!", Toast.LENGTH_LONG).show();
                break;
            case "HOUSE":
                HouseChoiceFragment houseChoiceFragment = new HouseChoiceFragment();
                transactionOverLay.replace(R.id.fragmentContainerView2, houseChoiceFragment);
                break;
            case "CAREER":
                CareerChoiceFragment careerChoiceFragment = new CareerChoiceFragment();
                transactionOverLay.replace(R.id.fragmentContainerView2, careerChoiceFragment);
                break;
            case "MID_LIFE":
                Toast.makeText(requireContext(), "Life is not that easy...", Toast.LENGTH_LONG).show();
                break;
            case "MARRY":
                stopCellFragment = StopCellFragment.newInstance(cellType);
                transactionOverLay.replace(R.id.fragmentContainerView2, stopCellFragment);
                break;
            case "GROW_FAMILY":
                stopCellFragment = StopCellFragment.newInstance(cellType);
                transactionOverLay.replace(R.id.fragmentContainerView2, stopCellFragment);
                break;

            case "RETIRE_EARLY":
                stopCellFragment = StopCellFragment.newInstance(cellType);
                transactionOverLay.replace(R.id.fragmentContainerView2, stopCellFragment);
                break;
            case "RETIREMENT":
                Toast.makeText(requireContext(), "Welcome to retirement!", Toast.LENGTH_LONG).show();
                break;
            case "NOTHING":
                handleCellNOTHING(currentCellPosition,currentPlayer);
                break;
            default:
                Log.d(TAG, "Something went wrong in handleCell");
        }

        transactionOverLay.addToBackStack(null);
        transactionOverLay.commit();
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
}