package se2.group3.gameoflife.frontend.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class SpinWheel extends AppCompatActivity {
    private WebsocketClient networkHandler;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spin_wheel);
        initializeNetworkHandler();
        applyWindowInsets(findViewById(R.id.main));
        initializeButton(R.id.spinButton, "Spin Button Clicked");
    }

    private void initializeNetworkHandler() {
        networkHandler = WebsocketClient.getInstance("ws://10.0.2.2:8080/gameoflife");
    }

    private void applyWindowInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeButton(int buttonId, String info) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> sendButtonClickInfo(info));
    }

    private void sendButtonClickInfo(String info) {
        if (networkHandler != null) {
            Disposable disposable = networkHandler.send("/app/spinWheel", info)
                    .subscribe(() -> showToast("Spin wheel info sent to backend"),
                            throwable -> showToast("Failed to send spin wheel info: " + throwable.getMessage()));
            compositeDisposable.add(disposable);
        } else {
            showToast("WebSocket client not initialized");
        }
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}

