package by.bstu;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerSideSocket {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");;
    private final File serverDir = new File("/Users/fas/Desktop/MacBook/Git/univer/src/recourses");

    public void run() {
        try {
            int serverPort = 4020;
            ServerSocket serverSocket = new ServerSocket(serverPort);
            File file;
            //serverSocket.setSoTimeout(100000);
            while(true) {
                System.out.println(LocalDateTime.now().format(formatter) + "[Server]:Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println(LocalDateTime.now().format(formatter) + "[Server]:User connected to " +
                        server.getRemoteSocketAddress());
                PrintWriter toClient =
                        new PrintWriter(server.getOutputStream(),true);
                BufferedReader fromClient =
                        new BufferedReader(
                                new InputStreamReader(server.getInputStream()));
                while (true) {
                    String line = fromClient.readLine();
                    System.out.println(LocalDateTime.now().format(formatter) + "[Server]:User enter line:" + line);
                    if (!validateFile(line)) {
                        toClient.println(LocalDateTime.now().format(formatter) + "[Server]:Validation error. Enter New file:");
                        continue;
                    }
                    toClient.println("Validate OK");
                    String strFromClient = fromClient.readLine();
                    if (strFromClient.equals("OK")) {
                        String[] arr = line.split(" ");
                        file = findFile(serverDir, arr[1]);
                    if (file == null) {
                        System.out.println(LocalDateTime.now().format(formatter) + "[Server]:Can't find the file. Server closed.");
                        toClient.println("Server closed.");
                        toClient.close();
                        fromClient.close();
                        serverSocket.close();
                        return;
                    }
                        Path filePath = Path.of(file.getPath());
                        String gg = Files.readAllLines(filePath).toString();
                        System.out.println(LocalDateTime.now().format(formatter) + "[Server]: server closed.");
                        toClient.println(gg);
                        toClient.close();
                        fromClient.close();
                        serverSocket.close();
                        return;
                    }
                    break;
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