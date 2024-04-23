package se2.group3.gameoflife.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.viewmodels.MainViewModel;

/**
 * This class contains the MainActivity. This is the first screen the player sees after opening the app. This activity is used to welcome the player and the option to choose a user name.
 */

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Networking";

    private MainViewModel mainViewModel;
    private TextView textUser;
    private static String username = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.connectToServer();

        Button check = findViewById(R.id.buttonCheck);
        check.setOnClickListener(v -> {
            TextView user = findViewById(R.id.enterUsername);
            username = user.getText().toString();
            textUser = findViewById(R.id.textUsername);
            if (mainViewModel.checkUsername(username)){
                Intent intent = new Intent(this, LobbyActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("uuid", mainViewModel.getUUID());
                startActivity(intent);
            } else{
                textUser.setText(getString(R.string.usernameHint));
            }
        });
    }

    @Override
    protected void onDestroy() {
        mainViewModel.dispose();
        super.onDestroy();
    }
}
