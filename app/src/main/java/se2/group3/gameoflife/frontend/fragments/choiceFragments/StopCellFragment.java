package se2.group3.gameoflife.frontend.fragments.choiceFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.fragments.OverlayFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StopCellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StopCellFragment extends Fragment {
    private View rootView;
    private GameViewModel gameViewModel;


    private static final String CELLTYPE = "celltype";

    private String cellType;


    public StopCellFragment() {
        // Required empty public constructor
    }

    public static StopCellFragment newInstance(String cellType) {
        StopCellFragment fragment = new StopCellFragment();
        Bundle args = new Bundle();
        args.putString(CELLTYPE, cellType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cellType = getArguments().getString(CELLTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stop_cell, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        TextView name = rootView.findViewById(R.id.stopCellName);
        TextView description = rootView.findViewById(R.id.stopCellDescribtion);

        switch (cellType){
            case "MARRY":
                name.setText("STOP SIGN:/n Marriage");
                description.setText("Do you want to get married? /nCost: 50k /nPegs: +1");
                break;
            case "GROW_FAMILY":
                name.setText("STOP SIGN:/n Family Time");
                description.setText("Do you want to grow your family? /nCost: 50k /nPegs: +1");
                break;
            case "RETIRE_EARLY":
                name.setText("STOP SIGN:/n Retire early");
                description.setText("Do you want to retire earlier? /nCost: 0");
                break;
        }

        Button yesBTN = rootView.findViewById(R.id.stopCellYesBTN);
        Button noBTN = rootView.findViewById(R.id.stopCellNoBTN);

        yesBTN.setOnClickListener(v -> {
            gameViewModel.makeChoice(true);
            navigateToOverlayFragment();
        });

        noBTN.setOnClickListener(v -> {
            gameViewModel.makeChoice(false);
            navigateToOverlayFragment();
        });


        return rootView;
    }

    private void navigateToOverlayFragment(){
        if (getActivity() != null) {
            FragmentTransaction transactionOverLay = getActivity().getSupportFragmentManager().beginTransaction();
            OverlayFragment overlayFragment = new OverlayFragment();
            transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);
            transactionOverLay.addToBackStack(null);
            transactionOverLay.commit();
        }
    }
}