package se2.group3.gameoflife.frontend.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class StartGameViewModel extends ViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();

    private final MutableLiveData<LobbyDTO> lobbyDTO = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    public LiveData<LobbyDTO> getLobby() {
        return lobbyDTO;
    }

    public void getLobbyUpdates(long lobbyID) {
        disposables.add(websocketClient.subscribe("/topic/lobbies/" + lobbyID, LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );
    }

    public void dispose() {
        disposables.dispose();
    }
}
