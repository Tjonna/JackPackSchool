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
    public static String status;


    public void Run(int cycle,int jetPackFuel) throws IOException {
        amount = Integer.toString(round(jetPackFuel/1.5));
        if (cycle == 1) {
            //als cycle 1 is wordt de setup van de server gedraaid
            Server = new ServerSocket(5000);
            //poort 5000 wordt geopend
            System.out.println("TCPServer Waiting for client on port 5000");
            //loop draait op de achtergrond totdat de controller connected is
            while (true) {
                Socket connected = Server.accept();
                //accepteerd connectie
                System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");
                // zorgt dat voortaan de andere loop gedraaid zal worden
                cyclenr = 2;
                try {
                    pw = new PrintWriter(connected.getOutputStream());
                } catch (NullPointerException e) {
                    System.out.println("iets verkeerd met de connectie");
                }
                inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
                try {
                    fromclient = inFromClient.readLine();
                    System.out.println("RECIEVED:" + fromclient);
                    pw.write("100;1");
                    pw.flush();
                    //voorkomt haperingen omdat dit het begin van de connecite is
                    Thread.sleep(300);
                } catch (NullPointerException | InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("reeee");
                }


            }
        } else{
            try {
                //leest data via tcp uit
                fromclient = inFromClient.readLine();
                String[] parts = fromclient.split(";");
                System.out.println(amount);
                pw.write(amount+";"+status);
                //stuurt de geschreven data ook daadwerkelijk
                pw.flush();
                status = "1";
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("geen connectie");
            }
        }
}
}