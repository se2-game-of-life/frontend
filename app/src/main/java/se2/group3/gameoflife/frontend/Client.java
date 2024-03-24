package se2.group3.gameoflife.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{

    private String servername;
    private int port;
    private String response;
    private String request;

    public Client (String request){
        this.request = request;
    }

    @Override
    public void run() {
/*        try {
            Socket socket = new Socket(serverName, serverPort);
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.println(request);

            String message = input.readLine();

            input.close();
            output.close();
            socket.close();

        } catch (IOException io) {
            io.printStackTrace();
        } catch (UnknownHostException ue){
            ue.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }



    public String getResponse(){
        return response;
    }


}
