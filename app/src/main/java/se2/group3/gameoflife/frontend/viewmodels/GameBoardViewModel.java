package se2.group3.gameoflife.frontend.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class GameBoardViewModel extends ViewModel {
    private static final String TAG = "GameBoardViewModel";

    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // LiveData to hold the entire game state
    private final MutableLiveData<LobbyDTO> lobbyState = new MutableLiveData<>();
    // LiveData to hold the spin result extracted from the lobby state
    private final MutableLiveData<Integer> spinResult = new MutableLiveData<>();

    // Method to spin the wheel
    public void spinWheel() {
        disposables.add(websocketClient.subscribe("/topic/game/", LobbyDTO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lobbyDTO -> {
                            lobbyState.setValue(lobbyDTO);
                            // Extract the spin result from the lobby state
                            spinResult.setValue(lobbyDTO.getSpunNumber());
                        },
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

    // Getter method for the entire lobby state
    public MutableLiveData<LobbyDTO> getLobbyState() {
        return lobbyState;
    }


    // Getter method for error message
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Clean up disposables
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}