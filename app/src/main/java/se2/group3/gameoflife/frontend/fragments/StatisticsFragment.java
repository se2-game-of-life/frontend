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


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import se2.group3.gameoflife.frontend.R;

import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.fragments.statistics.StatisticsPlayerFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    private GameViewModel gameViewModel;
    public final String TAG = "Networking";

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
        Log.d(TAG, "onCreateView gestartet");
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        if (savedInstanceState == null) {
            Log.d(TAG, "savedInstanceState ist null, Fragment wird hinzugefügt");
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer_PlayerStat, new StatisticsPlayerFragment())
                    .commit();
        }

        try {
            gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
            LobbyDTO lobbyDTO = gameViewModel.getLobbyDTO();
            List<PlayerDTO> players = lobbyDTO.getPlayers();

            if (players == null || players.isEmpty()) {
                Log.e(TAG, "Playerliste ist null oder leer");
                return rootView;
            }

            Button[] playerButtons = new Button[4];
            playerButtons[0] = rootView.findViewById(R.id.button_player1);
            playerButtons[1] = rootView.findViewById(R.id.button_player2);
            playerButtons[2] = rootView.findViewById(R.id.button_player3);
            playerButtons[3] = rootView.findViewById(R.id.button_player4);

            for (int i = 0; i < players.size() && i < playerButtons.length; i++) {
                PlayerDTO player = players.get(i);
                Button playerButton = playerButtons[i];
                playerButton.setVisibility(View.VISIBLE);
                playerButton.setText(player.getPlayerName());
                playerButton.setOnClickListener(v -> {
                    gameViewModel.setPlayerDTO(player);
                    replaceFragment(player);
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Fehler im onCreateView: " + e.getMessage(), e);
        }

        return rootView;
    }

    private void replaceFragment(PlayerDTO player) {
        if (player == null) {
            Log.e(TAG, "PlayerDTO is null");
            return;
        }

        try {
            Fragment fragment = StatisticsPlayerFragment.newInstance(player);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_PlayerStat, fragment);
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Fehler beim Ersetzen des Fragments: " + e.getMessage(), e);
        }
    }


}