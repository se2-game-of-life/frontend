package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;

import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.networking.ConnectionService;


public class ChoosePathFragment extends Fragment {
    private final String TAG = "Networking";

    ConnectionService connectionService = requireActivity().getSystemService(ConnectionService.class);
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_path, container, false);
        LobbyDTO lobbyDTO = connectionService.getLiveData(LobbyDTO.class).getValue();
        Long lobbyID = lobbyDTO.getLobbyID();
        connectionService.subscribe("/topic/lobbies/" + lobbyID, LobbyDTO.class);

        Button btnCareer = rootView.findViewById(R.id.btnCareer);
        btnCareer.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::navigateToGameBoardFragment, error -> Log.e(TAG, "Error Sending Create Lobby: " + error))));

        Button btnCollege = rootView.findViewById(R.id.btnCollege);
        btnCollege.setOnClickListener(v -> {
            compositeDisposable.add(connectionService.send("/app/lobby/choice", true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::navigateToGameBoardFragment, error -> Log.e(TAG, "Error Sending Create Lobby: " + error)));
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