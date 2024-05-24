package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.viewmodels.MakeDecisionsViewModel;

public class MakeDecisionsFragment extends Fragment {

    private static final String TAG = "Networking";
    private Button buttonLeft;
    private Button buttonRight;

    private MakeDecisionsViewModel viewModel;
    private String playerUuid; // Assuming you have a way to set this UUID for the player

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
        viewModel = new ViewModelProvider(this).get(MakeDecisionsViewModel.class);

        // Set up button click listeners
        setupButtonListeners();

        // Observe the LiveData from the ViewModel
        observeViewModel();

        return rootView;
    }

    private void setupButtonListeners() {
        buttonLeft.setOnClickListener(v -> makeChoice(true));
        buttonRight.setOnClickListener(v -> makeChoice(false));
    }

    private void makeChoice(boolean chooseLeft) {
        if (playerUuid == null) {
            Log.e(TAG, "Player UUID is not set!");
            return;
        }

        // Use the ViewModel to make the choice
        viewModel.makeChoice(chooseLeft, playerUuid);
    }

    private void observeViewModel() {
        // Observe choice result
        viewModel.getChoiceResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (Boolean.TRUE.equals(success)) {
                    Toast.makeText(getContext(), "Choice successfully made!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to make choice!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Log.e(TAG, "Error: " + errorMessage);
                    Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Assuming you have a method to set the player UUID
    public void setPlayerUuid(String uuid) {
        this.playerUuid = uuid;
    }
}

