package se2.group3.gameoflife.frontend.networking;

public interface WebSocketMessageHandler<T> {

    void onMessageReceived(T message);

}