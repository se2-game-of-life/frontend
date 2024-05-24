package se2.group3.gameoflife.frontend.viewmodels;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import se2.group3.gameoflife.frontend.networking.WebsocketClient;

public class SpinWheelViewModel extends ViewModel {
    private static final String URI = "ws://10.0.2.2:8080/gameoflife";
    private final CompositeDisposable disposables = new CompositeDisposable();

    private WebsocketClient websocketClient;

    public void connectToServer() {
        websocketClient = WebsocketClient.getInstance(URI);
        disposables.add(websocketClient.connect(URI) // Pass URI here
                .subscribe(
                        () -> System.out.println("Connected to server!"),
                        throwable -> {
                            System.err.println("Error connecting to server: " + throwable.getMessage());
                            throwable.printStackTrace();
                        }
                ));
    }

    public void dispose() {
        disposables.dispose();
    }

    public WebsocketClient getWebsocketClient() {
        return websocketClient;
    }
}


