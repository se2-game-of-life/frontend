package se2.group3.gameoflife.frontend.fragments.statistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsPlayerFragment extends Fragment {


    private GameViewModel gameViewModel;
    private static PlayerDTO PLAYER = null;

    private View rootView;
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
        rootView = inflater.inflate(R.layout.fragment_statistics_players, container, false);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        gameViewModel.getPlayerDTO().observe(getViewLifecycleOwner(), this::updateStatistics);

        return rootView;
    }

    private void updateStatistics(PlayerDTO playerDTO){
        TextView money = rootView.findViewById(R.id.moneyStat);
        TextView college = rootView.findViewById(R.id.universityDegreeStat);
        TextView job = rootView.findViewById(R.id.jobStat);
        TextView houses = rootView.findViewById(R.id.housesStat);
        if(playerDTO != null){
            money.setText("Money: " + playerDTO.getMoney());
            college.setText("College Degree: "+playerDTO.isCollegeDegree());
            job.setText("Job: " + playerDTO.getCareerCard().toString());
            houses.setText("#houses" + playerDTO.getHouses().size());
        }
    }
}