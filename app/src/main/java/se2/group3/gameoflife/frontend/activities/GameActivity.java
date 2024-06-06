package se2.group3.gameoflife.frontend.activities;

import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.fragments.ChoosePathFragment;


public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectMapper objectMapper = new ObjectMapper();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LobbyDTO lobby;
        Fragment fragment = new ChoosePathFragment();

        try {
            lobby = objectMapper.readValue(getIntent().getStringExtra("lobbyDTO"), LobbyDTO.class);
            String lobbyDTOJson = objectMapper.writeValueAsString(lobby);

            Bundle bundle = new Bundle();
            bundle.putString("lobbyDTO", lobbyDTOJson);
            fragment.setArguments(bundle);
        } catch (JsonProcessingException e) {
            Log.d(TAG, "Error getting lobbyDTO: " + e.getMessage());
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

}