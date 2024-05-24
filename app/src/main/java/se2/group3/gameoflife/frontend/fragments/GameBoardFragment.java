package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.viewmodels.GameBoardViewModel;

public class GameBoardFragment extends Fragment {

    private GameBoardViewModel gameBoardViewModel;

    public GameBoardFragment() {
        // Required empty public constructor
    }

    public static GameBoardFragment newInstance() {
        return new GameBoardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_board, container, false);
        gameBoardViewModel = new ViewModelProvider(this).get(GameBoardViewModel.class);

        Button btnSpin = rootView.findViewById(R.id.buttonSpin);
        btnSpin.setOnClickListener(v -> gameBoardViewModel.spinWheel());

        // Add more buttons and their click listeners as needed

        return rootView;
    }

    @Override
    public void onDestroy() {
        gameBoardViewModel.dispose();
        super.onDestroy();
    }
}
