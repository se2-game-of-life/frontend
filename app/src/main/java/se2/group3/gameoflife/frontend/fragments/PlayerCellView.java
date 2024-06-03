package se2.group3.gameoflife.frontend.fragments;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import se2.group3.gameoflife.frontend.R;


public class PlayerCellView extends LinearLayout {
    private TextView player1View;
    private TextView player2View;
    private TextView player3View;
    private TextView player4View;

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

        // Find and initialize the TextViews for each player symbol
        player1View = findViewById(R.id.player1_text);
        player2View = findViewById(R.id.player2_text);
        player3View = findViewById(R.id.player3_text);
        player4View = findViewById(R.id.player4_text);
    }

    // Methods to set player symbols for each section
    public void setPlayer1Text(String text) {
        player1View.setText(text);
    }

    public void setPlayer2Text(String text) {
        player2View.setText(text);
    }

    public void setPlayer3Text(String text) {
        player3View.setText(text);
    }

    public void setPlayer4Text(String text) {
        player4View.setText(text);
    }
}
