package se2.group3.gameoflife.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

    private static final String TAG = "Networking";
    private static LobbyDTO lobbyDTO;

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

        findViewById(R.id.buttonReturnToUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.buttonCreateNewGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLobby(new PlayerDTO(MainActivity.getUsername()));
            }
        });

        findViewById(R.id.buttonJoinGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_join_game);
                findViewById(R.id.GObutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          EditText lobbyIDText = findViewById(R.id.lobbyCodeEntry);
                          String lobbyIDString = lobbyIDText.getText().toString();
                          if (!lobbyIDString.isEmpty()){
                            long lobbyID = Long.parseLong(lobbyIDString);
                            joinLobby(lobbyID, new PlayerDTO(MainActivity.getUsername()));
                        }
                    }
                });
            }
        });
    }

    private void joinLobby(long lobbyID, PlayerDTO player) {
        ResponseHandler lobbyJoinResponseHandler = new ResponseHandler() {
            @Override
            public void handleMessage(String msg) {
                try {
                    Log.d(TAG, "Received Lobby Data!");
                    lobbyDTO = (LobbyDTO) SerializationUtil.toObject(msg, LobbyDTO.class);
                    Intent intent = new Intent(LobbyActivity.this, StartGameActivity.class);
                    startActivity(intent);
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

        Disposable topicSubscription = MainActivity.getNetworkHandler().subscribe("/topic/lobbies/" + lobbyID, lobbyJoinResponseHandler);

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
                    lobbyDTO = (LobbyDTO) SerializationUtil.toObject(msg, LobbyDTO.class);
                    Log.d(TAG, "Lobby: " + lobbyDTO.getLobbyID());

                    Intent intent = new Intent(LobbyActivity.this, StartGameActivity.class);
                    startActivity(intent);
                } catch (JsonProcessingException e) {
                    Log.e(TAG, "Error processing incoming LobbyDTO!", e.getCause());
                }
            }

            /**
             *Error handling yet missing. Will be implemented in the next sprint.
             */
            @Override
            public void handleError() {}
        };

        Disposable topicSubscription = MainActivity.getNetworkHandler().subscribe("/topic/lobbies/" + MainActivity.uuid, lobbyResponseHandler);

        try {
            Disposable sendSubscription = MainActivity.getNetworkHandler().send("/app/lobby/create", player);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Error converting PlayerDTO into json string!", e.getCause());
        }
    }

    public static LobbyDTO getLobbyDTO() {
        return lobbyDTO;
    }
}