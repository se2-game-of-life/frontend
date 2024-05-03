package se2.group3.gameoflife.frontend.viewmodels;

import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class GameViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void choosePath(PlayerDTO playerDTO){
        MutableLiveData<PlayerDTO> player = new MutableLiveData<>(playerDTO);
        disposables.add(websocketClient.subscribe("/game/path", PlayerDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        player::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );
    }

    public void dispose() {
        disposables.dispose();
    }
}
