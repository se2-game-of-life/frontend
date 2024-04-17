package se2.group3.gameoflife.frontend.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.dto.JoinLobbyRequest;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class LobbyViewModel extends ViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();

    private final MutableLiveData<LobbyDTO> lobbyDTO = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    public LiveData<LobbyDTO> getLobby() {
        return lobbyDTO;
    }

    public void createLobby(PlayerDTO player, String uuid) {

        disposables.add(websocketClient.subscribe("/topic/lobbies/" + uuid, LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        disposables.add(websocketClient.send("/app/lobby/create", player)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, error -> errorMessage.setValue(error.getMessage()))
        );
    }

    public void joinLobby(long lobbyID, PlayerDTO player) {

        disposables.add(websocketClient.subscribe("/topic/lobbies/" + lobbyID, LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        disposables.add(websocketClient.send("/app/lobby/join", new JoinLobbyRequest(lobbyID, player))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, error -> errorMessage.setValue(error.getMessage()))
        );
    }

    public void dispose() {
        disposables.dispose();
    }
}
