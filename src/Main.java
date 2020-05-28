import java.io.IOException;

public class Main {


    private static MainFrame mainFrame;
    static Server s = new Server();


    public static void main(String[] args) {
        // Opent mainFrame op 800 breedte en 1000 hoogte.
        mainFrame = new MainFrame(800,1000);
        mainFrame.openFrame();
        try {
            s.Run(1,100);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
