import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {

    Player player;
    Timer gameTimer;
    ArrayList<Platform> platforms = new ArrayList<>();
    ArrayList<Fuel> fuels = new ArrayList<>();
    static Server s = new Server();
    int offset;

    long startTimer, currentTimer;
    int differentiate = 0;
    int x;
    int y;
    int enter;

    int score;
    int jetPackFuel;
    int loopcount;
    String client = "0;0;0";



    public GamePanel() throws InterruptedException {
        player = new Player(400, 0, this);
        loopcount=1;
        resetGame();
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (jetPackFuel > 150) {
                    jetPackFuel = 150;
                } else if (jetPackFuel < 0) {
                    jetPackFuel = 0;
                }

                if (player.y <= 0) {
                    differentiate += 800;
                    for (Platform plat : platforms) plat.set(differentiate);
                    for (Fuel fuel : fuels) fuel.set(differentiate);
                    player.y = 750;
                    score += 10;
                }
                if(loopcount == 11) {
                    try {
                        s.Run(2,jetPackFuel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loopcount = 0;
                    client = s.fromclient;
                    analysejoystick();
                }
                else{
                    loopcount+=1;
                }
                // Dit is de game loop
                // Dit is voor de game generation, gebaseerd op vorige platform hoogte en vorig camera hoogte
//                if (cameraY - (platforms.get(platforms.size() - 1).y) <= generateTerrainTreshold) {
//                    System.out.println("Should generate");
//                    offset -= 400;
//                    generatePlatforms(offset);
//                    generateTerrainTreshold -= 100;
//                }
                /* Aangezien de camera sneller gaat dan het poppetje en de scherm, wil ik het getal evenwijdig aan mekaar laten gaan. Ook al kan ik er geen
                 goede wiskundige berekening er voor vinden, heb ik maar een lineaire vergelijking gedaan die altijd op X: 900 van het scherm blijft.
                (int) (cameraY / -1000000000) + 990;
                 Alleen kwam ik er later achter dat het poppetje en de deathLine nooit echt in de ... - 100 zone komt
                player.deathRange = 900;

                 Daardoor is deathRange overbodig en heb ik gewoon y >= 900  || y <= 250 ingevuld

                 */
                // cameraYspeed = (int) ((cameraYspeed * (currentTimer / 1000))) + 1;
                // De tijd in milliseconde bijhouden
                currentTimer = System.currentTimeMillis() - startTimer;
                // Zet de player positiesrr
                player.set();
                //    for (Platform plat : platforms) plat.set();
                //   for (Fuel fuel : fuels) fuel.set();
                // Teken op de game panel
                repaint();
            }
            // We gaan voor 60 fps. met 1000 ms 1000/ 60 = 16.666 ~= 17
        }, 0, 17);
    }

    public void resetGame() {
        player.x = 400;
        player.y = 800;
        player.xspeed = 0;
        player.flyspeed = 0;
        jetPackFuel = 150;
        // Even alles weghalen uit de arrayList , zodat er geen platformen nog  bestaan van vorige game
        platforms.clear();
        fuels.clear();
        // Begin platform
        for (int i = 0; i < 16; i++) platforms.add(new Platform(i * 50, 850, 50, 50, player));
        // Offset tussen de platform generatie
        offset = 250;
        // We beginnen bij 0 voor differentiate.
        differentiate = 0;

        startTimer = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            generatePlatforms(offset);
            offset -= 250;
        }
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
        int spawnLocationY = platforms.get(index).y - 35;
        if (spawnLocationY < player.y && index != 1) {
            fuels.add(new Fuel(platforms.get(index).x + 10, spawnLocationY, 50, 50, player));
        }
    }

    public void paint(Graphics g) {
        // Dit doe je, zodat hij altijd over je frames heen "paint" en je geen "flicker" krijgt.
        super.paint(g);

        Graphics2D gtd = (Graphics2D) g;

        try {
            player.draw(gtd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(platforms != null) {
            for (Platform plat : platforms) {
                try {
                    plat.draw(gtd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(fuels != null) {
        for (Fuel fuel : fuels) {
            try {
                fuel.draw(gtd);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w' && player.canFly && jetPackFuel > 0) {
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
    public void analysejoystick() {
        if (!(client == null || client.isEmpty())) {
            System.out.println("omfg");
            System.out.println(client.length());
            String[] parts = client.split(";");
            x = Integer.parseInt(parts[0]);
            System.out.println(x);
            y = Integer.parseInt(parts[1]);
            System.out.println(y);
            enter = Integer.parseInt(parts[2]);
            System.out.println(enter);
            if (y == 1) {
                player.keyUp = true;
            }
            else{player.keyUp = false;}
            if (y == -1) {
                player.keyDown = true;
            }
            else{player.keyDown = false;}
            if (x == 1) {
                player.keyRight = true;
            }
            else{player.keyRight = false;}
            if (x == -1) {
                player.keyLeft = true;
            }
            else{player.keyLeft = false;}
//            if (parts[1] == "1") {
//                player.keyUp = true;
//            } else {
//                player.keyUp = false;
//            }
//            System.out.println("aaa");
//            if (parts[1] == "-1") {
//                player.keyDown = true;
//            } else {
//                player.keyDown = false;
//            }
//            if (parts[0] == "-1") {
//                player.keyLeft = true;
//            } else {
//                player.keyLeft = false;
//            }
//            if (parts[0] == "1") {
//                player.keyRight = true;
//            } else {
//                player.keyRight = false;
//            }
//            if (parts[3] == "1") {
//                player.keyRestart = true;
//            } else {
//                player.keyRestart = false;
//            }
            }
        else{
            System.out.println("sdf");
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
