import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {

    Player player;
    Timer gameTimer;
    ArrayList<Platform> platforms = new ArrayList<>();
    ArrayList<Fuel> fuels = new ArrayList<>();

    int cameraY;
    int cameraYspeed;
    int generateTerrainTreshold = 1600;
    int offset;

    long startTimer, currentTimer;


    public GamePanel() throws InterruptedException {
        player = new Player(400, 150, this);

        resetGame();
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Dit is de game loop
                // Dit is voor de game generation, gebaseerd op vorige platform hoogte en vorig camera hoogte
                if (cameraY - platforms.get(platforms.size() - 1).y <= generateTerrainTreshold) {
                    offset -= 400;
                    System.out.println(platforms.size());
                    generatePlatforms(offset);
                    generateTerrainTreshold -= 100;
                }
                /* Aangezien de camera sneller gaat dan het poppetje en de scherm, wil ik het getal evenwijdig aan mekaar laten gaan. Ook al kan ik er geen
                 goede wiskundige berekening er voor vinden, heb ik maar een lineaire vergelijking gedaan die altijd op X: 900 van het scherm blijft.
                (int) (cameraY / -1000000000) + 990;
                 Alleen kwam ik er later achter dat het poppetje en de deathLine nooit echt in de ... - 100 zone komt
                player.deathRange = 900;

                 Daardoor is deathRange overbodig en heb ik gewoon y >= 900  || y <= 250 ingevuld

                 */
                // Laat de camera verticaal bewegen
                cameraY -= cameraYspeed;
                // cameraYspeed = (int) ((cameraYspeed * (currentTimer / 1000))) + 1;
                // De tijd in milliseconde bijhouden
                currentTimer = System.currentTimeMillis() - startTimer;
                // Camera versnelling door de game heen
                // cameraYspeed = (int) (cameraYspeed * ((currentTimer / 10000) + 1));
                // Zet de player posities
                player.set();
                for (Platform plat : platforms) plat.set(cameraY);
                for (Fuel fuel : fuels) fuel.set(cameraY);
                // Teken op de game panel
                repaint();
            }
            // We gaan voor 60 fps. met 1000 ms 1000/ 60 = 16.666 ~= 17
        }, 0, 17);
    }

    public void resetGame() {
        player.x = 400;
        player.y = 150;
        player.xspeed = 0;
        player.flyspeed = 0;
        cameraY = -500;
        // Camera begin speed, wordt vermeerderd laatste tijd
        cameraYspeed = 1;
        // Even alles weghalen uit de arrayList , zodat er geen platformen nog  bestaan van vorige game
        platforms.clear();
        fuels.clear();
        // Begin platform
        platforms.add(new Platform(0, 200, 800, 50, player));
        // Begin tijd zetten , zodat we die later van mekaar af kunnen halen
        generateTerrainTreshold = 1600;
        offset = 0;

        startTimer = System.currentTimeMillis();
        //generatePlatforms(offset);
    }

    private void generatePlatforms(int offset) {
        int s = 50;
        Random rand = new Random();
        int index = rand.nextInt(3);

        if (index == 0) {
            for (int i = 0; i < 6; i++) platforms.add(new Platform(i * 50, offset, s, s, player));
        } else if (index == 1) {
            for (int i = 0; i < 6; i++) platforms.add(new Platform(500 + (i * 50), offset, s, s, player));
        } else if (index == 2) {
            for (int i = 0; i < 3; i++) platforms.add(new Platform(i * 50, offset, s, s, player));
            for (int i = 0; i < 3; i++) platforms.add(new Platform(650 + (i * 50), offset, s, s, player));
        }
        generateJetFuel();
    }

    private void generateJetFuel() {
        Random rand = new Random();
        int index = rand.nextInt(platforms.size());
        int spawnLocationY = platforms.get(index).y - 20;
        if (spawnLocationY < player.y && index != 1) {
            fuels.add(new Fuel(platforms.get(index).x, spawnLocationY, 50, 50, player));
        }
    }

    public void paint(Graphics g) {
        // Dit doe je, zodat hij altijd over je frames heen "paint" en je geen "flicker" krijgt.
        super.paint(g);

        Graphics2D gtd = (Graphics2D) g;

        player.draw(gtd);
        for (Platform plat : platforms) plat.draw(gtd);
        for (Fuel fuel : fuels) fuel.draw(gtd);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w' && player.canFly) {
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
