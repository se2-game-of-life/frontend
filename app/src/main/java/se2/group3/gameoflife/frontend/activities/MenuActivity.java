package se2.group3.gameoflife.frontend.activities;

import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.viewmodels.MenuViewModel;

public class MenuActivity extends AppCompatActivity {

    private MenuViewModel menuViewModel;
    private ObjectMapper objectMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        objectMapper = new ObjectMapper();
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
            menuViewModel.getLobby().observe(MenuActivity.this, lobbyDTO -> {
                Intent intent = new Intent(MenuActivity.this, StartGameActivity.class);
                try {
                    intent.putExtra("lobbyDTO", objectMapper.writeValueAsString(lobbyDTO));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);
            });
            Log.d(TAG, "Before create lobby!");
            assert playerName != null;
            menuViewModel.createLobby(playerName, getIntent().getStringExtra("uuid"));
            Log.d(TAG, "After create lobby!");
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
                    menuViewModel.getLobby().observe(MenuActivity.this, lobbyDTO -> {
                        Intent intent = new Intent(MenuActivity.this, StartGameActivity.class);
                        try {
                            intent.putExtra("lobbyDTO", objectMapper.writeValueAsString(lobbyDTO));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        startActivity(intent);
                    });
                    assert playerName != null;
                    menuViewModel.joinLobby(lobbyID, playerName);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        menuViewModel.dispose();
        super.onDestroy();
    }
}