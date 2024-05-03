package se2.group3.gameoflife.frontend.viewmodels;

import androidx.lifecycle.MutableLiveData;

import io.reactivex.disposables.CompositeDisposable;

import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class GameViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public void choosePath(){
        //todo: communicate decision to server
    }

    public void dispose() {
        disposables.dispose();
    }
}
