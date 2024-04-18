package se2.group3.gameoflife.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.game.MainViewModel;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

/**
 * This class contains the MainActivity. This is the first screen the player sees after opening the app. This activity is used to welcome the player and the option to choose a user name.
 */

public class MainActivity extends AppCompatActivity {

    private static WebsocketClient networkHandler;
    public static String uuid = UUID.randomUUID().toString();
    TextView textUser;
    static String username = null;
    String playerJSON = null;
    ObjectMapper objectMapper;
    PlayerDTO player;


    /**
     * Function of the button is defined.
     * The player is prompted to choose a user name. If the username is correct, the player is directed to the next screen. If the username is incorrect, the player is asked to choose a new one.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect to the server
        networkHandler = new WebsocketClient("ws://10.0.2.2:8080/gameoflife");
        networkHandler.connect();

        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);

        Button check = findViewById(R.id.buttonCheck);
        check.setOnClickListener(v -> {
            TextView user = findViewById(R.id.enterUsername);
            username = user.getText().toString();
            textUser = findViewById(R.id.textUsername);
            if (model.checkUsername(username)){
                goToNextActivity();
            } else{
                textUser.setText(getString(R.string.usernameHint));
            }
        });
    }

    /**
     * Is used to change the activity
     */
    public void goToNextActivity(){
        Intent intent = new Intent(this, LobbyActivity.class);
        startActivity(intent);
    }

    public static WebsocketClient getNetworkHandler() {
        return networkHandler;
    }

    public static String getUsername() {
        return username;
    }
}
