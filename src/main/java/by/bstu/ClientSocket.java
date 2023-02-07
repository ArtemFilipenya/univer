package by.bstu;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class ClientSocket {
    private final Scanner scanner = new Scanner(System.in);
    public void run() {
        try {
            int serverPort = 4020;
            boolean isGoodInformationFromClient = false;
            InetAddress host = InetAddress.getByName("localhost");
            System.out.println("Connecting to server on port " + serverPort);

            Socket socket = new Socket(host,serverPort);
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
            PrintWriter toServer =
                    new PrintWriter(socket.getOutputStream(),true);
            BufferedReader fromServer =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            while (!isGoodInformationFromClient) {
                String clientStr = scanner.nextLine();
                toServer.println(clientStr);
                String strFromServer = fromServer.readLine();
                if (strFromServer.equals("OK")) {
                    isGoodInformationFromClient = true;
                }
                System.out.println(strFromServer);
            }
            toServer.close();
            fromServer.close();
            socket.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void checkFile() {

    }


    public static void main(String[] args) {
        ClientSocket client = new ClientSocket();
        client.run();
    }
}
