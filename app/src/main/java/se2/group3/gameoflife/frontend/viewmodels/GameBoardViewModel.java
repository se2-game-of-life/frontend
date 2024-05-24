package se2.group3.gameoflife.frontend.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class GameBoardViewModel extends ViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // Add any additional LiveData objects you might need
    private MutableLiveData<Integer> spinResult = new MutableLiveData<>();

    // Method to spin the wheel
    public void spinWheel() {
        disposables.add(websocketClient.subscribe("/topic/game/", Integer.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        spinResult::setValue,
                        error -> errorMessage.setValue(error.getMessage())
                )
        );

        disposables.add(websocketClient.send("/app/game/spin", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, error -> errorMessage.setValue(error.getMessage()))
        );
    }

    // Getter method for spin result
    public MutableLiveData<Integer> getSpinResult() {
        return spinResult;
    }

    // Clean up disposables
    public void dispose() {
        disposables.dispose();
    }
}