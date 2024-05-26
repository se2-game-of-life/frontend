package se2.group3.gameoflife.frontend.viewmodels;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


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


    public void setLobbyDTO(LobbyDTO lobbyDTO){
        if(lobbyDTO == null){
            throw new IllegalArgumentException("LobbyDTO not found in the StartGameActivity");
        } else{
            this.lobbyDTO = new MutableLiveData<>(lobbyDTO);
        }
    }

    //    public void dispose() {
//        disposables.dispose();
//    }

    private MutableLiveData<LobbyDTO> lobbyDTO = new MutableLiveData<>();
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

}
