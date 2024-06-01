package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
        Log.d(TAG, "onCreateView started");
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);


        money = rootView.findViewById(R.id.moneyStat);
        college = rootView.findViewById(R.id.universityDegreeStat);
        job = rootView.findViewById(R.id.jobStat);
        houses = rootView.findViewById(R.id.housesStat);


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

                gameViewModel.getLobby().observe(getViewLifecycleOwner(), new Observer<LobbyDTO>() {
                    @Override
                    public void onChanged(LobbyDTO lobbyDTO) {
                        List<PlayerDTO> playersChanged = lobbyDTO.getPlayers();
                        PlayerDTO playerDTO = playersChanged.get(0);
                        money.setVisibility(View.VISIBLE);
                        college.setVisibility(View.VISIBLE);
                        job.setVisibility(View.VISIBLE);
                        houses.setVisibility(View.VISIBLE);
                        money.setText("Money: " + playerDTO.getMoney());
                        college.setText("College Degree: "+ playerDTO.isCollegeDegree());
                        if(playerDTO.getCareerCard() == null){
                            job.setText("Job: none");
                        } else{
                            job.setText("Job: " + playerDTO.getCareerCard().toString());
                        }
                        houses.setText("#houses: " + playerDTO.getHouses().size());

                        playerButtons[0].setOnClickListener(v -> updateStatistics(playersChanged.get(0)));

                        playerButtons[1].setOnClickListener(v -> updateStatistics(playersChanged.get(1)));

                        playerButtons[2].setOnClickListener(v -> updateStatistics(playersChanged.get(2)));

                        playerButtons[3].setOnClickListener(v -> updateStatistics(playersChanged.get(3)));
                    }
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
        money.setText("Money: " + player.getMoney());
        college.setText("College Degree: "+ player.isCollegeDegree());
        if(player.getCareerCard() == null){
            job.setText("Job: none");
        } else{
            job.setText("Job: " + player.getCareerCard().toString());
        }
        houses.setText("#houses: " + player.getHouses().size());
    }

    private void updateLobby(LobbyDTO lobbyDTO){
        gameViewModel.setLobbyDTO(lobbyDTO);
    }

}