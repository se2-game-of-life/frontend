package se2.group3.gameoflife.frontend.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import se2.group3.gameoflife.frontend.R;


public class GameBoardFragment extends Fragment {

    public GameBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game_board, container, false);

        // Find the GridLayout in the layout
        GridLayout gridLayout = rootView.findViewById(R.id.gridLayout);

        // Create cells for the grid
        int numRows = gridLayout.getRowCount();
        int numColumns = gridLayout.getColumnCount();
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                // Create a new TextView for each cell
                TextView cell = new TextView(getContext());
                cell.setLayoutParams(new GridLayout.LayoutParams(
                        GridLayout.spec(row, 1f), // Row weight
                        GridLayout.spec(column, 1f) // Column weight
                ));

                // Set text and appearance of filled cells
                if ((row + column) % 2 == 0) {
                    cell.setText("X"); // Filled cell
                    cell.setBackgroundColor(Color.YELLOW); // Green background
                } else {
                    cell.setText(""); // Empty cell
                }

                // Add cell to the GridLayout
                gridLayout.addView(cell);
            }
        }

        return rootView;
    }



}