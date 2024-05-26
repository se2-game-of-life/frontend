package se2.group3.gameoflife.frontend.fragments.statistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsPlayerFragment extends Fragment {


    private static PlayerDTO PLAYER = null;


    private String mParam1;
    private String mParam2;

    public StatisticsPlayerFragment() {
        // Required empty public constructor
    }



    public static StatisticsPlayerFragment newInstance(PlayerDTO player) {
        StatisticsPlayerFragment fragment = new StatisticsPlayerFragment();
        Bundle args = new Bundle();
        PLAYER = player;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = PLAYER.getPlayerName();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics_players, container, false);

        TextView money = rootView.findViewById(R.id.moneyStat);
        TextView college = rootView.findViewById(R.id.universityDegreeStat);
        TextView job = rootView.findViewById(R.id.jobStat);
        TextView houses = rootView.findViewById(R.id.housesStat);

        if (getArguments() != null) {
            String playerUUID = getArguments().getString(PLAYER.getPlayerUUID());
        }

        if(PLAYER != null){
            money.setText("Money: " + PLAYER.getMoney());
            college.setText("College Degree: "+PLAYER.isCollegeDegree());
            job.setText("Job: " + PLAYER.getCareerCard().toString());
            houses.setText("#houses" + PLAYER.getHouses().size());
        }

        return rootView;
    }
}