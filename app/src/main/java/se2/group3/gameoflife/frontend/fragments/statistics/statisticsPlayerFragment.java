package se2.group3.gameoflife.frontend.fragments.statistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se2.group3.gameoflife.frontend.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link statisticsPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class statisticsPlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public statisticsPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param playerName Parameter 1.
     * @param uuid Parameter 2.
     * @return A new instance of fragment statisticsPlayer_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static statisticsPlayerFragment newInstance(String playerName, String uuid) {
        statisticsPlayerFragment fragment = new statisticsPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, playerName);
        args.putString(ARG_PARAM2, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics_players, container, false);
    }
}