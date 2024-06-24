package se.group3.gameoflife.frontend.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;
import se.group3.gameoflife.frontend.dto.LobbyDTO;
import se.group3.gameoflife.frontend.dto.PlayerDTO;
import se.group3.gameoflife.frontend.networking.ConnectionService;


public class LobbyActivity extends AppCompatActivity {

    private static final String TAG = "Networking";
    private ConnectionService connectionService;
    private CompositeDisposable compositeDisposable;
    private boolean isBound = false;
    private final MutableLiveData<Boolean> serviceBound = new MutableLiveData<>();

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ConnectionService.ConnectionServiceBinder binder = (ConnectionService.ConnectionServiceBinder) service;
            connectionService = binder.getService();
            isBound = true;
            serviceBound.setValue(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            serviceBound.setValue(false);
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
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //deactivate back button
            }
        });

        findViewById(R.id.buttonReturnToLobby).setOnClickListener(v -> {
            compositeDisposable.add(connectionService.unsubscribe("/topic/lobbies/" + connectionService.getUuidLiveData().getValue())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());

            compositeDisposable.add(connectionService.send("/app/lobby/leave", "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Intent intent = new Intent(LobbyActivity.this, MainActivity.class);
                        startActivity(intent);
                    }, error -> Log.e(TAG, "Error Sending Leave Lobby: " + error)));
        });

        findViewById(R.id.StartButton).setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/start", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
                    startActivity(intent);
                }, error -> Log.e(TAG, "Error Sending Start Lobby: " + error))));

        serviceBound.observe(this, isConnectionServiceBound -> {
            if (Boolean.TRUE.equals(isConnectionServiceBound)) {
                connectionService.getLiveData(LobbyDTO.class).observe(this, lobby -> {
                    compositeDisposable.add(connectionService.subscribe("/topic/lobbies/" + lobby.getLobbyID(), LobbyDTO.class)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());
                    updateLobby(lobby);
                });
            }
        });

    }

    private void updateLobby(LobbyDTO lobbyDTO) {
        if(lobbyDTO.isHasStarted()) {
            Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
            startActivity(intent);
        }
        Button[] playerButtons = new Button[4];
        playerButtons[0] = findViewById(R.id.player1Button);
        playerButtons[1] = findViewById(R.id.player2Button);
        playerButtons[2] = findViewById(R.id.player3Button);
        playerButtons[3] = findViewById(R.id.player4Button);
        playerButtons[0].setVisibility(View.INVISIBLE);
        playerButtons[1].setVisibility(View.INVISIBLE);
        playerButtons[2].setVisibility(View.INVISIBLE);
        playerButtons[3].setVisibility(View.INVISIBLE);

        TextView lobbyID = findViewById(R.id.lobbyID);
        lobbyID.setText(String.format("ID: %s", lobbyDTO.getLobbyID()));
        List<PlayerDTO> players = lobbyDTO.getPlayers();
        for (int i = 0; i < players.size(); i++){
            playerButtons[i].setText(players.get(i).getPlayerName());
            playerButtons[i].setVisibility(View.VISIBLE);
        }
    }
}