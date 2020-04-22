import javax.swing.*;

public class Gui extends JFrame {

    private GamePaneel mainPanel = new GamePaneel();
    private Wereld world = new Wereld();

    public Gui() {

        // Zodra de game gesloten wordt door op het kruisje te drukken, gaat t script ook uit, die blijft dus niet door runnen als de JFrame weg is
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Hier zet in de panel (!!!!!!!!)
        setContentPane(mainPanel);
        // Zorgen dat gebruikers niet het hele frame uitmekaar trekken en de scaling vernielen
        setResizable(false);
        pack();
    }




    public void openGui() {
        setVisible(true);
    }

    public void closeGui() {
        setVisible(false);
    }

}
