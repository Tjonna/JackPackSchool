import java.awt.*;

public class Player {

    private GamePanel panel;
    int x;
    int y;
    int width = 50;
    int height = 50;

    double xspeed = x;
    double flyspeed = y;

    Rectangle hitBox;

    boolean keyLeft;
    boolean keyRight;
    boolean keyUp;
    boolean keyDown;
    boolean keyRestart;

    boolean canFly;
    long endTime;
    int deathRange = 1800;

    // Locatie van de speler en de panel waar hij geprojecteerd op moet owrden
    public Player(int x, int y, GamePanel panel) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        hitBox = new Rectangle(x, y, width, height);
    }

    public void set() {
        if (y >= 900 || y <= -150 || keyRestart) {
            if (keyRestart) {
                System.out.println("Restart!");
            } else {
                System.out.println("Speler is dood!");
                System.out.println("The player was at: " + y);
                System.out.println("The camera was at: " + panel.cameraY);
                System.out.println("The player had to be higher than: " + (2000 + panel.cameraY));
            }
            panel.resetGame();
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

        if (keyUp) {
            if (!canFly) {
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
            hitBox.y++;
            flyspeed = -6;
            hitBox.y--;
        }

        flyspeed += 0.3;

        // Horizontaal collision
        hitBox.x += xspeed;
        for (Platform plat : panel.platforms) {
            if (hitBox.intersects(plat.hitBox)) {
                canFly = true;
                hitBox.x -= xspeed;
                while (!plat.hitBox.intersects(hitBox)) hitBox.x += Math.signum(xspeed);
                hitBox.x -= Math.signum(xspeed);
                xspeed = 0;
                x = hitBox.x;
            }
        }

        // Verticaal collision
//        hitBox.y += flyspeed;
//        for (Platform plat : panel.platforms) {
//            if (hitBox.intersects(plat.hitBox)) {
//                canFly = true;
//                hitBox.y -= flyspeed;
//
//                while (!plat.hitBox.intersects(hitBox)) hitBox.y += Math.signum(flyspeed);
//                hitBox.y -= Math.signum(flyspeed);
//
//                flyspeed = 0;
//                y = hitBox.y;
//            }
//        }
        hitBox.y += flyspeed;
        for (Platform plat : panel.platforms) {
            if (hitBox.intersects(plat.hitBox)) {
                canFly = true;
                hitBox.y -= flyspeed;
                while (!plat.hitBox.intersects(hitBox)) hitBox.y += Math.signum(flyspeed);
                hitBox.y -= Math.signum(flyspeed);
                flyspeed = 0;
                y = hitBox.y;
            }
        }

        for (int i = 0; i < panel.fuels.size(); i++) {
            if (hitBox.intersects(panel.fuels.get(i).hitBox)) {
                panel.fuels.remove(i);
                System.out.println("POWERED UP!");
            }
        }

    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setColor(Color.GREEN);
        g.fillRect(x + 1, y + 1, width - 1, height - 1);

        Font f = new Font("Arial", Font.BOLD, 20);
        g.setFont(f);
        g.setColor(Color.BLACK);
        g.drawString("Camera Y: " + panel.cameraY, 100, 100);
        g.drawString("Camera Y Speed: " + panel.cameraYspeed, 100, 125);
        g.drawString("Player Y: " + y, 100, 150);

    }
}
