package se2.group3.gameoflife.frontend.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.JoinLobbyRequest;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.networking.ConnectionService;

public class MenuActivity extends AppCompatActivity {

    private final String TAG = "Networking";
    ConnectionService connectionService;
    CompositeDisposable compositeDisposable;
    boolean isBound = false;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ConnectionService.ConnectionServiceBinder binder = (ConnectionService.ConnectionServiceBinder) service;
            connectionService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ConnectionService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
        if(isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonReturnToUser).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.buttonCreateNewGame).setOnClickListener(v -> {
            String playerName = getIntent().getStringExtra("username");
            String UUID = connectionService.getUuidLiveData().getValue();
            compositeDisposable.add(connectionService.subscribe("/topic/lobbies/" + UUID, LobbyDTO.class)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());

            compositeDisposable.add(connectionService.send("/app/lobby/create", playerName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Intent intent = new Intent(MenuActivity.this, LobbyActivity.class);
                        startActivity(intent);
                    }, error -> Log.e(TAG, "Error Sending Create Lobby: " + error)));
        });

        findViewById(R.id.buttonJoinGame).setOnClickListener(v -> {
            setContentView(R.layout.activity_join_game);
            findViewById(R.id.GObutton).setOnClickListener(v1 -> {
                String playerName = getIntent().getStringExtra("username");
                if(playerName == null) Log.e(TAG, "Error with the username intent!");

                EditText lobbyIDText = findViewById(R.id.lobbyCodeEntry);
                String lobbyIDString = lobbyIDText.getText().toString();

                if (!lobbyIDString.isEmpty()){
                    long lobbyID = Long.parseLong(lobbyIDString);
                    connectionService.subscribe("/topic/lobbies/" + lobbyID, LobbyDTO.class);

                    compositeDisposable.add(connectionService.send("/app/lobby/join", new JoinLobbyRequest(lobbyID, playerName)).
                            subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                Intent intent = new Intent(MenuActivity.this, LobbyActivity.class);
                                startActivity(intent);
                            }, error -> Log.e(TAG, "Error Sending Create Lobby: " + error)));
                }
            });
        });
    }
}