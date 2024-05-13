package se2.group3.gameoflife.frontend.viewmodels;

import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.UUID;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

/**
 * Used for the business logic of the MainActivity
 */
public class MainViewModel extends ViewModel {

    private static final String URI = "ws://10.0.2.2:8080/gameoflife";
//    private static final String URI = "ws://se2-demo.aau.at:53207/gameoflife";
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    private String uuid = null;
    private String username = null;

    public void connectToServer() {
        WebsocketClient websocketClient = WebsocketClient.getInstance(URI);
        disposables.add(websocketClient.connect(getUUID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d(TAG, String.format("Connected to %s!", URI)),
                        error -> {
                            Log.e(TAG, String.format("Error connecting to %s!", URI));
                            throw new RuntimeException("Error connecting to server!");
                        })
        );
    }

    public String getUUID() {
        if(uuid == null) uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * method to check if username is valid
     * The username must consist of letters and can contain 0 or more digits at the end.
     * @return if the Username matches the necessary regex
     */
    public boolean checkUsername(){
        String usernameRegex = "^[a-zA-Z]+\\d*$";
        return Pattern.matches(usernameRegex, username);
    }

    public void dispose() {
        disposables.dispose();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
