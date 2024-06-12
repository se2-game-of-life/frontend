package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WinScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WinScreenFragment extends Fragment {
    private GameViewModel gameViewModel;
    private View rootView;

    public WinScreenFragment() {
        // Required empty public constructor
    }


    public static WinScreenFragment newInstance() {
        WinScreenFragment fragment = new WinScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_win_screen, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        return rootView;
    }
}