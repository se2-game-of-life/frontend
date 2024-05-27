package se2.group3.gameoflife.frontend.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fasterxml.jackson.databind.ObjectMapper;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoosePathFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoosePathFragment extends Fragment {


    public ChoosePathFragment() {
        // Required empty public constructor
    }

    public static ChoosePathFragment newInstance() {
        ChoosePathFragment fragment = new ChoosePathFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GameViewModel gameViewModel;
        View rootView = inflater.inflate(R.layout.fragment_choose_path, container, false);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);


        if (getArguments() != null) {
            String lobbyDTOJson = getArguments().getString("lobbyDTO");
            if (lobbyDTOJson != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    LobbyDTO lobbyDTO = objectMapper.readValue(lobbyDTOJson, LobbyDTO.class);
                    if(lobbyDTO != null){
                        gameViewModel.setLobbyDTO(lobbyDTO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        Button btnCareer = rootView.findViewById(R.id.btnCareer);
        btnCareer.setOnClickListener(v -> {
                            gameViewModel.makeChoice(false);
                            navigateToGameBoardFragment();
                }
        );

        Button btnCollege = rootView.findViewById(R.id.btnCollege);
        btnCollege.setOnClickListener(v -> {
            //chooseLeft = chooseCollege
            gameViewModel.makeChoice(true);
            navigateToGameBoardFragment();
        });

        return rootView;
    }

    private void navigateToGameBoardFragment() {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new GameBoardFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

//    @Override
//    public void onDestroy() {
//        gameViewModel.dispose();
//        super.onDestroy();
//    }
}