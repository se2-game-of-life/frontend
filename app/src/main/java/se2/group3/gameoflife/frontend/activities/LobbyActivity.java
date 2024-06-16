package se2.group3.gameoflife.frontend.activities;

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
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.ConnectionService;


public class LobbyActivity extends AppCompatActivity {

    private final String TAG = "Networking";
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

        findViewById(R.id.buttonReturnToLobby).setOnClickListener(v -> {
            compositeDisposable.add(connectionService.unsubscribe("/topic/lobbies/" + connectionService.getUuidLiveData().getValue())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());

            compositeDisposable.add(connectionService.send("/app/lobby/leave", "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Intent intent = new Intent(LobbyActivity.this, MenuActivity.class);
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

        serviceBound.observe(this, isBound -> {
            if (isBound) {
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