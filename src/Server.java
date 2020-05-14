import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static pythagoras.d.MathUtil.round;

public class Server{
    public static ServerSocket Server;
    public static PrintWriter pw;
    public static int cyclenr;
    public static BufferedReader inFromClient;
    public String fromclient;
    public static String amount;


    public void Run(int cycle,int jetPackFuel) throws IOException {
        amount = Integer.toString(round(jetPackFuel/1.5));
        if (cycle == 1) {
            Server = new ServerSocket(5000);
            System.out.println("TCPServer Waiting for client on port 5000");
            while (true) {
                Socket connected = Server.accept();
                System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");
                cyclenr = 2;
                try {
                    pw = new PrintWriter(connected.getOutputStream());
                } catch (NullPointerException e) {
                    System.out.println("but why");
                }
                inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
                try {
                    fromclient = inFromClient.readLine();
                    System.out.println("RECIEVED:" + fromclient);
                    pw.write("40;1");
                    pw.flush();
                    System.out.println("RECIEVED:" + fromclient);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("reeee");
                }


            }
        } else{
            try {
                fromclient = inFromClient.readLine();
                String[] parts = fromclient.split(";");
                System.out.println(amount);
                System.out.println("RECIEVEDa:" + fromclient);
                pw.write(amount+";1");
                pw.flush();
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("reeee");
            }
        }
}
}