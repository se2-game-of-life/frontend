package se.group3.gameoflife.frontend.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se.group3.gameoflife.frontend.activities.GameActivity;
import se.group3.gameoflife.frontend.dto.PlayerDTO;
import se.group3.gameoflife.frontend.networking.ConnectionService;
import se.group3.gameoflife.frontend.viewmodels.GameBoardViewModel;
import se2.group3.gameoflife.frontend.R;
import se.group3.gameoflife.frontend.dto.BoardDTO;
import se.group3.gameoflife.frontend.dto.CellDTO;
import se.group3.gameoflife.frontend.dto.LobbyDTO;


public class GameBoardFragment extends Fragment {

    private static final String TAG = "Networking";
    private CompositeDisposable compositeDisposable;
    private ConnectionService connectionService;
    private GameBoardViewModel gameViewModel;

    public GameBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_board, container, false);
        compositeDisposable = new CompositeDisposable();
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameBoardViewModel.class);
        makeOverlayVisible();

        GameActivity activity = (GameActivity) getActivity();
        assert activity != null;

        activity.getIsBound().observe(getViewLifecycleOwner(), isBound -> {
            if (Boolean.TRUE.equals(isBound)) {
                Log.d(TAG, "Bound");
                connectionService = activity.getService();
                LobbyDTO lobby = connectionService.getLiveData(LobbyDTO.class).getValue();
                assert lobby != null;
                fetchBoardData();

                connectionService.getLiveData(LobbyDTO.class).observe(getViewLifecycleOwner(), lobbyUpdated -> {
                    try {
                        deletePlayerDots(gameViewModel.getOldLobbyDTO().getPlayers());
                    } catch (Exception e) {
                        Log.e(TAG, "Deleting playerDots was called to early.");
                    }
                    updatePlayerDots(lobbyUpdated.getPlayers());
                    gameViewModel.setOldLobbyDTO(lobbyUpdated);
                });
            }
        });

        return rootView;
    }

    private void fetchBoardData() {
        LobbyDTO lobby = connectionService.getLiveData(LobbyDTO.class).getValue();
        assert lobby != null;
        compositeDisposable.add(connectionService.subscribe("/topic/board/" + lobby.getLobbyID(), BoardDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> connectionService.getLiveData(BoardDTO.class).observe(getViewLifecycleOwner(), this::updateBoardUI)));

        compositeDisposable.add(connectionService.send("/app/fetch", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
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
            gameViewModel.setCellDTOHashMap(boardDTO);

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
                    renderCell(cellDTO, cell1);

                    // Add cell to the GridLayout
                    gridLayout1.addView(cell1);
                }
            }
        } else {
            // Log a message or handle the situation when BoardDTO or its cells are null
            Log.e(TAG, "BoardDTO or cells are null");
        }

        LobbyDTO lobbyDTO = connectionService.getLiveData(LobbyDTO.class).getValue();
        assert lobbyDTO != null;
        List<PlayerDTO> players = lobbyDTO.getPlayers();
        if (players == null) {
            Log.e(TAG, "Players are null");
        } else {
            updatePlayerDots(players);
            gameViewModel.setOldLobbyDTO(lobbyDTO);
            Log.d(TAG, "Old Lobby DTO " + lobbyDTO.getPlayers().size());
        }
    }

    private static void renderCell(CellDTO cellDTO, TextView cellView) {
        if (cellDTO == null) return;
        
        switch (cellDTO.getType()) {
            case "ACTION":
                cellView.setBackgroundResource(R.drawable.cell_action_background);
                break;
            case "FAMILY":
                cellView.setBackgroundResource(R.drawable.cell_addpeg_background);
                break;
            case "HOUSE":
                cellView.setBackgroundResource(R.drawable.cell_house_background);
                break;
            case "CASH":
                cellView.setBackgroundResource(R.drawable.cell_payday_background);
                break;
            case "CAREER":
                cellView.setBackgroundResource(R.drawable.cell_career_background);
                break;
            case "GROW_FAMILY":
            case "GRADUATE":
            case "MARRY":
            case "MID_LIFE":
            case "RETIRE_EARLY":
                cellView.setBackgroundResource(R.drawable.cell_stop_background);
                break;
            case "RETIREMENT":
                cellView.setBackgroundResource(R.drawable.retirement_background);
                break;
            case "NOTHING":
                cellView.setBackgroundResource(R.drawable.cell_nothing_background);
                break;
            case "TELEPORT":
                cellView.setBackgroundResource(R.drawable.cell_teleport_background);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + cellDTO.getType());
        }
    }

    private void makeOverlayVisible() {
        GameActivity gameActivity = (GameActivity) getActivity();
        if (gameActivity != null) {
            gameActivity.setFragmentVisibility();
        }
    }

    private void updatePlayerDot(int row, int col, int playerNumber) {
        Log.d(TAG, "updatePlayerUI started");

        // Find the GridLayout in the layout
        GridLayout gridLayout = requireView().findViewById(R.id.gridLayout2);

        // Calculate the index of the cell in the GridLayout
        int index = row * gridLayout.getColumnCount() + col;

        // Check if the index is within the bounds of the GridLayout
        if (index >= 0 && index < gridLayout.getChildCount()) {
            // Get the PlayerCellView representing the cell
            PlayerCellView cellView = (PlayerCellView) gridLayout.getChildAt(index);

            // Determine which player's dot to update based on playerNumber
            switch (playerNumber) {
                case 1:
                    cellView.setPlayer1Dot(R.drawable.player1_dot);
                    break;
                case 2:
                    cellView.setPlayer2Dot(R.drawable.player2_dot);
                    break;
                case 3:
                    cellView.setPlayer3Dot(R.drawable.player3_dot);
                    break;
                case 4:
                    cellView.setPlayer4Dot(R.drawable.player4_dot);
                    break;
                default:
                    Log.e(TAG, "Invalid player number: " + playerNumber);
                    break;
            }

        }
    }

    private void clearPlayerDot(int row, int col, int playerNumber) {

        // Find the GridLayout in the layout
        GridLayout gridLayout = requireView().findViewById(R.id.gridLayout2);

        // Calculate the index of the cell in the GridLayout
        int index = row * gridLayout.getColumnCount() + col;

        // Check if the index is within the bounds of the GridLayout
        if (index >= 0 && index < gridLayout.getChildCount()) {
            // Get the PlayerCellView representing the cell
            PlayerCellView cellView = (PlayerCellView) gridLayout.getChildAt(index);

            // Determine which player's dot to update based on playerNumber
            switch (playerNumber) {
                case 1:
                    cellView.clearPlayer1Dot();
                    break;
                case 2:
                    cellView.clearPlayer2Dot();
                    break;
                case 3:
                    cellView.clearPlayer3Dot();
                    break;
                case 4:
                    cellView.clearPlayer4Dot();
                    break;
                default:
                    Log.e(TAG, "Invalid player number: " + playerNumber);
                    break;
            }

        }
    }

    private void updatePlayerDots(List<PlayerDTO> playerDTOS) {
        if (playerDTOS != null) {
            for (int i = 0; i < playerDTOS.size(); i++) {
                CellDTO cell = gameViewModel.getCellDTOHashMap().get(playerDTOS.get(i).getCurrentCellPosition());
                if (cell != null) {
                    updatePlayerDot(cell.getRow(), cell.getCol(), i + 1);
                }
            }
        } else {
            Log.e(TAG, "New player list was null");
        }
    }

    private void deletePlayerDots(List<PlayerDTO> oldPlayers) {
        if (oldPlayers != null) {
            for (int i = 0; i < oldPlayers.size(); i++) {
                PlayerDTO oldPlayer = oldPlayers.get(i);
                if (oldPlayer != null) {
                    CellDTO oldCell = gameViewModel.getCellDTOHashMap().get(oldPlayer.getCurrentCellPosition());
                    if (oldCell != null) {
                        clearPlayerDot(oldCell.getRow(), oldCell.getCol(), i + 1);
                    } else {
                        Log.e(TAG, "Old cell was null for player: " + oldPlayer.getPlayerName() + " at position: " + oldPlayer.getCurrentCellPosition());
                    }
                } else {
                    Log.e(TAG, "Old player at index " + i + " is null");
                }
            }
        } else {
            Log.e(TAG, "Old player list was null");
        }
    }


}