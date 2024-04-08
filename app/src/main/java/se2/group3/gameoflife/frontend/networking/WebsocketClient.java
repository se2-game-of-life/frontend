package se2.group3.gameoflife.frontend.networking;


import static se2.group3.gameoflife.frontend.activities.MainActivity.uuid;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.util.SerializationUtil;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebsocketClient {

    private final StompClient stompClient;
    private static final String TAG = "Networking";

    public WebsocketClient(String url) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        Log.d(TAG, "STOMP client created!");
    }



    public void disconnect() {
        stompClient.disconnect();
        Log.d(TAG, "STOMP client disconnected!");
    }

    public void connect() {
        stompClient.connect();
        Log.d(TAG, "STOMP client connected!");

        try {
            send("/app/setIdentifier", uuid);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Disposable subscribe(String topic, ResponseHandler handler) {
        return stompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    handler.handleMessage(stompMessage.getPayload());
                    Log.d(TAG, "Received: " + stompMessage.getPayload());
                }, throwable -> {
                    handler.handleError();
                    Log.e(TAG, "Error on subscribe: ", throwable);
                });
    }
 
    public Disposable send(String destination, Object payload) throws JsonProcessingException {
        return stompClient.send(destination, SerializationUtil.toJsonString(payload))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "Message Sent: " + SerializationUtil.toJsonString(payload));
                }, throwable -> {
                    Log.e(TAG, "Error sending STOMP message: ", throwable);
                });
    }
}
