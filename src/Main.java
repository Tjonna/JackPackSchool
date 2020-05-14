import java.io.IOException;

public class Main {

    private static MainFrame mainFrame;
    static Server s = new Server();
    static {
        try {
            mainFrame = new MainFrame(800, 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        mainFrame.openFrame();
        try {
            s.Run(1,100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}