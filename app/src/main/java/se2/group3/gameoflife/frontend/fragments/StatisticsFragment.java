package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.activities.GameActivity;
import se2.group3.gameoflife.frontend.activities.StartGameActivity;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.fragments.statistics.statisticsPlayerFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    private GameViewModel gameViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        LobbyDTO lobbyDTO = gameViewModel.getLobbyDTO();
        List<PlayerDTO> players = lobbyDTO.getPlayers();
        for (int i = 1; i <= players.size(); i++) {
            switch (i) {
                case 1:
                    Button player1 = rootView.findViewById(R.id.button_player1);
                    player1.setVisibility(View.VISIBLE);
                    player1.setText(players.get(i).getPlayerName());
                    break;
                case 2:
                    Button player2 = rootView.findViewById(R.id.button_player2);
                    player2.setVisibility(View.VISIBLE);
                    player2.setText(players.get(i).getPlayerName());
                    break;
                case 3:
                    Button player3 = rootView.findViewById(R.id.button_player3);
                    player3.setVisibility(View.VISIBLE);
                    player3.setText(players.get(i).getPlayerName());
                    break;
                case 4:
                    Button player4 = rootView.findViewById(R.id.button_player4);
                    player4.setVisibility(View.VISIBLE);
                    player4.setText(players.get(i).getPlayerName());
                    break;
                default:
                    Log.d("Networking", "default case in statistic fragment");
            }

        }
        return rootView;
    }

}