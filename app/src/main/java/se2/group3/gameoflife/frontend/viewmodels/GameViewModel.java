package se2.group3.gameoflife.frontend.viewmodels;



import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class GameViewModel extends ViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MutableLiveData<LobbyDTO> lobbyDTO = new MutableLiveData<>();

    public void makeChoice(boolean chooseLeft){
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
        disposables.add(websocketClient.subscribe("/topic/game/" + lobbyDTO.getValue().getLobbyID(), LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        disposables.add(websocketClient.send("/app/game/spin", "")
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

}
