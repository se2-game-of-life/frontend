package se.group3.gameoflife.frontend.networking;

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
import io.reactivex.CompletableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ConnectionService extends Service {
    private static final String TAG = "ConnectionService";
    private StompClient stompClient;
    private ObjectMapper objectMapper;
    private HashMap<String, Disposable> subscriptionHashMap;
    private HashMap<Class<?>, MutableLiveData<?>> liveDataHashMap;
    private MutableLiveData<String> uuidLiveData;
    private CompositeDisposable compositeDisposable;

    @Override
    public void onCreate() {
        super.onCreate();
        compositeDisposable = new CompositeDisposable();

        this.stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://se2-demo.aau.at:53207/gameoflife");
        stompClient.connect();
        this.subscriptionHashMap = new HashMap<>();
        this.liveDataHashMap = new HashMap<>();
        this.uuidLiveData = new MutableLiveData<>();
        this.objectMapper = new ObjectMapper();
        Log.d(TAG, "STOMP client initialized!");

        this.uuidLiveData.setValue(UUID.randomUUID().toString()); //create new uuid for identification

        //send uuid for client mapping in the backend
        compositeDisposable.add(send("/app/setIdentifier", uuidLiveData.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "Exchanged Player UUID with Backend: " + uuidLiveData.getValue())));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stompClient.disconnect();
        compositeDisposable.dispose();
        Log.d(TAG, "STOMP client disconnected!");
    }

    // GETTER FOR LIVE DATA

    public MutableLiveData<String> getUuidLiveData() {
        return this.uuidLiveData;
    }

    /**
     * Use this method to get data from the lobby,
     * make sure to LiveDataObject.observe() instead of LiveDataObject.getValue() for UI.
     * @param dtoClass xxxxDTO.class, used as key for LiveDataHashMap
     * @return MutableLiveData[xxxxDTO] or null if not present or error
     * @param <T> Type of the DTO you wish to get.
     */
    @SuppressWarnings("unchecked") //this is safe because type is known @ runtime (credit: ChatGPT)
    public <T> MutableLiveData<T> getLiveData(Class<T> dtoClass) {
        if (liveDataHashMap == null) {
            Log.e(TAG, "liveDataHashMap is null");
            return null;
        }

        if (!liveDataHashMap.containsKey(dtoClass)) {
            Log.e(TAG, "No entry found in liveDataHashMap for class: " + dtoClass.getName());
            return null;
        }

        try {
            return (MutableLiveData<T>) liveDataHashMap.get(dtoClass);
        } catch (ClassCastException e) {
            Log.e(TAG, "Class cast exception while trying to get Live Data: " + e);
            return null;
        }
    }

    // SUBSCRIBE AND SEND METHODS

    public Completable unsubscribe(String topic) {
        return Completable.create(emitter -> {
           if(!subscriptionHashMap.containsKey(topic)) {
               emitter.onComplete();
               return;
           }

           Disposable subscription = subscriptionHashMap.get(topic);
           if(subscription == null) {
               Log.e(TAG, "NullPointerException during HashMapAccess for topic: " + topic);
               emitter.onError(new NullPointerException());
               return;
           }

           if(!subscription.isDisposed()) subscription.dispose();
           subscriptionHashMap.remove(topic);
        });
    }

    public  <T> Completable subscribe(String topic, Class<T> type) {
        return Completable.create(emitter -> {
            if(subscriptionHashMap.containsKey(topic)) {
                Disposable subscription = subscriptionHashMap.get(topic);
                if(subscription != null && !subscription.isDisposed()) {
                    emitter.onComplete();
                    return;
                }
                emitter.onError(new IllegalStateException());
            }

            MutableLiveData<T> newLiveDataObject = new MutableLiveData<>();

            Disposable disposable = stompClient.topic(topic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stompMessage -> handleDataFromSubscribe(type, emitter, stompMessage, newLiveDataObject), errorMessage -> {
                        Log.e(TAG, "Error Subscribing to Topic: " + errorMessage.toString());
                        emitter.onError(errorMessage);
                    });
            if(!liveDataHashMap.containsKey(type)) this.liveDataHashMap.put(type, newLiveDataObject);
            this.subscriptionHashMap.put(topic, disposable);
            emitter.onComplete();
        });
    }

    private <T> void handleDataFromSubscribe(Class<T> type, CompletableEmitter emitter, StompMessage stompMessage, MutableLiveData<T> newLiveDataObject) {
        try {
            Log.d(TAG, "Rx: " + stompMessage.getPayload());

            T value = type.cast(toObject(stompMessage.getPayload(), type));
            if (liveDataHashMap.containsKey(type)) {
                updateLiveDataObject(type, emitter, value);
            } else {
                newLiveDataObject.setValue(value);
            }
        } catch (ClassCastException | JsonProcessingException e) {
            Log.e(TAG, "Error Deserializing Data: " + e);
            emitter.onError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void updateLiveDataObject(Class<T> type, CompletableEmitter emitter, T value) {
        try {
            MutableLiveData<T> liveData = (MutableLiveData<T>) liveDataHashMap.get(type);
            assert liveData != null;
            liveData.setValue(value);
        } catch (ClassCastException e) {
            Log.e(TAG, "Error Casting!");
            emitter.onError(e);
        }
    }

    public Disposable subscribeEvent(String topic, VibrationCallback callback) {
        return stompClient.topic(topic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stompMessage -> callback.onCallback(),
                            errorMessage -> Log.e(TAG, "Error Subscribing to Topic: " + errorMessage.toString()));
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
                                }, error -> {
                                    Log.e(TAG, "Error subscribing to send: " + error.getMessage());
                                    emitter.onError(error);
                                }
                        );
                emitter.setCancellable(disposable::dispose);
            } catch (JsonProcessingException e) {
                Log.e(TAG, "Error processing JSON on send: " + e);
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
        public ConnectionService getService() {
            return ConnectionService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ConnectionServiceBinder();
    }

}
