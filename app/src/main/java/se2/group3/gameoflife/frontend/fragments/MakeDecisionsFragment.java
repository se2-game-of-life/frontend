package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class MakeDecisionsFragment extends Fragment {

    private static final String TAG = "Networking";
    private Button buttonLeft;
    private Button buttonRight;

    private GameViewModel viewModel;

    public MakeDecisionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_make_decisions, container, false);
        // Initialize buttons
        buttonLeft = rootView.findViewById(R.id.buttonLeft);
        buttonRight = rootView.findViewById(R.id.buttonRight);

        // Initialize viewModel
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // Set up button click listeners
        setupButtonListeners();

        // Observe the LiveData from the ViewModel
        observeViewModel();

        return rootView;
    }

    private void setupButtonListeners() {
        buttonLeft.setOnClickListener(v -> viewModel.makeChoice(true));
        buttonRight.setOnClickListener(v -> viewModel.makeChoice(false));
    }

    private void observeViewModel() {
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Log.e(TAG, "Error: " + errorMessage);
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

