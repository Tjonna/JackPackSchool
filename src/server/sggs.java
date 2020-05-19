package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class sggs {
    static Socket s;
    static PrintWriter pw;


    public static void main(String[] args) throws IOException {
        String fromclient;
        ServerSocket Server = new ServerSocket(5000);
        System.out.println("TCPServer Waiting for client on port 5000");

        while (true) {
            Socket connected = Server.accept();
            System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");
            try {
                pw = new PrintWriter(connected.getOutputStream());
            }
            catch(NullPointerException e){
                System.out.println("but why");
            }
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));

            while (true) {
                try {
                    fromclient = inFromClient.readLine();

                    if (fromclient.equals("q") || fromclient.equals("Q")) {
                        break;
                    } else {
                        System.out.println("RECIEVED:" + fromclient);
                        pw.write("40;1");
                        pw.flush();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("reeee");
                }
            }
        }
    }
}
