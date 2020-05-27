import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// Gemaakt door Julian
public class Server{
     static ServerSocket Server;
     static PrintWriter pw;
     static int cyclenr;
     static BufferedReader inFromClient;
     String fromclient;
     static String amount;


    void Run(int cycle, int jetPackFuel) throws IOException {
        amount = Integer.toString((int)(jetPackFuel/1.5));
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
                    Thread.sleep(300);
                } catch (NullPointerException | InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("reeee");
                }


            }
        } else{
            try {
                fromclient = inFromClient.readLine();
                String[] parts = fromclient.split(";");
                System.out.println(amount);
                System.out.println("RECIEVED:" + fromclient);
                pw.write(amount+";1");
                pw.flush();
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("geen connectie");
            }
        }
}
}