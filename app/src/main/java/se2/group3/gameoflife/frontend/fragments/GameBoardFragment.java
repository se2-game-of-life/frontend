package se2.group3.gameoflife.frontend.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;


import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.BoardDTO;
import se2.group3.gameoflife.frontend.dto.CellDTO;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;


public class GameBoardFragment extends Fragment {

    private static final String TAG = "Networking";
    private Button spinButton;

    private GameViewModel viewModel;
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    public GameBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game_board, container, false);
        // Initialize spinButton
        spinButton = rootView.findViewById(R.id.buttonSpin);
        rootView.findViewById(R.id.statisticsBtn).setOnClickListener(v -> changeToStatisticsFragment());

        // Initialize viewModel
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);


        if (getArguments() != null) {
            String lobbyDTOJson = getArguments().getString("lobbyDTO");
            if (lobbyDTOJson != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    LobbyDTO lobbyDTO = objectMapper.readValue(lobbyDTOJson, LobbyDTO.class);
                    if(lobbyDTO != null){
                        viewModel.setLobbyDTO(lobbyDTO);
                    }
                } catch (NullPointerException | JsonProcessingException e) {
                    Log.d("Networking","Exception: " + e.getMessage());
                }
            }
        }

        // Set OnClickListener on spinButton
        spinButton.setOnClickListener(v -> viewModel.spinWheel());
        // Call fetchBoardData when the fragment is created
        fetchBoardData();

        return rootView;
    }

    private void fetchBoardData() {
        Log.d(TAG, "fetchBoardData started");

        disposables.add(websocketClient.subscribe("/topic/board/" + viewModel.getLobbyDTO().getCurrentPlayer().getPlayerUUID(), BoardDTO.class) //todo: needs uuid from player
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateBoardUI,
                        error -> Log.e(TAG, "Error fetching board data!", error)
                )
        );

        try {
            String temp = "I am a fetch request!";
            disposables.add(websocketClient.send("/app/fetch", temp)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {}, error -> errorMessage.setValue(error.getMessage()))
            );
            Log.d(TAG, "Fetch board request sent!");
        } catch (Exception e) {
            Log.e(TAG, "Error sending fetch board request!", e);
        }
    }

    private void updateBoardUI(BoardDTO boardDTO) {
        Log.d(TAG, "updateUI started");

        // Find the GridLayout in the layout
        GridLayout gridLayout1 = requireView().findViewById(R.id.gridLayout1);
        GridLayout gridLayout2 = requireView().findViewById(R.id.gridLayout2);

        // Clear existing views from GridLayout
        gridLayout1.removeAllViews();
        gridLayout2.removeAllViews();

        // Check if BoardDTO is not null
        if (boardDTO != null && boardDTO.getCells() != null) {
            Log.d(TAG, "Populate GridLayout with cells from BoardDTO");

            // Populate GridLayout with cells from BoardDTO
            for (int row = 0; row < boardDTO.getCells().size(); row++) {
                for (int col = 0; col < boardDTO.getCells().get(row).size(); col++) {
                    // Get the cellDTO from the BoardDTO
                    CellDTO cellDTO = boardDTO.getCells().get(row).get(col);

                    // Create a new TextView for each cell
                    TextView cell1 = new TextView(getContext());
                    PlayerCellView cell2 = new PlayerCellView(getContext());



                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                            GridLayout.spec(row, 1f), // Row weight
                            GridLayout.spec(col, 1f)  // Column weight
                    );
                    params.width = 200;  // Set the width in pixels
                    params.height = 200; // Set the height in pixels

                    // Apply the layout parameters to the TextView
                    cell1.setLayoutParams(params);
                    cell2.setLayoutParams(params);

                    gridLayout2.addView(cell2);

                    // Set text and appearance of cells based on cellDTO presence
                    if (cellDTO != null) {
                        if(Objects.equals(cellDTO.getType(), "ACTION")) {
                            cell1.setBackgroundResource(R.drawable.cell_action_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "FAMILY")) {
                            cell1.setBackgroundResource(R.drawable.cell_addpeg_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "HOUSE")) {
                            cell1.setBackgroundResource(R.drawable.cell_house_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "CASH")) {
                            cell1.setBackgroundResource(R.drawable.cell_payday_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "CAREER")) {
                            cell1.setBackgroundResource(R.drawable.cell_career_background);
                        }

                        else if(Objects.equals(cellDTO.getType(), "GROW_FAMILY")) {
                            cell1.setBackgroundResource(R.drawable.cell_stop_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "GRADUATE")) {
                            cell1.setBackgroundResource(R.drawable.cell_stop_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "MARRY")) {
                            cell1.setBackgroundResource(R.drawable.cell_stop_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "MID_LIFE")) {
                            cell1.setBackgroundResource(R.drawable.cell_stop_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "RETIRE_EARLY")) {
                            cell1.setBackgroundResource(R.drawable.cell_stop_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "RETIREMENT")) {
                            cell1.setBackgroundResource(R.drawable.cell_stop_background);
                        }
                    }

                    // Add cell to the GridLayout
                    gridLayout1.addView(cell1);
                }
            }
        } else {
            // Log a message or handle the situation when BoardDTO or its cells are null
            Log.e(TAG, "BoardDTO or cells are null");
        }

        updatePlayerUI(2, 2, 1);
        updatePlayerUI(2, 2, 2);

    }

    private void changeToStatisticsFragment() {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            StatisticsFragment fragment = new StatisticsFragment();
            try {
                Bundle bundle = new Bundle();
                ObjectMapper objectMapper = new ObjectMapper();
                bundle.putString("lobbyDTO", objectMapper.writeValueAsString(viewModel.getLobbyDTO()));
                fragment.setArguments(bundle);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            transaction.replace(R.id.fragmentContainerView, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void updatePlayerUI(int row, int col, int playerNumber) {
        Log.d(TAG, "updatePlayerUI started");

        // Find the GridLayout in the layout
        GridLayout gridLayout = requireView().findViewById(R.id.gridLayout2);

        // Calculate the index of the cell in the GridLayout
        int index = row * gridLayout.getColumnCount() + col;

        // Check if the index is within the bounds of the GridLayout
        if (index >= 0 && index < gridLayout.getChildCount()) {
            // Get the PlayerCellView representing the cell
            PlayerCellView cellView = (PlayerCellView) gridLayout.getChildAt(index);

            // Determine which player's symbol to update based on playerNumber
            switch (playerNumber) {
                case 1:
                    cellView.setPlayer1Text("O.");
                    break;
                case 2:
                    cellView.setPlayer2Text(".");
                    break;
                case 3:
                    cellView.setPlayer3Text(".");
                    break;
                case 4:
                    cellView.setPlayer4Text(".");
                    break;
                default:
                    Log.e(TAG, "Invalid player number: " + playerNumber);
                    break;
            }
        }
    }



}