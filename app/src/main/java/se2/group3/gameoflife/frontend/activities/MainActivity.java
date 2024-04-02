package se2.group3.gameoflife.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

import se2.group3.gameoflife.frontend.R;

/**
 * This class contains the MainActivity. This is the first screen the player sees after opening the app. This activity is used to welcome the player and the option to choose a user name.
 */

public class MainActivity extends Activity {

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
                String username = user.getText().toString();
                TextView textUser = findViewById(R.id.textUsername);
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


}
