import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private GamePanel gamePanel = new GamePanel();

    public MainFrame(int breedte, int hoogte) throws InterruptedException {

        // Zodra de game gesloten wordt door op het kruisje te drukken, gaat t script ook uit, die blijft dus niet door runnen als de JFrame weg is
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Hier zet in de panel (!!!!!!!!)
        // Zorgen dat gebruikers niet het hele frame uitmekaar trekken en de scaling vernielen
        setResizable(false);
        // Grootte van window zetten
        setSize(breedte, hoogte);
        // Zet de titel / naam van de game
        setTitle("JackPack");
        // Neem even de scherm grootte van je scherm
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //  Gebruik dit om de game netjes in het midden van je scherm te laten starten
        setLocation((int) (screenSize.getWidth() / 2 - getWidth() / 2), (int) (screenSize.getHeight() / 2 - getHeight() / 2));
        // pack();

        // Paneel locatie is het zelfde als de frame.
        gamePanel.setLocation(0, 0);
        // Gebruik dezelfde size als die van de hoofd frame
        gamePanel.setSize(getSize());
        // Toevoegen van game paneel
        add(gamePanel);
        gamePanel.setBackground(Color.GRAY);
        gamePanel.setVisible(true);
        addKeyListener(new KeyChecker(gamePanel));
    }


    public void openFrame() {
        setVisible(true);
    }

    public void closeFrame() {
        setVisible(false);
    }

}
