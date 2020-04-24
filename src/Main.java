public class Main {

    private static MainFrame mainFrame;

    static {
        try {
            mainFrame = new MainFrame(800,1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        mainFrame.openFrame();
    }
}
