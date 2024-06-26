package se.group3.gameoflife.frontend.fragments.choice_fragments;

import android.annotation.SuppressLint;
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
import se.group3.gameoflife.frontend.fragments.OverlayFragment;
import se.group3.gameoflife.frontend.networking.ConnectionService;

public class StopCellFragment extends Fragment {
    private View rootView;

    private static final String TYPE = "celltype";
    private String cellType;
    private static final String TAG = "Networking";
    private ConnectionService connectionService;
    private CompositeDisposable compositeDisposable;

    public StopCellFragment() {
        // Required empty public constructor
    }

    public static StopCellFragment newInstance(String cellType) {
        StopCellFragment fragment = new StopCellFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, cellType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cellType = getArguments().getString(TYPE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stop_cell, container, false);

        compositeDisposable = new CompositeDisposable();

        TextView name = rootView.findViewById(R.id.stopCellName);
        TextView description = rootView.findViewById(R.id.stopCellDescribtion);
        TextView cost = rootView.findViewById(R.id.stopCellCost);
        TextView gain = rootView.findViewById(R.id.stopCellGain);

        switch (cellType) {
            case "MARRY":
                name.setText("STOP SIGN: Marriage");
                description.setText("Do you want to get married?");
                cost.setText("Cost: 50k");
                gain.setText("Pegs: +1");
                break;
            case "GROW_FAMILY":
                name.setText("STOP SIGN: Family Time");
                description.setText("Do you want to grow your family?");
                cost.setText("Cost: 50k");
                gain.setText("Pegs: +1");
                break;
            case "RETIRE_EARLY":
                name.setText("STOP SIGN: Retire early");
                description.setText("Do you want to retire earlier?");
                cost.setText("Cost: 0");
                gain.setText("Who doesn't want to retire early...?");
                break;
            default:
                Log.e(TAG, "Unknown Stop Cell Type");
        }

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
                        Button yesBTN = rootView.findViewById(R.id.stopCellYesBTN);
                        Button noBTN = rootView.findViewById(R.id.stopCellNoBTN);

                        yesBTN.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", true)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::navigateToOverlayFragment, error -> Log.e(TAG, "Error making choice: " + error))));

                        noBTN.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", false)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::navigateToOverlayFragment, error -> Log.e(TAG, "Error making choice: " + error))));
                    }
                }
            });
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
