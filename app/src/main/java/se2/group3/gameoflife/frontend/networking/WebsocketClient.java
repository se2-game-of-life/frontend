package se2.group3.gameoflife.frontend.networking;


import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebsocketClient {

    private static volatile WebsocketClient INSTANCE = null;
    private final StompClient stompClient;
    private final ObjectMapper objectMapper;

    private WebsocketClient(String url) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        objectMapper = new ObjectMapper();
        Log.d(TAG, "STOMP client created!");
    }

    public static WebsocketClient getInstance(String url) {
        if (INSTANCE == null) {
            synchronized (WebsocketClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WebsocketClient(url);
                }
            }
        }
        return INSTANCE;
    }

    public static WebsocketClient getInstance() {
        return INSTANCE;
    }

    public Completable disconnect() {
        return Completable.create(emitter -> {
            stompClient.disconnect();
            emitter.onComplete();
        });
    }

    public Completable connect(String uuid) {
        return Completable.create(emitter -> {
            stompClient.connect();

            Disposable dis = send("/app/setIdentifier", uuid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.d(TAG, "Send message: " + uuid)
                    );
            emitter.onComplete();
        });
    }

    public <T> Observable<T> subscribe(String topic, Class<T> type) {
        return Observable.create(emitter -> {
            Disposable disposable = stompClient.topic(topic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stompMessage -> {
                        try {
                            T message = type.cast(toObject(stompMessage.getPayload(), type));
                            Log.d(TAG, "Received Something!: " + stompMessage.getPayload());
                            emitter.onNext(message);
                        } catch (ClassCastException | JsonProcessingException e) {
                            emitter.onError(e);
                        }
                    }, emitter::onError);
            emitter.setCancellable(disposable::dispose);
        });
    }
 
    public Completable send(String destination, Object payload) {
        return Completable.create(emitter -> {
            try {
                String message = toJsonString(payload);
                Disposable disposable = stompClient.send(destination, message)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    Log.d(TAG, "Message send: " + message);
                                    emitter.onComplete();
                                },
                                emitter::onError
                        );
                emitter.setCancellable(disposable::dispose);
            } catch (JsonProcessingException e) {
                emitter.onError(e);
            }
        });
    }

    private String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> Object toObject(String message, Class<T> messageType) throws JsonProcessingException {
        return objectMapper.readValue(message, messageType);
    }
}
