package se2.group3.gameoflife.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobbyselector);

        WebsocketClient socketClient = new WebsocketClient();

        Button connectButton = findViewById(R.id.connect_button);
        Button sendButton = findViewById(R.id.send_hello);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient.connect();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient.send();
            }
        });
    }
}
