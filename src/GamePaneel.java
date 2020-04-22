import GameStatus.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePaneel extends JPanel implements Runnable, KeyListener {

    public static final int breedte = 300;
    public static final int hoogte = 400;
    public static final int schaal = 2;

    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    private Thread thread;
    private boolean running;

    // Afbeelding
    private BufferedImage image;
    private Graphics2D g;

    // Game State manager
    private GameStatusManager gsm;


    public GamePaneel() {
        super();
        setPreferredSize(new Dimension(breedte * schaal, hoogte * schaal));
        setFocusable(true);
    }

    // Zodra de game klaar is met laden, maak dan de thread en keylistener actief
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void initialiseer() {
        image = new BufferedImage(breedte, hoogte, BufferedImage.TYPE_INT_RGB);
        running = true;
        g = (Graphics2D) g;
        gsm = new GameStatusManager();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    gsm.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gsm.keyReleased(e.getKeyCode());
    }

    @Override
    public void run() {
        initialiseer();

        long startTime = System.nanoTime();
        long elapsedTime;
        long waitTime;
        while (running) {
            update();
            draw();
            drawToScreen();

            elapsedTime = System.nanoTime() - startTime;

            waitTime = targetTime - elapsedTime / 1000000;

            try {
                Thread.sleep(waitTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        gsm.update();
    }

    private void draw() {
        gsm.draw(g);
    }

    private void drawToScreen() {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }
}
