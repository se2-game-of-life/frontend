package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class OverlayFragment extends Fragment {

    private GameViewModel gameViewModel;

    //todo: get lobbyDTO from LiveData
    //todo: change visibility of buttons depending on how many people are in the lobby
    //todo: make buttons toggle between player name and player stats

    public OverlayFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overlay, container, false);

        Button player1Button = rootView.findViewById(R.id.player1Button);
        Button player2Button = rootView.findViewById(R.id.player2Button);
        Button player3Button = rootView.findViewById(R.id.player3Button);
        Button player4Button = rootView.findViewById(R.id.player4Button);
        Button spinButton = rootView.findViewById(R.id.spinButton);
        Button cheatButton = rootView.findViewById(R.id.cheatButton);

        player1Button.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        player1Button.setOnLongClickListener(v -> {
            gameViewModel.report(0);
            return true;
        });
        player2Button.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        player2Button.setOnLongClickListener(v -> {
            gameViewModel.report(1);
            return true;
        });
        player3Button.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        player3Button.setOnLongClickListener(v -> {
            gameViewModel.report(2);
            return true;
        });
        player4Button.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        player4Button.setOnLongClickListener(v -> {
            gameViewModel.report(3);
            return true;
        });
        cheatButton.setOnClickListener(view -> {
            //todo: handle short click player1button
        });
        cheatButton.setOnLongClickListener(v -> {
            //todo: handle long click player1button
            return true;
        });
        spinButton.setOnClickListener(view -> {
            //todo: handle short click player1button
        });

        return rootView;
    }
}