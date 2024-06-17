package se2.group3.gameoflife.frontend.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.activities.GameActivity;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.ConnectionService;

public class OverlayFragment extends Fragment {
    private View rootView;
    public final String TAG = "Networking";
    private boolean playerName;
    ConnectionService connectionService;
    CompositeDisposable compositeDisposable;

    public OverlayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_overlay, container, false);
        compositeDisposable = new CompositeDisposable();

        Button spinButton = rootView.findViewById(R.id.spinButton);
        Button cheatButton = rootView.findViewById(R.id.cheatButton);
        Button legendButton = rootView.findViewById(R.id.legendBTN);

        GameActivity activity = (GameActivity) requireActivity();

        activity.getIsBound().observe(getViewLifecycleOwner(), isBound -> {
            if(isBound) {
                connectionService = activity.getService();
                assert connectionService != null;

                connectionService.getLiveData(LobbyDTO.class).observe(getViewLifecycleOwner(), lobby -> {
                    String uuid = connectionService.getUuidLiveData().getValue();
                    if (uuid != null && uuid.equals(lobby.getCurrentPlayer().getPlayerUUID())) {
                        spinButton.setVisibility(View.VISIBLE);
                    } else {
                        spinButton.setVisibility(View.GONE);
                    }

                    updateStatistics(lobby);
                });

                cheatButton.setOnClickListener(view -> {
                    compositeDisposable.add(connectionService.send("/app/cheat", "")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                            }, error -> Log.e(TAG, "Error cheating: " + error)));
                });

                spinButton.setOnClickListener(view -> {
                    compositeDisposable.add(connectionService.send("/app/lobby/spin", "")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                            }, error -> Log.e(TAG, "Error spin the wheel:  " + error)));
                });

                legendButton.setOnClickListener(v -> {
                    if (isAdded()) {
                        FragmentTransaction transactionOverLay = requireActivity().getSupportFragmentManager().beginTransaction();
                        LegendFragment legendFragment = new LegendFragment();
                        transactionOverLay.replace(R.id.fragmentContainerView2, legendFragment);
                        transactionOverLay.addToBackStack(null);
                        transactionOverLay.commit();
                    }
                });
            }
        });

        return rootView;
    }

    private void updateStatistics(LobbyDTO lobby) {
        List<PlayerDTO> players = lobby.getPlayers();

        if (players == null || players.isEmpty()) {
            Log.e(TAG, "Player list is null or empty");
            return;
        }

        Button[] playerButtons = new Button[4];
        playerButtons[0] = rootView.findViewById(R.id.player1Button);
        playerButtons[1] = rootView.findViewById(R.id.player2Button);
        playerButtons[2] = rootView.findViewById(R.id.player3Button);
        playerButtons[3] = rootView.findViewById(R.id.player4Button);

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

        playerButtons[0].setOnLongClickListener(v -> {
            report(0);
            return true;
        });

        playerButtons[1].setOnClickListener(v -> {
            if (playerName) {
                updateButton(playerButtons, players, players.get(1));
            } else {
                setPlayerNamesButton(players, playerButtons);
            }
        });

        playerButtons[1].setOnLongClickListener(v -> {
            report(1);
            return true;
        });

        playerButtons[2].setOnClickListener(v -> {
            if (playerName) {
                updateButton(playerButtons, players, players.get(2));
            } else {
                setPlayerNamesButton(players, playerButtons);
            }
        });

        playerButtons[2].setOnLongClickListener(v -> {
            report(2);
            return true;
        });

        playerButtons[3].setOnClickListener(v -> {
            if (playerName) {
                updateButton(playerButtons, players, players.get(3));
            } else {
                setPlayerNamesButton(players, playerButtons);
            }
        });

        playerButtons[3].setOnLongClickListener(v -> {
            report(3);
            return true;
        });
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
                    playerButton.setText("Money: " + playerDTO.getMoney() + "\nCollege: " + playerDTO.isCollegeDegree() + "\nJob: none " + "\n#Pegs: " + playerDTO.getNumberOfPegs() +
                            "\n#houses: " + playerDTO.getHouses().size());
                } else {
                    playerButton.setText("Money: " + playerDTO.getMoney() + "\nCollege: " + playerDTO.isCollegeDegree() + "\nJob: " + playerDTO.getCareerCard().getName() +
                            "\nSalary: " + playerDTO.getCareerCard().getSalary() + "\nBonus: " + playerDTO.getCareerCard().getBonus() + "\n#Pegs: " + playerDTO.getNumberOfPegs() +
                            "\n#houses: " + playerDTO.getHouses().size());
                }
            }
        }
        playerName = false;
    }


    private void report(int player) {
        LobbyDTO lobby = connectionService.getLiveData(LobbyDTO.class).getValue();
        assert lobby != null;
        String playerUUID = lobby.getPlayers().get(player).getPlayerUUID();

        compositeDisposable.add(connectionService.send("/app/report", playerUUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, error -> Log.e(TAG, "Error Sending Create Lobby: " + error)));

    }
}