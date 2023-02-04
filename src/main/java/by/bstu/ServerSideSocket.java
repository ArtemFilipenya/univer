package by.bstu;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ServerSideSocket {
    Scanner scanner = new Scanner(System.in);
    public void run() {
        try {
            int serverPort = 4020;
            ServerSocket serverSocket = new ServerSocket(serverPort);
            serverSocket.setSoTimeout(10000);
            while(true) {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                PrintWriter toClient =
                        new PrintWriter(server.getOutputStream(),true);
                BufferedReader fromClient =
                        new BufferedReader(
                                new InputStreamReader(server.getInputStream()));
                String line = fromClient.readLine();
                String[] arr = line.split(" ");
                String answer = arr[1];

                System.out.println("Server received: " + answer);
                toClient.println(answer);
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public String readUserCommand(String str) {
        String userInputData;
        String res = "";
        boolean isRightData = false;

        while (!isRightData) {
            System.out.println("Введите название файла:");
            userInputData = scanner.nextLine();
            if (userInputData == null || userInputData.isEmpty()) {
                System.out.println("Пусто");
                continue;
            }
            String[] arr = userInputData.split(" ");
            if (arr.length != 2 || !arr[0].equals("load")) {
                continue;
            }
            res = arr[1];
            isRightData = true;
        }
        return res;
    }

    public static void main(String[] args) {
        ServerSideSocket srv = new ServerSideSocket();
        srv.run();
    }
}
