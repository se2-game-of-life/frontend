package se2.group3.gameoflife.frontend.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoosePathFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoosePathFragment extends Fragment {
    private GameViewModel gameViewModel;

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
        View rootView = inflater.inflate(R.layout.fragment_choose_path, container, false);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);


//        if (getArguments() != null) {
//            PlayerDTO playerDTO = getArguments().getParcelable("playerDTO");
//            gameViewModel.setPlayerDTO(playerDTO);
//        }

//
//        Button btnCareer = rootView.findViewById(R.id.btnCareer);
//        btnCareer.setOnClickListener(v -> gameViewModel.choosePath(false));
//
//        Button btnCollege = rootView.findViewById(R.id.btnCollege);
//        btnCollege.setOnClickListener(v -> gameViewModel.choosePath(true));

        return rootView;
    }

//    @Override
//    public void onDestroy() {
//        gameViewModel.dispose();
//        super.onDestroy();
//    }
}