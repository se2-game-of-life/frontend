package se2.group3.gameoflife.frontend;

public class Client extends Thread{

    private String servername;
    private int port;
    private String response;
    private String request;

    public Client (String request){
        this.request = request;
    }


}
