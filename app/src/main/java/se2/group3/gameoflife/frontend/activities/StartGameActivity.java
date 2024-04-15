package se2.group3.gameoflife.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.viewmodels.StartGameViewModel;


public class StartGameActivity extends AppCompatActivity {

    private StartGameViewModel startGameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGameViewModel = new ViewModelProvider(this).get(StartGameViewModel.class);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonReturnToLobby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGameActivity.this, LobbyActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.StartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new UnsupportedOperationException();
            }
        });

//        LobbyDTO lobbyInformation = getIntent().getParcelableExtra("lobbyDTO");
//        if(lobbyInformation == null) throw new RuntimeException("LobbyDTO not found in the StartGameActivity");
//        updateLobby(lobbyInformation);

//        startGameViewModel.getLobby().observe(StartGameActivity.this, this::updateLobby);
    }

//    private void updateLobby(LobbyDTO lobbyDTO) {
//        TextView lobbyID = findViewById(R.id.lobbyID);
//        lobbyID.setText("ID: " + lobbyDTO.getLobbyID());
//        int numberPlayers = lobbyDTO.getPlayers().length;
//        PlayerDTO[] players = lobbyDTO.getPlayers();
//        for (int i = 1; i <= numberPlayers; i++){
//            switch(i){
//                case 1:
//                    Button player1 = findViewById(R.id.player1Button);
//                    player1.setText(players[0].getPlayerName());
//                    player1.setVisibility(View.VISIBLE);
//                    break;
//                case 2:
//                    Button player2 = findViewById(R.id.player2Button);
//                    player2.setText(players[1].getPlayerName());
//                    player2.setVisibility(View.VISIBLE);
//                    break;
//                case 3:
//                    Button player3 = findViewById(R.id.player3Button);
//                    player3.setText(players[2].getPlayerName());
//                    player3.setVisibility(View.VISIBLE);
//                    break;
//                case 4:
//                    Button player4 = findViewById(R.id.player4Button);
//                    player4.setText(players[3].getPlayerName());
//                    player4.setVisibility(View.VISIBLE);
//                    break;
//                default:
//            }
//        }
//    }
}