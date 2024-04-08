package se2.group3.gameoflife.frontend.networking;

public interface ResponseHandler {
    void handleMessage(String msg);
    void handleError();
}
