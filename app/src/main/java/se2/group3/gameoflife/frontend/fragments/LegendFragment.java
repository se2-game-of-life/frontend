package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import se2.group3.gameoflife.frontend.R;

public class LegendFragment extends Fragment {


    public LegendFragment() {
        // Required empty public constructor
    }

    public static LegendFragment newInstance() {
        LegendFragment fragment = new LegendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_legend, container, false);

        Button backBTN = rootView.findViewById(R.id.btnBacktoGame);

        backBTN.setOnClickListener(v -> {
            if (isAdded()) {
                FragmentTransaction transactionOverLay = requireActivity().getSupportFragmentManager().beginTransaction();
                OverlayFragment overlayFragment = new OverlayFragment();
                transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);
                transactionOverLay.addToBackStack(null);
                transactionOverLay.commit();
            }
        });
        return rootView;
    }
}