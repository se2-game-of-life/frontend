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
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;


import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.fragments.ChoosePathFragment;
import se2.group3.gameoflife.frontend.networking.ConnectionService;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class GameActivity extends AppCompatActivity {

    public interface VibrateCallback {
        void onCallback();
    }

    private GameViewModel gameViewModel;
    private final String TAG = "Networking";

    private static final int MIN_INTERVAL = 1000;
    private static final int MAX_INTERVAL = 5000;
    private final Random random = new Random();
    private final Handler handler = new Handler();
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

    private final Runnable vibrateTask = new Runnable() {
        @Override
        public void run() {
            vibrate();
            int nextInterval = random.nextInt(MAX_INTERVAL - MIN_INTERVAL + 1) + MIN_INTERVAL;
            handler.postDelayed(this, nextInterval);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        compositeDisposable = new CompositeDisposable();

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);


        startGame(this::vibrate);
        vibrateTask.run();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new ChoosePathFragment())
                .commit();
    }
  
    public void setFragmentVisibility(){
        View fragment = findViewById(R.id.fragmentContainerView2);
        if(fragment != null){
            fragment.setVisibility(View.VISIBLE);
        }
    }


    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(vibrateTask);
        gameViewModel.dispose();
        super.onDestroy();
    }

    private void startGame(VibrateCallback callback){
        LobbyDTO lobby = connectionService.getLiveData(LobbyDTO.class).getValue();
        if(lobby == null) throw new RuntimeException("LobbyDTO NULL in GameViewModel!");
        compositeDisposable.add(connectionService.subscribe("/topic/lobbies/" + lobby.getLobbyID(), LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());


        //subscribe to vibrate-events for the lobby
        compositeDisposable.add(connectionService.subscribe("/topic/lobbies/" + lobby.getLobbyID() + "/vibrate", LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());

        //return if the lobby has already started
        if(lobby.isHasStarted()) return;

        //start lobby if lobby not started already
        compositeDisposable.add(connectionService.send("/app/lobby/start", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

}