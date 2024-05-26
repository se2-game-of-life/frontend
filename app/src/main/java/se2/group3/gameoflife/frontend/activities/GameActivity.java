package se2.group3.gameoflife.frontend.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.fragments.ChoosePathFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;
import se2.group3.gameoflife.frontend.viewmodels.StartGameViewModel;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameViewModel gameViewModel;
        super.onCreate(savedInstanceState);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        ObjectMapper objectMapper = new ObjectMapper();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        try {
            LobbyDTO lobby = objectMapper.readValue(getIntent().getStringExtra("lobbyDTO"), LobbyDTO.class);
            gameViewModel.setLobbyDTO(lobby);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ChoosePathFragment fragment = new ChoosePathFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

}