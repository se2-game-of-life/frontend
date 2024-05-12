package se2.group3.gameoflife.frontend.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class GameViewModel extends ViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<PlayerDTO> playerDTO = new MutableLiveData<>();

    public void choosePath(boolean collegePath){
        PlayerDTO player = getPlayerDTO();
        player.setCollegePath(collegePath);
        disposables.add(websocketClient.subscribe("/topic/game/", PlayerDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        playerDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );
        Log.d("Networking", playerDTO.toString());

        disposables.add(websocketClient.send("/app/game/path", collegePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, error -> errorMessage.setValue(error.getMessage()))
        );
    }

    public void setPlayerDTO(PlayerDTO playerDTO){
        if(playerDTO == null){
            throw new IllegalArgumentException("PlayerDTO is null");
        } else{
            this.playerDTO = new MutableLiveData<>(playerDTO);
        }
    }
    public PlayerDTO getPlayerDTO(){
        if(playerDTO == null || playerDTO.getValue() == null){
            throw new IllegalArgumentException("PlayerDTO is null");
        } else{
            return playerDTO.getValue();
        }
    }

    public void dispose() {
        disposables.dispose();
    }
}
