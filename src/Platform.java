import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Platform extends GenereerbaarObject {

    int x;
    int y;
    int width;
    int height;
    int startY;

    boolean hasFuel;

    Player player;

    Rectangle hitBox;

    BufferedImage image;

    public Platform(int x, int y, int width, int height, Player player) {

        this.x = x;
        this.y = y;
        startY = y;
        this.width = width;
        this.height = height;
        this.player = player;

        hitBox = new Rectangle(x, y, width, height);

        Random rand = new Random();
        int index = rand.nextInt(5);
        if (index == 4) {
            System.out.println("has fuel is true");
            hasFuel = true;
        } else {
            System.out.println("has fuel is false");
            hasFuel = false;
        }
    }

    public void draw(Graphics2D g) throws IOException {
        // Zo kan je de sprite van de platformen aanzetten
        if (player.keyGraphics) {
            image = ImageIO.read(new File("src/resources/platform.png"));
            g.drawImage(image, x, y, null);
        } else {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
            g.setColor(Color.YELLOW);
            g.fillRect(x + 1, y + 1, width - 2, height - 2);
        }
    }

    public int set(int playerY) {
        y = startY + playerY;
        if (!hitBox.intersects(player.hitBox)) hitBox.y = y;

        return y;
    }


}
