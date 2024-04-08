package se2.group3.gameoflife.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.regex.Pattern;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

/**
 * This class contains the MainActivity. This is the first screen the player sees after opening the app. This activity is used to welcome the player and the option to choose a user name.
 */

public class MainActivity extends Activity {

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

        Button check = findViewById(R.id.buttonCheck);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView user = findViewById(R.id.enterUsername);
                username = user.getText().toString();
                textUser = findViewById(R.id.textUsername);
                if (checkUsername(username)){
                    goToNextActivity();
                } else{
                    textUser.setText("Please choose a username consisting only of letters and, if you like, digits at the end.");
                }
            }
        });
    }

    /**
     * method to check if username is valid
     * The username must consist of letters and can contain 0 or more digits at the end.
     */
    public boolean checkUsername(String username){
        String usernameRegex = "^[a-zA-Z]+[0-9]*$";
        return Pattern.matches(usernameRegex, username);
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
