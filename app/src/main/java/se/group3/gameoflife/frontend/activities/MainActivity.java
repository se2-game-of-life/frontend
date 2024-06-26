package se.group3.gameoflife.frontend.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import se.group3.gameoflife.frontend.viewmodels.MainViewModel;
import se2.group3.gameoflife.frontend.R;

public class MainActivity extends AppCompatActivity {
    private TextView textUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //deactivate back button
            }
        });

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Button check = findViewById(R.id.buttonCheck);
        check.setOnClickListener(v -> {
            TextView user = findViewById(R.id.enterUsername);
            mainViewModel.setUsername(user.getText().toString());
            textUser = findViewById(R.id.textUsername);
            if (mainViewModel.checkUsername()){
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra("username", mainViewModel.getUsername());
                startActivity(intent);
            } else{
                textUser.setText(getString(R.string.usernameHint));
            }
        });

    }
}
