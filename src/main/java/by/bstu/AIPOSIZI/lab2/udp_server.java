package by.bstu.AIPOSIZI.lab2;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class udp_server {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");;
    private static final File serverDir = new File("/Users/fas/Desktop/MacBook/Git/univer/src/recourses");

    public static void main(String args[]) {
        DatagramSocket sock = null;
        File file;

        try {
            //1. creating a server socket, parameter is local port number
            sock = new DatagramSocket(7777);

            //buffer to receive incoming data
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            //2. Wait for an incoming data
            System.out.println("[Server]:Server socket created. Waiting...");

            //communication loop
            while(true)
            {
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());

                if(!validateFile(s)) {
                    String smsForUser = "Error.";
                    sock.send(new DatagramPacket(smsForUser.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort()));
                }
                String[] massivData = s.split(" ");
                file = findFile(serverDir, massivData[1]);
                Path filePath = Path.of(file.getPath());
                String gg = Files.readAllLines(filePath).toString();
                //echo the details of incoming data - client ip : client port - client message
                System.out.println("[Server]: HostAddress:" + incoming.getAddress().getHostAddress() + ". Port: " +
                        incoming.getPort() + ". Message: " + gg);

                s = "OK : " + s;
                DatagramPacket dp = new DatagramPacket(gg.getBytes() , gg.getBytes().length , incoming.getAddress() , incoming.getPort());
                sock.send(dp);
            }
        }

        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }

    public static boolean validateFile(String str) {
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

    //simple function to echo data to terminal
//    public static void echo(String msg)
//    {
//        System.out.println(msg);
//    }
}