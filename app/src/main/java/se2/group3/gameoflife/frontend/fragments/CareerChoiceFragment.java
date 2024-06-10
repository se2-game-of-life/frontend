package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se2.group3.gameoflife.frontend.R;


public class CareerChoiceFragment extends Fragment {
    private View rootView;


    public CareerChoiceFragment() {
        // Required empty public constructor
    }
    public static CareerChoiceFragment newInstance() {
        CareerChoiceFragment fragment = new CareerChoiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_career_choice, container, false);


        return rootView;
    }
}