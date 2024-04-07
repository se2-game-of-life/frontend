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

import io.reactivex.disposables.Disposable;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.ResponseHandler;
import se2.group3.gameoflife.frontend.util.SerializationUtil;

public class LobbyActivity extends AppCompatActivity {

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

        Button b = findViewById(R.id.createButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLobby();
            }
        });
    }

    private void createLobby() {
        ResponseHandler lobbyResponseHandler = new ResponseHandler() {
            @Override
            public void handleMessage(String msg) {
                LobbyDTO lobbyDTO = (LobbyDTO) SerializationUtil.toObject(msg, LobbyDTO.class);
                Log.d("Lobby", "Lobby: " + lobbyDTO);
            }

            @Override
            public void handleError() {}
        };



        Disposable topicSubscription = MainActivity.getNetworkHandler().subscribe("/topic/lobbies", lobbyResponseHandler);

        Disposable sendSubscription = MainActivity.getNetworkHandler().send("/app/lobby/create", new PlayerDTO("Test"));

    }
}