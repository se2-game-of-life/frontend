package se2.group3.gameoflife.frontend.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.reactivex.disposables.Disposable;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.BoardDTO;
import se2.group3.gameoflife.frontend.networking.ResponseHandler;
import se2.group3.gameoflife.frontend.util.SerializationUtil;

public class BoardActivity extends AppCompatActivity {

    private static final String TAG = "Networking";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Call fetchBoardData when the activity is created
        fetchBoardData();

    }


    private void fetchBoardData() {
        Log.d(TAG, "fetchBoardData started" );

        ResponseHandler boardResponseHandler = new ResponseHandler() {
            @Override
            public void handleMessage(String msg) {
                try {
                    // Parse the JSON string into a BoardDTO object
                    Gson gson = new Gson();
                    BoardDTO boardDTO = gson.fromJson(msg, BoardDTO.class);

                    // Log the received BoardDTO
                    Log.d(TAG, "Board data received: " + boardDTO);

                    // Process the board data as needed (e.g., update UI)
                } catch (Exception e) {
                    Log.e(TAG, "Error processing incoming BoardDTO!", e.getCause());
                }
            }

            @Override
            public void handleError() {
                // Handle error if needed
            }
        };

        // Subscribe to the board topic to receive board data
        Disposable topicSubscription = MainActivity.getNetworkHandler().subscribe("/topic/board/" + MainActivity.uuid, boardResponseHandler);

        // Send a request to fetch the board data
        try {
            MainActivity.getNetworkHandler().send("/app/board/fetch", null);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Error sending fetch board request!", e.getCause());
        }

    }

}