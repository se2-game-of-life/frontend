package se2.group3.gameoflife.frontend;

public class Client extends Thread{

    private String serverName;
    private int serverPort;
    private String serverResponse;
    private String clientRequest;

    public Client (String clientRequest){
        this.clientRequest = clientRequest;
    }

    @Override
    public void run() {
/*        try {
            Socket socket = new Socket(serverName, serverPort);
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.println(clientRequest);

            serverResponse = input.readLine();

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



    public String getServerResponse(){
        return serverResponse;
    }


}
