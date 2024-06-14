package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;

import se2.group3.gameoflife.frontend.activities.GameActivity;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.networking.ConnectionService;
import se2.group3.gameoflife.frontend.networking.ConnectionServiceCallback;


public class ChoosePathFragment extends Fragment {

    private final String TAG = "ConnectionService";
    private ConnectionService connectionService;
    private CompositeDisposable compositeDisposable;

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_path, container, false);
        compositeDisposable = new CompositeDisposable();

        GameActivity activity = (GameActivity) getActivity();
        assert activity != null;

        Button careerBTN = rootView.findViewById(R.id.btnCareer);
        Button collegeBTN = rootView.findViewById(R.id.btnCollege);

        activity.getIsBound().observe(getViewLifecycleOwner(), isBound -> {
            if (isBound) {
                connectionService = activity.getService();
                assert connectionService != null;

                collegeBTN.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", true)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::navigateToGameBoardFragment, error -> Log.e(TAG, "Error Sending Create Lobby: " + error))));


                careerBTN.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", false)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::navigateToGameBoardFragment, error -> Log.e(TAG, "Error Sending Create Lobby: " + error))));

            }
        });

        return rootView;
    }

    private void navigateToGameBoardFragment() {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            FragmentTransaction transactionOverLay = getActivity().getSupportFragmentManager().beginTransaction();
            GameBoardFragment gameBoardFragment = new GameBoardFragment();
            OverlayFragment overlayFragment = new OverlayFragment();

            transaction.replace(R.id.fragmentContainerView, gameBoardFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);
            transactionOverLay.addToBackStack(null);
            transactionOverLay.commit();
        }
    }
}