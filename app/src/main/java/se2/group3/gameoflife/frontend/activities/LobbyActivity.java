package se2.group3.gameoflife.frontend.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class LobbyActivity extends AppCompatActivity {

    private final static String TAG = "LobbyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button createLobbyButton = findViewById(R.id.createButton);
        createLobbyButton.setOnClickListener(v -> createLobby(new PlayerDTO("Test Player Name")));

        Button joinGameButton = findViewById(R.id.joinButton);
        joinGameButton.setOnClickListener(v -> joinLobby(0L, new PlayerDTO("Test Player 2 Name")));
    }

    private void joinLobby(long lobbyID, PlayerDTO player) {
        ResponseHandler lobbyJoinResponseHandler = new ResponseHandler() {
            @Override
            public void handleMessage(String msg) {
                try {
                    LobbyDTO lobbyDTO = (LobbyDTO) SerializationUtil.toObject(msg, LobbyDTO.class);
                    Log.d(TAG, "Lobby: " + lobbyDTO.getHost());
                } catch (JsonProcessingException e) {
                    Log.e(TAG, "Error processing incoming LobbyDTO!", e.getCause());
                }
            }
            @Override
            public void handleError() {}
        };

        Disposable topicSubscription = MainActivity.getNetworkHandler().subscribe("/topic/lobbies", lobbyJoinResponseHandler);

        try {
            Disposable sendSubscription = MainActivity.getNetworkHandler().send("/app/lobby/join", new JoinLobbyRequest(lobbyID, player));
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Error converting PlayerDTO into json string!", e.getCause());
        }
    }

    private void createLobby(PlayerDTO player) {
        ResponseHandler lobbyResponseHandler = new ResponseHandler() {
            @Override
            public void handleMessage(String msg) {
                try {
                    LobbyDTO lobbyDTO = (LobbyDTO) SerializationUtil.toObject(msg, LobbyDTO.class);
                    Log.d(TAG, "Lobby: " + lobbyDTO.getLobbyID());
                } catch (JsonProcessingException e) {
                    Log.e(TAG, "Error processing incoming LobbyDTO!", e.getCause());
                }
            }
            @Override
            public void handleError() {}
        };

        Disposable topicSubscription = MainActivity.getNetworkHandler().subscribe("/topic/lobbies", lobbyResponseHandler);

        try {
            Disposable sendSubscription = MainActivity.getNetworkHandler().send("/app/lobby/create", player);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Error converting PlayerDTO into json string!", e.getCause());
        }
    }
}