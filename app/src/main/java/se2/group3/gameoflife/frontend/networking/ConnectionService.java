package se2.group3.gameoflife.frontend.networking;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ConnectionService extends Service {
    private static final String TAG = "ConnectionService";
    private final StompClient stompClient;
    private final ObjectMapper objectMapper;
    private final HashMap<String, Subscription<?>> subscriptions;

    private final MutableLiveData<String> uuidLiveData;

    public ConnectionService(String url) {
        this.stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        this.subscriptions = new HashMap<>();
        this.uuidLiveData = new MutableLiveData<>();
        this.objectMapper = new ObjectMapper();
        Log.d(TAG, "STOMP client initialized!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        subscriptions.clear();
        this.uuidLiveData.setValue(UUID.randomUUID().toString()); //create new uuid for identification

        stompClient.connect();

        //send uuid for client mapping in the backend
        Disposable identifierDisposable = send("/app/setIdentifier", uuidLiveData.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d(TAG, "Exchanged Player UUID with Backend: " + uuidLiveData.getValue())
                );
        identifierDisposable.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stompClient.disconnect();
        Log.d(TAG, "STOMP client disconnected!");
    }

    // SUBSCRIBE AND SEND METHODS

    public Completable unsubscribe(String topic) {
        return Completable.create(emitter -> {
           if(!subscriptions.containsKey(topic)) {
               emitter.onComplete();
               return;
           }

           Subscription<?> subscription = subscriptions.get(topic);
           if(subscription == null) {
               Log.e(TAG, "NullPointerException during HashMapAccess for topic: " + topic);
               emitter.onError(new NullPointerException());
               return;
           }

           subscription.getDisposable().dispose();
           subscriptions.remove(topic);

           //todo: add more checks for null etc
        });
    }

    public  <T> Completable subscribe(String topic, Class<T> type) {
        return Completable.create(emitter -> {
            if(subscriptions.containsKey(topic)) {
                //todo: if we ever want to re-subscribe we need to check if the disposable in the subscription is not disposed
                emitter.onComplete();
                return;
            }
            MutableLiveData<T> liveDataObject = new MutableLiveData<>();
            Disposable disposable = stompClient.topic(topic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stompMessage -> {
                        try {
                            Log.d(TAG, "Rx: " + stompMessage.getPayload());
                            liveDataObject.setValue(type.cast(toObject(stompMessage.getPayload(), type)));
                        } catch (ClassCastException | JsonProcessingException e) {
                            Log.e(TAG, "Error Deserializing Data: " + e);
                            emitter.onError(e);
                        }
                    }, errorMessage -> {
                        Log.e(TAG, "Error Subscribing to Topic: " + errorMessage.toString());
                        emitter.onError(errorMessage);
                    });
            Subscription<T> newSubscription = new Subscription<>(disposable, liveDataObject);
            this.subscriptions.put(topic, newSubscription);
            emitter.onComplete();
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
                                    Log.d(TAG, "Tx: " + message);
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

    // HELPER METHODS FOR SERIALIZATION AND IDENTIFICATION

    private String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> Object toObject(String message, Class<T> messageType) throws JsonProcessingException {
        return objectMapper.readValue(message, messageType);
    }

    // DO NOT ADD ANYTHING AFTER THIS COMMENT

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class ConnectionServiceBinder extends Binder {
        ConnectionService getService() {
            return ConnectionService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ConnectionServiceBinder();
    }

}
