package se2.group3.gameoflife.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public class MainActivity extends Activity {
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
                String usernameRegex = "^[a-zA-Z0-9]+$";
                if (Pattern.matches(usernameRegex, username)){
                    goToNextActivity();
                } else{
                    TextView textUser = findViewById(R.id.textUsername);
                    textUser.setText("Please choose a username consisting only of letters and digits.");
                }
            }
        });
    }

    public void goToNextActivity(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }


}
