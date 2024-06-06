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
    //todo: change visibility of buttons depending on how many people are in the lobby
    //todo: make buttons toggle between player name and player stats
    //todo: implement long press for report
    //todo: add button for spinning
    //todo: add button for cheating / fake cheating (fake cheating also needs to be added in the backend)

    public OverlayFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overlay, container, false);

        Button player1Button = rootView.findViewById(R.id.player1Button);
        Button player2Button = rootView.findViewById(R.id.player2Button);
        Button player3Button = rootView.findViewById(R.id.player3Button);
        Button player4Button = rootView.findViewById(R.id.player4Button);
        Button spinButton = rootView.findViewById(R.id.spinButton);
        Button cheatButton = rootView.findViewById(R.id.cheatButton);

        player1Button.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        player1Button.setOnLongClickListener(v -> {
            gameViewModel.report(0);
            return true;
        });
        player2Button.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        player2Button.setOnLongClickListener(v -> {
            gameViewModel.report(1);
            return true;
        });
        player3Button.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        player3Button.setOnLongClickListener(v -> {
            gameViewModel.report(2);
            return true;
        });
        player4Button.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        player4Button.setOnLongClickListener(v -> {
            gameViewModel.report(3);
            return true;
        });
        cheatButton.setOnClickListener(view -> {
            gameViewModel.cheat();
            vibratePhone();
        });
        cheatButton.setOnLongClickListener(v -> {
            vibratePhone();
            return true;
        });
        spinButton.setOnClickListener(view -> {
            //todo: handle short click player1button
        });

        return rootView;
    }

    private void vibratePhone() {
        //todo: implement vibrations
    }
    private void updateLobby(LobbyDTO lobbyDTO){
        gameViewModel.setLobbyDTO(lobbyDTO);
    }

    private void updateButton(Button[] playerButtons, PlayerDTO playerDTO){
        for (Button playerButton : playerButtons) {
            if (!playerButton.getText().equals(playerDTO.getPlayerName())) {
                playerButton.setText(playerDTO.getPlayerName());
            }
        }

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
    }

}