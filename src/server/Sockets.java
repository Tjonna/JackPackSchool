package server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.Random;
public class Sockets {
    static Socket s;
    static PrintWriter pw;

    public static void main(String[] args) {
        try {
            s = new Socket("raspberrypi", 8000);
            pw = new PrintWriter(s.getOutputStream());
            pw.write("80");
            pw.flush();
            pw.close();
            s.close();
        }
        catch (UnknownHostException e){
            System.out.println("Fail");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("Fail");
            e.printStackTrace();
        }
    }
}
