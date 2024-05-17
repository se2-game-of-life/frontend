package se2.group3.gameoflife.frontend.viewmodels;

import android.util.Log;

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

    public void choosePath(boolean collegePath){
        disposables.add(websocketClient.subscribe("/topic/lobbies/" + lobbyDTO.getValue().getLobbyID(), LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        Log.d("Networking", lobbyDTO.toString());

        disposables.add(websocketClient.send("/app/lobby/choice", collegePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, error -> errorMessage.setValue(error.getMessage()))
        );
    }


    public void setLobbyDTO(LobbyDTO lobbyDTO){
        if(lobbyDTO == null){
            throw new IllegalArgumentException("LobbyDTO not found in the StartGameActivity");
        } else{
            this.lobbyDTO = new MutableLiveData<>(lobbyDTO);
        }
    }

    public LobbyDTO getLobbyDTO() {
        if(lobbyDTO == null || lobbyDTO.getValue() == null){
            throw new IllegalArgumentException("LobbyDTO is null");
        } else{
            return lobbyDTO.getValue();
        }
    }

//    public void dispose() {
//        disposables.dispose();
//    }
}
