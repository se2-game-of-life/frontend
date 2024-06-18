package se2.group3.gameoflife.frontend.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Comparator;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.activities.GameActivity;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.ConnectionService;

public class WinScreenFragment extends Fragment {
    private View rootView;

    ConnectionService connectionService;
    CompositeDisposable compositeDisposable;
    private boolean playerName;


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
    public WinScreenFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_win_screen, container, false);
        compositeDisposable  = new CompositeDisposable();

        GameActivity activity = (GameActivity) getActivity();
        if (activity != null) {
            activity.getIsBound().observe(getViewLifecycleOwner(), isBound -> {
                if (isBound) {
                    connectionService = activity.getService();
                    if (connectionService != null) {
                        connectionService.getLiveData(LobbyDTO.class).observe(getViewLifecycleOwner(), lobby -> {
                            List<PlayerDTO> players = lobby.getPlayers();

                            players.sort(Comparator.comparingDouble(PlayerDTO::getMoney).reversed());

                            updateUI(players);
                            updateStatistics(lobby);

                        });

                    }
                }
            });
        }



        return rootView;
    }

    private void setPlayerNamesButton(List<PlayerDTO> players, Button[] playerButtons) {
        for (int i = 0; i < players.size() && i < playerButtons.length; i++) {
            PlayerDTO player = players.get(i);
            Button playerButton = playerButtons[i];
            playerButton.setText(player.getPlayerName());
        }
        playerName = true;
    }

    @SuppressLint("SetTextI18n")
    private void updateButton(Button[] playerButtons, List<PlayerDTO> players, PlayerDTO playerDTO) {
        setPlayerNamesButton(players, playerButtons);

        for (Button playerButton : playerButtons) {
            if (playerButton.getText().equals(playerDTO.getPlayerName())) {
                if (playerDTO.getCareerCard() == null) {
                    playerButton.setText("College: " + playerDTO.isCollegeDegree() + "\nJob: none " + "\n#Pegs: " + playerDTO.getNumberOfPegs() +
                            "\n#houses: " + playerDTO.getHouses().size());
                } else {
                    playerButton.setText("College: " + playerDTO.isCollegeDegree() + "\nJob: " + playerDTO.getCareerCard().getName() +
                            "\n#Pegs: " + playerDTO.getNumberOfPegs() +
                            "\n#houses: " + playerDTO.getHouses().size());
                }
            }
        }
        playerName = false;
    }


    @SuppressLint("SetTextI18n")
    private void updateUI(List<PlayerDTO> players){
        Button[] playerNames = new Button[4];
        playerNames[0] = rootView.findViewById(R.id.nameFirstPlayer);
        playerNames[1] = rootView.findViewById(R.id.nameSecondPlayer);
        playerNames[2] = rootView.findViewById(R.id.nameThirdPlayer);
        playerNames[3] = rootView.findViewById(R.id.nameFourthPlayer);

        TextView[] playerMoney = new TextView[4];
        playerMoney[0] = rootView.findViewById(R.id.moneyFirstPlayer);
        playerMoney[1] = rootView.findViewById(R.id.moneySecondPlayer);
        playerMoney[2] = rootView.findViewById(R.id.moneyThirdPlayer);
        playerMoney[3] = rootView.findViewById(R.id.moneyFourthPlayer);

        TextView[] places = new TextView[4];
        places[0] = rootView.findViewById(R.id.firstPlace);
        places[1] = rootView.findViewById(R.id.secondPlace);
        places[2] = rootView.findViewById(R.id.thirdPlace);
        places[3] = rootView.findViewById(R.id.fourthPlace);


        for(int i = 0; i < players.size(); i++){
            playerNames[i].setText(players.get(i).getPlayerName());
            playerNames[i].setVisibility(View.VISIBLE);
            playerMoney[i].setText("$: " + players.get(i).getMoney());
            playerMoney[i].setVisibility(View.VISIBLE);
            places[i].setVisibility(View.VISIBLE);
        }
    }

    private void updateStatistics(LobbyDTO lobby) {
        List<PlayerDTO> players;
        String TAG = "Networking";

        try{
            players = lobby.getPlayers();
        } catch(NullPointerException e){
            Log.e(TAG, "Player list is null or empty");
            return;
        }

        Button[] playerButtons = new Button[4];
        playerButtons[0] = rootView.findViewById(R.id.nameFirstPlayer);
        playerButtons[1] = rootView.findViewById(R.id.nameSecondPlayer);
        playerButtons[2] = rootView.findViewById(R.id.nameThirdPlayer);
        playerButtons[3] = rootView.findViewById(R.id.nameFourthPlayer);

        setPlayerNamesButton(players, playerButtons);

        for (int i = 0; i < players.size() && i < playerButtons.length; i++) {
            PlayerDTO player = players.get(i);
            Button playerButton = playerButtons[i];
            playerButton.setVisibility(View.VISIBLE);
            playerButton.setText(player.getPlayerName());
        }


        playerButtons[0].setOnClickListener(v -> {
            if (playerName) {
                updateButton(playerButtons, players, players.get(0));
            } else {
                setPlayerNamesButton(players, playerButtons);
            }
        });


        playerButtons[1].setOnClickListener(v -> {
            if (playerName) {
                updateButton(playerButtons, players, players.get(1));
            } else {
                setPlayerNamesButton(players, playerButtons);
            }
        });


        playerButtons[2].setOnClickListener(v -> {
            if (playerName) {
                updateButton(playerButtons, players, players.get(2));
            } else {
                setPlayerNamesButton(players, playerButtons);
            }
        });


        playerButtons[3].setOnClickListener(v -> {
            if (playerName) {
                updateButton(playerButtons, players, players.get(3));
            } else {
                setPlayerNamesButton(players, playerButtons);
            }
        });

    }

}