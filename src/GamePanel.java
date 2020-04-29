import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel {

    Player player;
    Timer gameTimer;
    ArrayList<Platform> platforms = new ArrayList<>();

    int cameraY;
    int cameraYspeed;


    public GamePanel() throws InterruptedException {
        player = new Player(400, 300, this);

        resetGame();
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Zet de player posities
                player.set();
                for (Platform plat : platforms) plat.set(cameraY);
                // Teken op de game panel
                repaint();
            }
            // We gaan voor 60 fps. met 1000 ms 1000/ 60 = 16.666 ~= 17
        }, 0, 17);
    }

    public void resetGame() {
        player.x = 400;
        player.y = 300;
        player.xspeed = 0;
        player.flyspeed = 0;
        cameraY = 150;
        cameraYspeed = 1;
        platforms.clear();

        generatePlatforms(50);
    }

    private void generatePlatforms(int offset) {
        int s = 50;
        Random rand = new Random();
        int index = rand.nextInt(1);

        if (index == 0) {
            for (int i = 0; i < 16; i++) platforms.add(new Platform( i * 50, 800, 50, 50, player));
        }

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
