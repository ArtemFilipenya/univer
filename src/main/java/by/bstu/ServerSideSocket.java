package by.bstu;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class ServerSideSocket {
    private final Scanner scanner = new Scanner(System.in);
    private final File serverDir = new File("/Users/fas/Desktop/MacBook/Git/univer/src/recourses");
    public void run() {
        try {
            int serverPort = 4020;
            boolean isGoodInfoFromClient = false;
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
                while (!isGoodInfoFromClient) {
                    String line = fromClient.readLine();
                    if (!validateFile(line)) {
                        toClient.println("Error.Enter New file:");
                        continue;
                    }
                    String[] arr = line.split(" ");
                    if (findFile(serverDir, arr[1]) == null) {
                        toClient.println("Can't find the file. Enter New file:");
                    }
                    isGoodInfoFromClient = true;
                }
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean validateFile(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        String[] arr = str.split(" ");
        return arr.length == 2 && arr[0].equals("load") && arr[1].contains(".txt");
    }

    private static File findFile(File dir, String name) {
        File result = null; // no need to store result as String, you're returning File anyway
        File[] dirlist  = dir.listFiles();

        for (File file : dirlist) {
            if (file.isDirectory()) {
                result = findFile(file, name);
                if (result != null) break; // recursive call found the file; terminate the loop
            } else if (file.getName().matches(name)) {
                return file; // found the file; return it
            }
        }
        return result; // will return null if we didn't find anything
    }

    public static void main(String[] args) {
        ServerSideSocket srv = new ServerSideSocket();
        srv.run();
    }
}