import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel {

    Player player;
    Timer gameTimer;
    ArrayList<Platform> platforms = new ArrayList<>();


    public GamePanel() throws InterruptedException {
        player = new Player(400, 300, this);

        generatePlatforms();
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Zet de player posities
                player.set();
                // Teken op de game panel
                repaint();
            }
            // We gaan voor 60 fps. met 1000 ms 1000/ 60 = 16.666 ~= 17
        }, 0, 17);
    }

    private void generatePlatforms() {
//        for (int i = 50; i < 650; i += 50) {
//            platforms.add(new Platform(i, 600, 50, 50));
//        }

        platforms.add(new Platform(50, 600, 600, 50));
        // Linker kant omhoog voor de kooi kooi
        platforms.add(new Platform(50, 450, 50, 150));
        // Rechter kant omhoog voor de kooi
        platforms.add(new Platform(600, 450, 50, 150));

        platforms.add(new Platform(650, 350, 140, 50));

    }

    public void paint(Graphics g) {
        // Dit doe je, zodat hij altijd over je frames heen "paint" en je geen "flicker" krijgt.
        super.paint(g);

        Graphics2D gtd = (Graphics2D) g;

        player.draw(gtd);
        for (Platform plat : platforms) plat.draw(gtd);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            player.keyUp = true;
        }
        if (e.getKeyChar() == 'a') {
            player.keyLeft = true;
        }
        if (e.getKeyChar() == 's') {
            player.keyDown = true;
        }
        if (e.getKeyChar() == 'd') {
            player.keyRight = true;
        }
        if (e.getKeyChar() == 'r') {
            player.keyRestart = true;
        }

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            player.keyUp = false;
        }
        if (e.getKeyChar() == 'a') {
            player.keyLeft = false;
        }
        if (e.getKeyChar() == 's') {
            player.keyDown = false;
        }
        if (e.getKeyChar() == 'd') {
            player.keyRight = false;
        }
        if (e.getKeyChar() == 'r') {
            player.keyRestart = false;
        }
    }
}
