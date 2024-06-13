package se2.group3.gameoflife.frontend.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.activities.GameActivity.VibrateCallback;
import se2.group3.gameoflife.frontend.dto.BoardDTO;
import se2.group3.gameoflife.frontend.dto.CellDTO;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class GameViewModel extends ViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MutableLiveData<LobbyDTO> lobbyDTO = new MutableLiveData<>();
    private HashMap<Integer, CellDTO> cellDTOHashMap = new HashMap<>();

    public void startGame(VibrateCallback callback) {
        LobbyDTO lobby = lobbyDTO.getValue();
        if(lobby == null) throw new RuntimeException("LobbyDTO NULL in GameViewModel!");
        disposables.add(websocketClient.subscribe("/topic/lobbies/" + lobby.getLobbyID(), LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        //subscribe to vibrate-events for the lobby
        disposables.add(websocketClient.subscribe("/topic/lobbies/" + lobby.getLobbyID() + "/vibrate", LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        value -> {
                            lobbyDTO.setValue(value);
                            callback.onCallback();
                        },
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        //return if the lobby has already started
        if(lobby.isHasStarted()) return;

        //start lobby if lobby not started already
        disposables.add(websocketClient.send("/app/lobby/start", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, error -> errorMessage.setValue(error.getMessage()))
        );
    }

    public void makeChoice(boolean chooseLeft){
        if(lobbyDTO == null || lobbyDTO.getValue() == null) {
            Log.e("Networking", "Error making choice: lobbyDTO is null!");
            return;
        }

        disposables.add(websocketClient.subscribe("/topic/lobbies/" + lobbyDTO.getValue().getLobbyID(), LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        Log.d("Networking", lobbyDTO.toString());

        disposables.add(websocketClient.send("/app/lobby/choice", chooseLeft)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, error -> errorMessage.setValue(error.getMessage()))
        );
    }

    public void spinWheel() {
        if(lobbyDTO.getValue() == null) {
            Log.e("Networking", "Error making choice: lobbyDTO was null!");
            return;
        }
        disposables.add(websocketClient.subscribe("/topic/lobbies/" + lobbyDTO.getValue().getLobbyID(), LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        disposables.add(websocketClient.send("/app/lobby/spin", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        error -> errorMessage.setValue(error.getMessage())
                )
        );
    }

    public void report(int player) {
        LobbyDTO lobby = lobbyDTO.getValue();
        if(lobby == null) {
            Log.e("Networking", "Report has failed: lobbyDTO is not initialized yet!");
            return;
        }
        String playerUUID = lobby.getPlayers().get(player).getPlayerUUID();

        disposables.add(websocketClient.subscribe("/topic/game/" + lobbyDTO.getValue().getLobbyID(), LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        disposables.add(websocketClient.send("/app/report", playerUUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        error -> errorMessage.setValue(error.getMessage())
                )
        );
    }

    public void cheat() {
        if(lobbyDTO.getValue() == null) {
            Log.e("Networking", "Error making choice: lobbyDTO was null!");
            return;
        }
        disposables.add(websocketClient.subscribe("/topic/game/" + lobbyDTO.getValue().getLobbyID(), LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        disposables.add(websocketClient.send("/app/cheat", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        error -> errorMessage.setValue(error.getMessage())
                )
        );
    }

    public void dispose() {
        disposables.dispose();
    }

    public void setLobbyDTO(LobbyDTO lobbyDTO){
        if(lobbyDTO != null){
            this.lobbyDTO = new MutableLiveData<>(lobbyDTO);

        } else{
            throw new IllegalArgumentException("LobbyDTO not found in the StartGameActivity");
        }
    }

    public LobbyDTO getLobbyDTO() {
        if(lobbyDTO != null && lobbyDTO.getValue() != null){
            return lobbyDTO.getValue();
        } else{
            throw new IllegalArgumentException("LobbyDTO is null");
        }
    }


    public LiveData<LobbyDTO> getLobby() {
        return lobbyDTO;
    }

    public LiveData<String> getErrorMessage(){ return errorMessage;}

    public HashMap<Integer, CellDTO> getCellDTOHashMap() {
        return cellDTOHashMap;
    }

    public void setCellDTOHashMap(BoardDTO boardDTO) {
        for (int row = 0; row < boardDTO.getCells().size(); row++) {
            for (int col = 0; col < boardDTO.getCells().get(row).size(); col++) {
                CellDTO cell = boardDTO.getCells().get(row).get(col);
                if(cell == null) continue;
                cellDTOHashMap.put(cell.getNumber(), cell);
            }
        }
    }
}
