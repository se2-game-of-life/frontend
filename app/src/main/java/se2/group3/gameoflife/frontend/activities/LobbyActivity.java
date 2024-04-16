package se2.group3.gameoflife.frontend.activities;

import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.LobbyViewModel;

public class LobbyActivity extends AppCompatActivity {

    private LobbyViewModel lobbyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lobbyViewModel = new ViewModelProvider(this).get(LobbyViewModel.class);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonReturnToUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.buttonCreateNewGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lobbyViewModel.getLobby().observe(LobbyActivity.this, lobbyDTO -> {
                    Log.d(TAG, "UPDATED LOBBY DTO LIVE DATA OBJECT");
                    Intent intent = new Intent(LobbyActivity.this, StartGameActivity.class);
                    intent.putExtra("lobbyDTO", lobbyDTO);
                    startActivity(intent);
                });
                Log.d(TAG, "Before create lobby!");
                lobbyViewModel.createLobby(new PlayerDTO(MainActivity.getUsername()));
                Log.d(TAG, "After create lobby!");
            }
        });

        findViewById(R.id.buttonJoinGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_join_game);
                findViewById(R.id.GObutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          EditText lobbyIDText = findViewById(R.id.lobbyCodeEntry);
                          String lobbyIDString = lobbyIDText.getText().toString();
                          if (!lobbyIDString.isEmpty()){
                            long lobbyID = Long.parseLong(lobbyIDString);
                            lobbyViewModel.getLobby().observe(LobbyActivity.this, lobbyDTO -> {
                                Intent intent = new Intent(LobbyActivity.this, StartGameActivity.class);
                                intent.putExtra("lobbyDTO", lobbyDTO);
                                startActivity(intent);
                            });
                            lobbyViewModel.joinLobby(lobbyID, new PlayerDTO(MainActivity.getUsername()));
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        lobbyViewModel.dispose();
        super.onDestroy();
    }
}