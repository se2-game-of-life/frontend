package se2.group3.gameoflife.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import se2.group3.gameoflife.frontend.R;

public class ChoosePathActivity extends AppCompatActivity {
    private boolean college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_path);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.btnCollege).setOnClickListener(v -> {
            college = true;
            choosePath();
        });
        findViewById(R.id.btnCareer).setOnClickListener(v -> {
            college = false;
            choosePath();
        });
    }

    private void choosePath(){
        //todo: communicate decision to server
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
    }
}