package se2.group3.gameoflife.frontend.activities;

import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.fragments.ChoosePathFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class GameActivity extends AppCompatActivity {

    public interface VibrateCallback {
        void onCallback(String message);
    }

    private GameViewModel gameViewModel;

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

        ObjectMapper objectMapper = new ObjectMapper();
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        gameViewModel.getLobby().observe(GameActivity.this, this::updateLobby);

        try {
            gameViewModel.setLobbyDTO(objectMapper.readValue(getIntent().getStringExtra("lobbyDTO"), LobbyDTO.class));
        } catch (JsonProcessingException e) {
            Log.d(TAG, "Error getting lobbyDTO: " + e.getMessage());
        }

        gameViewModel.startGame(vibrationEvent -> vibrate());

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

    private void updateLobby(LobbyDTO lobbyDTO){
        gameViewModel.setLobbyDTO(lobbyDTO);
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

}