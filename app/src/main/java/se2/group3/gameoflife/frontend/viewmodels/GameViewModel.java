package se2.group3.gameoflife.frontend.viewmodels;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import io.reactivex.disposables.CompositeDisposable;


import se2.group3.gameoflife.frontend.dto.LobbyDTO;

import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class GameViewModel extends ViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

//    private MutableLiveData<PlayerDTO> playerDTO = new MutableLiveData<>();

//    public void setPlayerDTO(PlayerDTO playerDTO){
//        if(playerDTO == null){
//            throw new IllegalArgumentException("PlayerDTO is null");
//        } else{
//            this.playerDTO = new MutableLiveData<>(playerDTO);
//        }
//    }
//    public PlayerDTO getPlayerDTO(){
//        if(playerDTO == null || playerDTO.getValue() == null){
//            throw new IllegalArgumentException("PlayerDTO is null");
//        } else{
//            return playerDTO.getValue();
//        }
//    }

//    public void dispose() {
//        disposables.dispose();
//    }

    private MutableLiveData<LobbyDTO> lobbyDTO = new MutableLiveData<>();
    public void setLobbyDTO(LobbyDTO lobbyDTO){
        if(lobbyDTO == null){
            throw new IllegalArgumentException("LobbyDTO not found in the StartGameActivity");
        } else{
            this.lobbyDTO = new MutableLiveData<>(lobbyDTO);
        }
    }

    public LiveData<LobbyDTO> getLobby() {
        return lobbyDTO;
    }

}
