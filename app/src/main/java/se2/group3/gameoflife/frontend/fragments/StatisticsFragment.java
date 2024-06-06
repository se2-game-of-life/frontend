package se2.group3.gameoflife.frontend.fragments;

import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import se2.group3.gameoflife.frontend.R;

import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    private GameViewModel gameViewModel;
    public final String TAG = "Networking";

    private TextView money;
    private TextView college;
    private TextView job;
    private TextView houses;


    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Statistics fragment opened");
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);


        money = rootView.findViewById(R.id.moneyStat);
        college = rootView.findViewById(R.id.universityDegreeStat);
        job = rootView.findViewById(R.id.jobStat);
        houses = rootView.findViewById(R.id.housesStat);

        rootView.findViewById(R.id.backBTN).setOnClickListener(v -> {
            if (getActivity() != null) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                GameBoardFragment fragment = new GameBoardFragment();
                try {
                    Bundle bundle = new Bundle();
                    ObjectMapper objectMapper = new ObjectMapper();
                    bundle.putString("lobbyDTO", objectMapper.writeValueAsString(gameViewModel.getLobbyDTO()));
                    fragment.setArguments(bundle);
                } catch (JsonProcessingException e) {
                    Log.d(TAG, "Error getting lobbyDTO: " + e.getMessage());
                }
                transaction.replace(R.id.fragmentContainerView, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        try {
            gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
            if (getArguments() != null) {
                String lobbyDTOJson = getArguments().getString("lobbyDTO");
                if (lobbyDTOJson != null) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        LobbyDTO lobbyDTO = objectMapper.readValue(lobbyDTOJson, LobbyDTO.class);
                        if(lobbyDTO != null){
                            gameViewModel.setLobbyDTO(lobbyDTO);
                            gameViewModel.getLobby().observe(getViewLifecycleOwner(), this::updateLobby);
                        }
                    } catch (NullPointerException | JsonProcessingException e) {
                        Log.d("Networking","Exception: " + e.getMessage());
                    }
                }
            }

                LobbyDTO lobbyDTO = gameViewModel.getLobbyDTO();
                List<PlayerDTO> players = lobbyDTO.getPlayers();

                if (players == null || players.isEmpty()) {
                    Log.e(TAG, "Playerlist is null or empty");
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
                }

                gameViewModel.getLobby().observe(getViewLifecycleOwner(), lobbyDTO1 -> {
                    List<PlayerDTO> playersChanged = lobbyDTO1.getPlayers();
                    PlayerDTO playerDTO = playersChanged.get(0);
                    updateStatistics(playerDTO);

                    playerButtons[0].setOnClickListener(v -> updateStatistics(playersChanged.get(0)));

                    playerButtons[1].setOnClickListener(v -> updateStatistics(playersChanged.get(1)));

                    playerButtons[2].setOnClickListener(v -> updateStatistics(playersChanged.get(2)));

                    playerButtons[3].setOnClickListener(v -> updateStatistics(playersChanged.get(3)));
                });

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
        }

        return rootView;
    }


    private void updateStatistics(PlayerDTO player){
        money.setVisibility(View.VISIBLE);
        college.setVisibility(View.VISIBLE);
        job.setVisibility(View.VISIBLE);
        houses.setVisibility(View.VISIBLE);
        money.setText(getString(R.string.money) + player.getMoney());
        college.setText(getString(R.string.college_degree)+ player.isCollegeDegree());
        if(player.getCareerCard() == null){
            job.setText(R.string.job_none);
        } else{
            job.setText(getString(R.string.job) + player.getCareerCard().toString());
        }
        houses.setText(getString(R.string.houses) + player.getHouses().size());
    }

    private void updateLobby(LobbyDTO lobbyDTO){
        gameViewModel.setLobbyDTO(lobbyDTO);
    }

}