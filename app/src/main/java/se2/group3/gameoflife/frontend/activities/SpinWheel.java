package se2.group3.gameoflife.frontend.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.reactivex.disposables.Disposable;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class SpinWheel extends AppCompatActivity {
    private WebsocketClient networkHandler;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spin_wheel);
        networkHandler = WebsocketClient.getInstance("ws://10.0.2.2:8080/gameoflife");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button spinButton = findViewById(R.id.spinButton);

        spinButton.setOnClickListener(v -> {
            // Send a message to the backend to spin the wheel
            sendButtonClickInfo("Spin Button Clicked");
        });
    }
    private void sendButtonClickInfo(String info) {
        if (networkHandler != null) {
            disposable = networkHandler.send("/app/spinWheel", info)
                    .subscribe(() -> {
                        // Message sent successfully
                        runOnUiThread(() -> Toast.makeText(this, "Spin wheel info sent to backend", Toast.LENGTH_SHORT).show());
                    }, throwable -> {
                        // Error occurred while sending message
                        runOnUiThread(() -> Toast.makeText(this, "Failed to send spin wheel info: " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
                    });
        } else {
            Toast.makeText(this, "WebSocket client not initialized", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dispose the disposable when the activity is destroyed
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}