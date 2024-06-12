package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WinScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WinScreenFragment extends Fragment {
    private GameViewModel gameViewModel;
    private View rootView;

    public WinScreenFragment() {
        // Required empty public constructor
    }


    public static WinScreenFragment newInstance() {
        WinScreenFragment fragment = new WinScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_win_screen, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        LobbyDTO lobbyDTO = gameViewModel.getLobbyDTO();

        List<PlayerDTO> players = lobbyDTO.getPlayers();

        sortPlayersByMoney(players);

        updateUI(players);

        return rootView;
    }

    private static void sortPlayersByMoney(List<PlayerDTO> players) {
        players.sort(Comparator.comparingDouble(PlayerDTO::getMoney));
    }

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

}