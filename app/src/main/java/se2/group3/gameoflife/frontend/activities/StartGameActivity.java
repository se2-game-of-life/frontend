package se2.group3.gameoflife.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import se2.group3.gameoflife.frontend.networking.ResponseHandler;
import se2.group3.gameoflife.frontend.util.SerializationUtil;

public class StartGameActivity extends AppCompatActivity {
    private static final String TAG = "StartGameActivity";

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

        ResponseHandler getLobbyIDResponseHandler = new ResponseHandler() {
            @Override
            public void handleMessage(String msg) {
                try {
                    LobbyDTO lobbyDTO = (LobbyDTO) SerializationUtil.toObject(msg, LobbyDTO.class);
                    Log.d(TAG, "Lobby: " + lobbyDTO.getLobbyID());
                    TextView lobbyID = findViewById(R.id.lobbyID);
                    lobbyID.setText("ID: " + lobbyDTO.getLobbyID());
                } catch (JsonProcessingException e) {
                    Log.e(TAG, "Error processing incoming LobbyDTO!", e.getCause());
                }
            }

            /**
             * Error handling yet missing. Will be implemented in the next sprint.
             */
            @Override
            public void handleError() {}
        };

        Disposable topicSubscription = MainActivity.getNetworkHandler().subscribe("/topic/lobbies", getLobbyIDResponseHandler);



        findViewById(R.id.buttonReturnToLobby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGameActivity.this, LobbyActivity.class);
                startActivity(intent);
            }
        });
    }
}