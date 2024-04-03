package se2.group3.gameoflife.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.regex.Pattern;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.WebSocketClient;

/**
 * This class contains the MainActivity. This is the first screen the player sees after opening the app. This activity is used to welcome the player and the option to choose a user name.
 */

public class MainActivity extends Activity {
    WebSocketClient networkHandler;
    TextView textUser;
    String username = null;
    String player_JSON = null;

    /**
     * Function of the button is defined.
     * The player is prompted to choose a user name. If the username is correct, the player is directed to the next screen. If the username is incorrect, the player is asked to choose a new one.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    /**
     * Code from Demo App to establish communication
     */
    private void connectToWebSocketServer() {
        // register a handler for received messages when setting up the connection
        networkHandler.connectToServer(this::messageReceivedFromServer);
    }

    private void sendMessage() {
        networkHandler.sendMessageToServer(player_JSON);
    }


    private void messageReceivedFromServer(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PlayerDTO player = objectMapper.readValue(message, PlayerDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Log.d("Network", message);
        //textUser.setText(message);
    }

    /**
     * This method is used to convert a player object into a JSON string that
     * can later be communicated to the server.
     */
    private void createJSON(){
        ObjectMapper objectMapper = new ObjectMapper();
        PlayerDTO player = new PlayerDTO(username);
        try{
            player_JSON = objectMapper.writeValueAsString(player);
        } catch (StreamWriteException sw) {
            sw.printStackTrace();
        } catch (DatabindException db) {
            db.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


}
