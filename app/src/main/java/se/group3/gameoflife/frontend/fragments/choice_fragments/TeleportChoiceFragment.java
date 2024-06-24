package se.group3.gameoflife.frontend.fragments.choice_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;
import se.group3.gameoflife.frontend.activities.GameActivity;
import se.group3.gameoflife.frontend.dto.LobbyDTO;
import se.group3.gameoflife.frontend.fragments.OverlayFragment;
import se.group3.gameoflife.frontend.networking.ConnectionService;

public class TeleportChoiceFragment extends Fragment {
    private View rootView;
    private static final String TAG = "Networking";
    private ConnectionService connectionService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TeleportChoiceFragment() {
        // Required empty public constructor
    }

    public static TeleportChoiceFragment newInstance() {
        TeleportChoiceFragment fragment = new TeleportChoiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_teleport_choice, container, false);

        Log.d(TAG, "TeleportChoiceFragment started.");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GameActivity activity = (GameActivity) getActivity();
        if (activity != null) {
            activity.getIsBound().observe(getViewLifecycleOwner(), isBound -> {
                if (Boolean.TRUE.equals(isBound)) {
                    connectionService = activity.getService();
                    if (connectionService != null) {
                        LobbyDTO lobbyDTO = connectionService.getLiveData(LobbyDTO.class).getValue();
                        if (lobbyDTO == null) {
                            Log.e(TAG, "LobbyDTO is null in TeleportChoiceFragment.");
                            return;
                        }

                        updateUI();

                        Button teleportBTN = rootView.findViewById(R.id.teleportBTN);
                        Button stayBTN = rootView.findViewById(R.id.stayBTN);

                        String uuid = connectionService.getUuidLiveData().getValue();
                        if (uuid != null && uuid.equals(lobbyDTO.getCurrentPlayer().getPlayerUUID())) {
                            teleportBTN.setVisibility(View.VISIBLE);
                            stayBTN.setVisibility(View.VISIBLE);
                        } else {
                            teleportBTN.setVisibility(View.GONE);
                            stayBTN.setVisibility(View.GONE);
                        }

                        teleportBTN.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", true)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::navigateToOverlayFragment, error -> Log.e(TAG, "Error making choice: " + error))));

                        stayBTN.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", false)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::navigateToOverlayFragment, error -> Log.e(TAG, "Error making choice: " + error))));
                    }
                }
            });
        }
    }

    private void updateUI() {
        TextView description = rootView.findViewById(R.id.teleportDescription);
        description.setText("You can choose to teleport forward 2 cells or to stay in the same cell.");
    }

    private void navigateToOverlayFragment() {
        if (isAdded()) {
            FragmentTransaction transactionOverLay = requireActivity().getSupportFragmentManager().beginTransaction();
            OverlayFragment overlayFragment = new OverlayFragment();
            transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);
            transactionOverLay.addToBackStack(null);
            transactionOverLay.commit();
        }
    }
}
