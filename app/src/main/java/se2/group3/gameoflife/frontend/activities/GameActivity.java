package se2.group3.gameoflife.frontend.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;


import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.fragments.ChoosePathFragment;
import se2.group3.gameoflife.frontend.networking.ConnectionServiceCallback;
import se2.group3.gameoflife.frontend.networking.VibrationCallback;
import se2.group3.gameoflife.frontend.networking.ConnectionService;

public class GameActivity extends AppCompatActivity {

    private static final int MIN_INTERVAL = 1000;
    private static final int MAX_INTERVAL = 5000;
    private final Random random = new Random();
    private final Handler handler = new Handler();

    private final Runnable vibrateTask = new Runnable() {
        @Override
        public void run() {
            vibrate();
            int nextInterval = random.nextInt(MAX_INTERVAL - MIN_INTERVAL + 1) + MIN_INTERVAL;
            handler.postDelayed(this, nextInterval);
        }
    };

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
        Intent intent = new Intent(this, ConnectionService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
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
        handler.removeCallbacks(vibrateTask);
        compositeDisposable.dispose();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        serviceBound.observe(this, isBound -> {
            if (isBound) {
                connectionService.getLiveData(LobbyDTO.class).observe(this, v -> startVibrationFeature(this::vibrate));
                Log.d(TAG, "Attempting to load Decision fragment!");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new ChoosePathFragment())
                        .commit();
            }
        });
    }
  
    public void setFragmentVisibility(){
        View fragment = findViewById(R.id.fragmentContainerView2);
        if(fragment != null){
            fragment.setVisibility(View.VISIBLE);
        }
    }

    private void startVibrationFeature(VibrationCallback callback){
        vibrateTask.run();
        LobbyDTO lobby = connectionService.getLiveData(LobbyDTO.class).getValue();
        assert lobby != null;
        compositeDisposable.add(connectionService.subscribeEvent("/topic/lobbies/" + lobby.getLobbyID() + "/vibrate", callback));
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    public void getConnectionService(ConnectionServiceCallback callback) {
        serviceBound.observe(this, isBound -> {
            if (isBound) {
                connectionService.getLiveData(LobbyDTO.class).observe(this, v -> callback.onCallback(connectionService));
            }
        });
    }
}