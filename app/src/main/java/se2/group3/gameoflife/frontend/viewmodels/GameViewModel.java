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

    public void startGame() {
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

        //return if the lobby has already started
        if(lobby.isHasStarted()) return;

        //start lobby if lobby not started already
        disposables.add(websocketClient.send("app/lobby/start")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, error -> errorMessage.setValue(error.getMessage()))
        );
    }

    public void makeChoice(boolean chooseLeft){
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
        disposables.add(websocketClient.subscribe("/topic/game/", LobbyDTO.class)
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
