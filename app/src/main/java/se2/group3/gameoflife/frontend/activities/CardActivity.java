package se2.group3.gameoflife.frontend.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class CardActivity extends AppCompatActivity {
    private WebsocketClient networkHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card);
        networkHandler = WebsocketClient.getInstance("ws://10.0.2.2:8080/gameoflife");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonOne = findViewById(R.id.buttonOne);
        Button buttonTwo = findViewById(R.id.buttonTwo);

        buttonOne.setOnClickListener(v -> sendButtonClickInfo("Button One Clicked"));

        buttonTwo.setOnClickListener(v -> sendButtonClickInfo("Button Two Clicked"));
    }

    private void sendButtonClickInfo(String info) {
        if (networkHandler != null) {
            networkHandler.send("/app/buttonClicked", info)
                    .subscribe(() -> {
                        // Message sent successfully
                        runOnUiThread(() -> Toast.makeText(this, "Button click sent to backend", Toast.LENGTH_SHORT).show());
                    }, throwable -> {
                        // Error occurred while sending message
                        runOnUiThread(() -> Toast.makeText(this, "Failed to send button click: " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
                    });
        } else {
            Toast.makeText(this, "WebSocket client not initialized", Toast.LENGTH_SHORT).show();
        }
    }
}

