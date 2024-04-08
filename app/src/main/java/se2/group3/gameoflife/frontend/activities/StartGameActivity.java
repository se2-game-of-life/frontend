package se2.group3.gameoflife.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.reactivex.disposables.Disposable;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.JoinLobbyRequest;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.ResponseHandler;
import se2.group3.gameoflife.frontend.util.SerializationUtil;

public class StartGameActivity extends AppCompatActivity {
    private static final String TAG = "StartGameActivity";
    private boolean waiting = true;
    private int numberPlayers = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                waiting = false;
                //todo: change to gameplay activity
            }
        });

        TextView lobbyID = findViewById(R.id.lobbyID);
        LobbyDTO lobbyDTO = LobbyActivity.getLobbyDTO();
        lobbyID.setText("ID: " + lobbyDTO.getLobbyID());

        while(waiting){
            numberPlayers = lobbyDTO.getPlayers().length;
            PlayerDTO[] players = lobbyDTO.getPlayers();
            for (int i = 1; i <= numberPlayers; i++){
                switch(i){
                    case 1:
                        Button player1 = findViewById(R.id.player1Button);
                        player1.setText(players[0].getPlayerName());
                        player1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        Button player2 = findViewById(R.id.player2Button);
                        player2.setText(players[1].getPlayerName());
                        player2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        Button player3 = findViewById(R.id.player3Button);
                        player3.setText(players[2].getPlayerName());
                        player3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        Button player4 = findViewById(R.id.player4Button);
                        player4.setText(players[3].getPlayerName());
                        player4.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
            }
        }

    }
}