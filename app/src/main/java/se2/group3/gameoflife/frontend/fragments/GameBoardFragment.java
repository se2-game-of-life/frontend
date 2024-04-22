package se2.group3.gameoflife.frontend.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import se2.group3.gameoflife.frontend.util.SerializationUtil;


import io.reactivex.disposables.Disposable;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.activities.MainActivity;
import se2.group3.gameoflife.frontend.dto.BoardDTO;
import se2.group3.gameoflife.frontend.dto.CellDTO;
import se2.group3.gameoflife.frontend.networking.ResponseHandler;

public class GameBoardFragment extends Fragment {

    private static final String TAG = "Networking";

    public GameBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game_board, container, false);

        // Call fetchBoardData when the fragment is created
        fetchBoardData();

        return rootView;
    }

    private void fetchBoardData() {
        // Log.d(TAG, "fetchBoardData started");

        ResponseHandler boardResponseHandler = new ResponseHandler() {
            @Override
            public void handleMessage(String msg) {
                // Log.d(TAG, "Received a msg: " + msg);

                BoardDTO boardDTO = null;
                try {
                    boardDTO = (BoardDTO) SerializationUtil.toObject(msg, BoardDTO.class);

                    // Log the received BoardDTO
                    Log.d(TAG, "Board data received: " + boardDTO);
                } catch (Exception e) {
                    Log.e(TAG, "Error processing incoming BoardDTO!", e);
                }

                try{
                    // Update UI with board data
                    assert boardDTO != null;
                    updateUI(boardDTO);
                }catch (Exception e) {
                    Log.e(TAG, "Error rendering BoardDTO updateUI!", e);
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
        } catch (Exception e) {
            Log.e(TAG, "Error sending fetch board request!", e);
        }
    }


    private void updateUI(BoardDTO boardDTO) {
        Log.d(TAG, "updateUI started");

        // Find the GridLayout in the layout
        GridLayout gridLayout = requireView().findViewById(R.id.gridLayout);

        // Clear existing views from GridLayout
        gridLayout.removeAllViews();

        // Check if BoardDTO is not null
        if (boardDTO != null && boardDTO.getCells() != null) {
            Log.d(TAG, "Populate GridLayout with cells from BoardDTO");

            // Populate GridLayout with cells from BoardDTO
            for (int row = 0; row < boardDTO.getCells().size(); row++) {
                for (int col = 0; col < boardDTO.getCells().get(row).size(); col++) {
                    // Get the cellDTO from the BoardDTO
                    CellDTO cellDTO = boardDTO.getCells().get(row).get(col);

                    // Create a new TextView for each cell
                    TextView cell = new TextView(getContext());
                    cell.setLayoutParams(new GridLayout.LayoutParams(
                            GridLayout.spec(row, 1f), // Row weight
                            GridLayout.spec(col, 1f) // Column weight
                    ));

                    // Set text and appearance of cells based on cellDTO presence
                    if (cellDTO != null) {
                        if(Objects.equals(cellDTO.getType(), "actionCell")) {
                            cell.setBackgroundResource(R.drawable.cell_action_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "addpegCell")) {
                            cell.setBackgroundResource(R.drawable.cell_addpeg_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "investCell")) {
                            cell.setBackgroundResource(R.drawable.cell_invest_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "houseCell")) {
                            cell.setBackgroundResource(R.drawable.cell_house_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "paydayCell")) {
                            cell.setBackgroundResource(R.drawable.cell_payday_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "careerCell")) {
                            cell.setBackgroundResource(R.drawable.cell_career_background);
                        }
                        else if(cellDTO.getType().endsWith("StopCell")) {
                            cell.setBackgroundResource(R.drawable.cell_stop_background);
                        }
                        else if(cellDTO.getType().endsWith("start")) {
                            cell.setBackgroundResource(R.drawable.cell_start_background);
                        }
                    }

                    // Add cell to the GridLayout
                    gridLayout.addView(cell);
                }
            }
        } else {
            // Log a message or handle the situation when BoardDTO or its cells are null
            Log.e(TAG, "BoardDTO or cells are null");
        }

    }

}
