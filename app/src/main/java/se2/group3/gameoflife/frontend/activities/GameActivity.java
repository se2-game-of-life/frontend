package se2.group3.gameoflife.frontend.activities;

import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.fragments.ChoosePathFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class GameActivity extends AppCompatActivity {

    private GameViewModel gameViewModel;

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

        try {
            gameViewModel.setLobbyDTO(objectMapper.readValue(getIntent().getStringExtra("lobbyDTO"), LobbyDTO.class));
        } catch (JsonProcessingException e) {
            Log.d(TAG, "Error getting lobbyDTO: " + e.getMessage());
        }

        gameViewModel.startGame(); //start the game
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
}