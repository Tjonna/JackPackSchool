import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Player{

    private GamePanel panel;
    int x;
    int y;
    int width = 50;
    int height = 40;

    double xspeed = x;
    double flyspeed = y;

    Rectangle hitBox;

    boolean keyLeft;
    boolean keyRight;
    boolean keyUp;
    boolean keyDown;
    boolean keyRestart;
    boolean keyGraphics;

    boolean canFly;
    long endTime;
    int deathRange = 1800;

    BufferedImage image;

    // Locatie van de speler en de panel waar hij geprojecteerd op moet worden
    public Player(int x, int y, GamePanel panel) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        hitBox = new Rectangle(x, y, width, height);
    }

    public void set() {

        // Ik zet de player areas waar je dood kan gaan en de reset knop
        if (y >= 1000 || y <= -250 || keyRestart) {
            if (keyRestart) {
                System.out.println("Restart!");
            } else {
                System.out.println("Speler is dood!");
                System.out.println("The player was at: " + y);
                // System.out.println("The camera was at: " + panel.cameraY);
                // System.out.println("The player had to be higher than: " + (2000 + panel.cameraY));
            }
            panel.resetGame();
        }
        // Borders aan de X as
        if (x < 5) {
            x = 0;
        } else if (x > 745) {
            x = 745;
        }
        //Zorgen dat er geen dubbel op movement komt`
        if (keyLeft && keyRight || !keyLeft && !keyRight) xspeed *= 0.8;
        else if (keyLeft && !keyRight) xspeed--;
        else if (keyRight && !keyLeft) xspeed++;


        // Snelheid reguleren / niet keihard weg vliegen
        if (xspeed > 0 && xspeed < 0.75) xspeed = 0;
        if (xspeed < 0 && xspeed > -0.75) xspeed = 0;

        if (xspeed > 7) xspeed = 7;
        if (xspeed < -7) xspeed = -7;

        x += xspeed;
        y += flyspeed;

        hitBox.x = x;
        hitBox.y = y;

        // Dit is de jetpack controller, hij doet alle checks of de jetpack aan mag / speler omhoog mag ja of nee. Timer na gebruik, fuel
        if (keyUp) {
            if (!canFly) {
                System.out.println("Can't fly!");
                keyUp = false;
            } else if (panel.jetPackFuel <= 0) {
                System.out.println("Out of fuel");
                keyUp = false;
            } else if (endTime != 0) {
                if (panel.currentTimer >= endTime) {
                    System.out.println("Can't fly anymore");
                    canFly = false;
                    endTime = 0;
                }
            } else {
                System.out.println("Setting end timer to 1 secw");
                endTime = panel.currentTimer + 1500;
            }
            // Kijken of we de grond raken met hitbox)
            panel.jetPackFuel -= 0.1;
            hitBox.y++;
            flyspeed = -6;
            hitBox.y--;
        }

        flyspeed += 0.3;

        // Horizontaal collision handler. Hij kijkt of je collide met een hitbox en zet je terug met dezelfde xspeed als normaal.
        hitBox.x += xspeed;
        for (Platform plat : panel.platforms) {
            if (hitBox.intersects(plat.hitBox)) {
                // Tijdens de intersectie:
                canFly = true;
                hitBox.x -= xspeed;

                // Na de intersectie, dus nu niet meer.
                while (!plat.hitBox.intersects(hitBox)) hitBox.x += Math.signum(xspeed);
                hitBox.x -= Math.signum(xspeed);
                xspeed = 0;
                x = hitBox.x;
            }
        }

        // Verticaal collision handler. Hij kijkt of je collide met een hitbox en zet je terug met dezelfde flyspeed als normaal.
        hitBox.y += flyspeed;
        for (Platform plat : panel.platforms) {
            if (hitBox.intersects(plat.hitBox)) {
                // Tijdens de intersectie:
                hitBox.y -= flyspeed;
                if (hitBox.intersects(new Rectangle(plat.hitBox.x, plat.hitBox.y - 20, 50, 5))) endTime = 0;
                canFly = true;

                // Na de intersectie, dus nu niet meer.
                while (!plat.hitBox.intersects(hitBox)) hitBox.y += Math.signum(flyspeed);
                hitBox.y -= Math.signum(flyspeed);
                flyspeed = 0;
                y = hitBox.y;
            }

        }

        // Code scant alle fuels en kijkt of er ge-intersect wordt , verwijderd fuel uit de arrayList en voegt score toe en fuel meter bijvullen met 100.
        for (int i = 0; i < panel.fuels.size(); i++) {
            if (hitBox.intersects(panel.fuels.get(i).hitBox)) {
                // Zodra fuel gehit wordt, haal m uit de array list. Dus ook niet meer drawen.
                panel.fuels.remove(i);
                System.out.println("POWERED UP!");
                panel.jetPackFuel += 100;
                panel.score += 5;
            }
        }

    }

    public void draw(Graphics2D g) throws IOException {
        // Player:
        if (keyUp) {
            image = ImageIO.read(new File("src/resources/on.png"));
            g.drawImage(image, x, y, null);
        } else {
            image = ImageIO.read(new File("src/resources/off.png"));
            g.drawImage(image, x, y, null);
        }
//        g.setColor(Color.BLACK);
//        g.drawRect(x, y, width, height);
//        g.setColor(Color.GREEN);
//        g.fillRect(x + 1, y + 1, width - 1, height - 1);


        // Text in the game:
        Font f = new Font("Arial", Font.BOLD, 20);
        g.setFont(f);
        g.setColor(Color.BLACK);

        g.drawString("Score: " + panel.score, 50, 100);
        g.drawString("Fuel: " + panel.jetPackFuel, 50, 120);


    }
}
