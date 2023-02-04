package by.bstu;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class ClientSocket {
    private final Scanner scanner = new Scanner(System.in);
    public void run() {
        try {

            int serverPort = 4020;
            InetAddress host = InetAddress.getByName("localhost");
            System.out.println("Connecting to server on port " + serverPort);


            Socket socket = new Socket(host,serverPort);
            //Socket socket = new Socket("127.0.0.1", serverPort);
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
            PrintWriter toServer =
                    new PrintWriter(socket.getOutputStream(),true);
            BufferedReader fromServer =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            String str = scanner.nextLine();
            toServer.println(str);

            String line = fromServer.readLine();
            System.out.println("Client received: " + line + " from Server");

            //writeFileName();
            //readUserCommand();

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
