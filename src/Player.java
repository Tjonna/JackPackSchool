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

    public boolean keyLeft;
    public boolean keyRight;
    public boolean keyUp;
    public boolean keyDown;
    boolean keyRestart;

    // Locatie van de speler en de panel waar hij geprojecteerd op moet owrden
    public Player(int x, int y, GamePanel panel) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        hitBox = new Rectangle(x, y, width, height);
    }

    public void set() {
        panel.cameraY -= panel.cameraYspeed;
        if (y >= 1500 + panel.cameraY|| keyRestart) {
            if (keyRestart) {
                System.out.println("Restart!");
            } else {
                System.out.println("Speler is dood!");
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
            // Kijken of we de grond raken met hitbox)
            hitBox.y++;
            flyspeed = -6;
//            for (Platform plat : panel.platforms) {
//                if (plat.hitBox.intersects(hitBox)) {
//                    // -6 is eigenlijk omhoog.
//                    flyspeed = -6;
//                }
//            }
            hitBox.y--;

        } else if (keyDown) {
            hitBox.y++;
            flyspeed = +10;
            hitBox.y--;
        }

        flyspeed += 0.3;

        // Horizontaal collision
        hitBox.x += xspeed;
        for (Platform plat : panel.platforms) {
            if (hitBox.intersects(plat.hitBox)) {
                hitBox.x -= xspeed;
                while (!plat.hitBox.intersects(hitBox)) hitBox.x += Math.signum(xspeed);
                hitBox.x -= Math.signum(xspeed);
                xspeed = 0;
                x = hitBox.x;
            }
        }

        // Verticaal collision
        hitBox.y += flyspeed;
        for (Platform plat : panel.platforms) {
            if (hitBox.intersects(plat.hitBox)) {
                hitBox.y -= flyspeed;
                while (!plat.hitBox.intersects(hitBox)) hitBox.y += Math.signum(flyspeed);

                hitBox.y -= Math.signum(flyspeed);
                flyspeed = 0;
                y = hitBox.y;
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
    }
}
