package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class OverlayFragment extends Fragment {
    private GameViewModel gameViewModel;
    private View rootView;
    public final String TAG = "Networking";
    private boolean playerName;

    private void getLobbyDTO() {
        if (getArguments() != null) {
            String lobbyDTOJson = getArguments().getString("lobbyDTO");
            if (lobbyDTOJson != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    LobbyDTO lobbyDTO = objectMapper.readValue(lobbyDTOJson, LobbyDTO.class);
                    if (lobbyDTO != null) {
                        gameViewModel.setLobbyDTO(lobbyDTO);
                        gameViewModel.getLobby().observe(getViewLifecycleOwner(), this::updateLobby);
                    }
                } catch (NullPointerException | JsonProcessingException e) {
                    Log.d("Networking", "Exception: " + e.getMessage());
                }
            }
        }
    }

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
        getLobbyDTO();
        updateStatistics();

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
            //todo: handle short click player1button
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
}