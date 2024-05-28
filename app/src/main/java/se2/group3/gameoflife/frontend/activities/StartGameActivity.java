package se2.group3.gameoflife.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.StartGameViewModel;


public class StartGameActivity extends AppCompatActivity {

    private StartGameViewModel startGameViewModel;
    private ObjectMapper objectMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGameViewModel = new ViewModelProvider(this).get(StartGameViewModel.class);
        objectMapper = new ObjectMapper();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonReturnToLobby).setOnClickListener(v -> {
            Intent intent = new Intent(StartGameActivity.this, MenuActivity.class);
            startActivity(intent);
            //todo: handle player leave lobby
        });

        findViewById(R.id.StartButton).setOnClickListener(v -> {
            Intent intent = new Intent(StartGameActivity.this, GameActivity.class);
            try {
                intent.putExtra("lobbyDTO", objectMapper.writeValueAsString(startGameViewModel.getLobbyDTO()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            startActivity(intent);
        });

        try {
            LobbyDTO lobby = objectMapper.readValue(getIntent().getStringExtra("lobbyDTO"), LobbyDTO.class);
            startGameViewModel.setLobbyDTO(lobby);
            updateLobby(startGameViewModel.getLobbyDTO());
            startGameViewModel.getLobbyUpdates(lobby.getLobbyID());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        startGameViewModel.getLobby().observe(StartGameActivity.this, this::updateLobby);
    }

    private void updateLobby(LobbyDTO lobbyDTO) {
        if(lobbyDTO.isHasStarted()) {
            Intent intent = new Intent(StartGameActivity.this, GameActivity.class);
            try {
                intent.putExtra("lobbyDTO", objectMapper.writeValueAsString(lobbyDTO));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            startActivity(intent);
        }

        TextView lobbyID = findViewById(R.id.lobbyID);
        lobbyID.setText(String.format("ID: %s", lobbyDTO.getLobbyID()));
        int numberPlayers = lobbyDTO.getPlayers().size();
        List<PlayerDTO> players = lobbyDTO.getPlayers();
        for (int i = 1; i <= numberPlayers; i++){
            switch(i){
                case 1:
                    Button player1 = findViewById(R.id.player1Button);
                    player1.setText(players.get(0).getPlayerName());
                    player1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    Button player2 = findViewById(R.id.player2Button);
                    player2.setText(players.get(1).getPlayerName());
                    player2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    Button player3 = findViewById(R.id.player3Button);
                    player3.setText(players.get(2).getPlayerName());
                    player3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    Button player4 = findViewById(R.id.player4Button);
                    player4.setText(players.get(3).getPlayerName());
                    player4.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    }
}