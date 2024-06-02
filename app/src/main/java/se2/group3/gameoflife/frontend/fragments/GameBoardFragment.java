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
                        this::updateUI,
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

    private void updateUI(BoardDTO boardDTO) {
        Log.d(TAG, "updateUI started");

        // Find the GridLayout in the layout
        GridLayout gridLayout = requireView().findViewById(R.id.gridLayout);

        // Clear existing views from GridLayout
        gridLayout.removeAllViews();

        // Check if BoardDTO is not null
        if (boardDTO != null && boardDTO.getCells() != null) {
            Log.d(TAG, "Populate GridLayout with cells from BoardDTO");

            // Populate GridLayout with cells from BoardDTO
            for (int row = 0; row < boardDTO.getCells().size(); row++) {
                for (int col = 0; col < boardDTO.getCells().get(row).size(); col++) {
                    // Get the cellDTO from the BoardDTO
                    CellDTO cellDTO = boardDTO.getCells().get(row).get(col);

                    // Create a new TextView for each cell
                    TextView cell = new TextView(getContext());
                    cell.setLayoutParams(new GridLayout.LayoutParams(
                            GridLayout.spec(row, 1f), // Row weight
                            GridLayout.spec(col, 1f) // Column weight
                    ));

                    // Set text and appearance of cells based on cellDTO presence
                    if (cellDTO != null) {
                        if(Objects.equals(cellDTO.getType(), "actionCell")) {
                            cell.setBackgroundResource(R.drawable.cell_action_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "addpegCell")) {
                            cell.setBackgroundResource(R.drawable.cell_addpeg_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "investCell")) {
                            cell.setBackgroundResource(R.drawable.cell_invest_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "houseCell")) {
                            cell.setBackgroundResource(R.drawable.cell_house_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "paydayCell")) {
                            cell.setBackgroundResource(R.drawable.cell_payday_background);
                        }
                        else if(Objects.equals(cellDTO.getType(), "careerCell")) {
                            cell.setBackgroundResource(R.drawable.cell_career_background);
                        }
                        else if(cellDTO.getType().endsWith("StopCell")) {
                            cell.setBackgroundResource(R.drawable.cell_stop_background);
                        }
                        else if(cellDTO.getType().endsWith("start")) {
                            cell.setBackgroundResource(R.drawable.cell_start_background);
                        }
                    }

                    // Add cell to the GridLayout
                    gridLayout.addView(cell);
                }
            }
        } else {
            // Log a message or handle the situation when BoardDTO or its cells are null
            Log.e(TAG, "BoardDTO or cells are null");
        }

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



}