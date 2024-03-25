package se2.group3.gameoflife.frontend.networking;


import android.util.Log;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebsocketClient {

    private final String URL = "ws://10.0.2.2:8080/gameoflife";
    private final StompClient stompClient;

    public WebsocketClient() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, URL);
        Log.d("custom", "client created");
    }

    public void disconnect() {
        stompClient.disconnect();
    }

    public void connect() {
        stompClient.connect();
        Log.d("custom", "connected");
        Disposable topicSubscription = stompClient.topic("/topic/test")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    // Handle received message
                    Log.d("custom", "Received: " + stompMessage.getPayload());
                }, throwable -> {
                    // Handle error
                    Log.e("custom", "Error on subscribe: ", throwable);
                });
    }

    public void send() {
        Disposable s = stompClient.send("/app/test", "Your message payload")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("custom", "STOMP message sent.");
                }, throwable -> {
                    Log.e("custom", "Error sending STOMP message: ", throwable);
                });
    }
}
