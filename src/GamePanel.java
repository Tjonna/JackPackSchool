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
    public static ArrayList<Platform> platforms = new ArrayList<>();
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



    boolean cantGenerateMorePlatforms;


    public GamePanel() throws InterruptedException {
        player = new Player(400, 0, this);
        loopcount=1;
        resetGame();
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Amount of platforms: " + platforms.size());
                // Jetpack fuel checken, het mag niet over de gestelde grenswaardes heen.
                if (jetPackFuel > 150) {
                    jetPackFuel = 150;
                } else if (jetPackFuel < 0) {
                    jetPackFuel = 0;
                }


                // Zodra de speler net op een nieuwe positie gezet is door uit het gamepanel venster te gaan moeten er wat dingen gebeuren
                if (player.y <= 0) {
                    // Ff checken of t niet al gedaan wordt.
                    if (!cantGenerateMorePlatforms) {
                        for (int i = 0; i < 10; i++) {
                            // Generering van terrein / platformen
                            System.out.println("Should generate platforms now");
                            generatePlatforms(offset);
                            offset -= 250;
                            // Zodat het evenement maar 1 keer afspeeld en niet 100000 platforms genereert.
                            if (i == 9) {
                                cantGenerateMorePlatforms = true;
                            }
                        }
                    }

                    cantGenerateMorePlatforms = false;
                    differentiate += 800;
                    for (Platform plat : platforms) plat.set(differentiate);
                    for (Fuel fuel : fuels) fuel.set(differentiate);
                    for (int i = 0; i < platforms.size(); i++) {
                        // Zodat we geen problemen krijgen bij de verticale colissie
                        if (platforms.get(i).hitBox.y - 750 >= 20 && platforms.get(i).hitBox.y - 750 <= 50) {
                            System.out.println("700!");
                            player.y = 700;
                        }
                    }
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
                // De tijd in milliseconde bijhouden
                currentTimer = System.currentTimeMillis() - startTimer;
                // Zet de player positiesrr
                player.set();
                // Deze mochten weg gehaald worden omdat ze op andere plek komen. Om spawns te fixen.
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
        // We beginnen bij 0 voor differentiate, set in Platform & Fuel berekent hier mee

        differentiate = 0;
        score = 0;
        fuels.add(new Fuel(400, 400, 50, 50, player));
        startTimer = System.currentTimeMillis();


        for (int i = 0; i < 5; i++) {
            generatePlatforms(offset);
            offset -= 250;
        }

    }

    private void generatePlatforms(int offset) {
        // We gebruiken een standaard grootte genaamd s , voor hoogte en breedte
        int s = 50;
        Random rand = new Random();
        int index = rand.nextInt(3);
        // Dit zijn de random generated structures.
        if (index == 0) {
            for (int i = 0; i < 6; i++) platforms.add(new Platform(i * 50, offset, s, s, player));
        } else if (index == 1) {
            for (int i = 0; i < 6; i++) platforms.add(new Platform(500 + (i * 50), offset, s, s, player));
        } else if (index == 2) {
            for (int i = 0; i < 3; i++) platforms.add(new Platform(i * 50, offset, s, s, player));
            for (int i = 0; i < 3; i++) platforms.add(new Platform(650 + (i * 50), offset, s, s, player));
        }
        // We genereren pas fuel NA het neerzetten van platforms ( zodat ze niet verkeerd spawnen omdat de y van de platformen veranderd en niet van de fuel ) door Platform.set
        for (int i = 0; i < platforms.size(); i++) {
            if (platforms.get(i).hasFuel) {
                fuels.add(new Fuel(platforms.get(i).x + 10, platforms.get(i).y - 40, 50, 50, player));
                platforms.get(i).hasFuel = false;
            }
        }
    }

    // Oude functie, werkte een tijdje maar hasFuel boolean voor elk platform is de gekozen optie geworden.


//    private void generateJetFuel() {
//        Random rand = new Random();
//        int index = rand.nextInt(platforms.size());
//        int spawnLocationY = platforms.get(index).y - 35;
//        if (spawnLocationY < player.y && index != 1) {
//            fuels.add(new Fuel(platforms.get(index).x + 10, spawnLocationY, 50, 50, player));
//        }
//    }

    public void paint(Graphics g) {
        // Dit doe je, zodat hij altijd over je frames heen "paint" en je geen "flicker" krijgt.
        super.paint(g);

        Graphics2D gtd = (Graphics2D) g;

        try {
            player.draw(gtd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Tekenen van platforms
        if (platforms != null) {
            for (int i = 0; i < platforms.size(); i++) {
                try {
                    platforms.get(i).draw(gtd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Teken van fuel
        if (fuels != null) {
            for (int i = 0; i < fuels.size(); i++) {
                try {
                    fuels.get(i).draw(gtd);
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
        // Het moet een toggle zijn, dus er is ook geen keyRelease.
        if (e.getKeyChar() == 'g' && player.keyGraphics) {
            player.keyGraphics = false;
        } else if (e.getKeyChar() == 'g' && !player.keyGraphics) {
            player.keyGraphics = true;
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
            if (y == 1 && player.canFly && jetPackFuel > 0) {
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
            if (enter == 1) {
                player.keyRestart = true;
            }
            else{player.keyRestart = false;}
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
