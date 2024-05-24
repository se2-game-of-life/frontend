package se2.group3.gameoflife.frontend.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class MakeDecisionsViewModel extends ViewModel {
    private final WebsocketClient websocketClient = WebsocketClient.getInstance();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> choiceResult = new MutableLiveData<>();

    // Method to make a choice
    public void makeChoice(boolean chooseLeft, String uuid) {
        String choiceData = createChoiceData(chooseLeft, uuid);

        disposables.add(websocketClient.send("/app/makeChoice", choiceData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    choiceResult.setValue(true); // Assuming true indicates success
                }, error -> {
                    errorMessage.setValue(error.getMessage());
                    choiceResult.setValue(false); // Assuming false indicates failure
                })
        );
    }

    private String createChoiceData(boolean chooseLeft, String uuid) {
        return "{\"chooseLeft\":" + chooseLeft + ",\"uuid\":\"" + uuid + "\"}";
    }

    // Getter method for choice result
    public MutableLiveData<Boolean> getChoiceResult() {
        return choiceResult;
    }

    // Getter method for error message
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Clean up disposables
    public void dispose() {
        disposables.dispose();
    }
}
