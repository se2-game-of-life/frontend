package se2.group3.gameoflife.frontend.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import se2.group3.gameoflife.frontend.R;

public class PlayerCellView extends LinearLayout {
    private ImageView player1Dot;
    private ImageView player2Dot;
    private ImageView player3Dot;
    private ImageView player4Dot;

    public PlayerCellView(Context context) {
        super(context);
        initializeViews(context);
    }

    public PlayerCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        // Inflate the layout for the cell with four sections
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.player_cell_view, this);

        // Find and initialize the ImageViews for each player dot
        player1Dot = findViewById(R.id.player1_dot);
        player2Dot = findViewById(R.id.player2_dot);
        player3Dot = findViewById(R.id.player3_dot);
        player4Dot = findViewById(R.id.player4_dot);
    }

    // Methods to set player dots for each section
    public void setPlayer1Dot(int drawableResId) {
        player1Dot.setBackgroundResource(drawableResId);
    }

    public void setPlayer2Dot(int drawableResId) {
        player2Dot.setBackgroundResource(drawableResId);
    }

    public void setPlayer3Dot(int drawableResId) {
        player3Dot.setBackgroundResource(drawableResId);
    }

    public void setPlayer4Dot(int drawableResId) {
        player4Dot.setBackgroundResource(drawableResId);
    }


    public void clearPlayer1Dot() {
        player1Dot.setBackgroundResource(android.R.color.transparent); // Set transparent background to clear the dot
    }

    public void clearPlayer2Dot() {
        player2Dot.setBackgroundResource(android.R.color.transparent); // Set transparent background to clear the dot
    }

    public void clearPlayer3Dot() {
        player3Dot.setBackgroundResource(android.R.color.transparent); // Set transparent background to clear the dot
    }

    public void clearPlayer4Dot() {
        player4Dot.setBackgroundResource(android.R.color.transparent); // Set transparent background to clear the dot
    }
}
