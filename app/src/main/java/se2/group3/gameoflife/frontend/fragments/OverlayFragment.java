package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se2.group3.gameoflife.frontend.R;

public class OverlayFragment extends Fragment {

    //todo: get lobbyDTO from LiveData
    //todo: change visibility of buttons depending on how many people are in the lobby
    //todo: make buttons toggle between player name and player stats
    //todo: implement long press for report
    //todo: add button for spinning
    //todo: add button for cheating / fake cheating (fake cheating also needs to be added in the backend)

    public OverlayFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overlay, container, false);
    }
}