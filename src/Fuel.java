import java.awt.*;

public class Fuel {

    int x;
    int y;
    int width;
    int height;
    int startY;

    Player player;

    Rectangle hitBox;

    public Fuel(int x, int y, int width, int height, Player player) {

        this.x = x;
        this.y = y;
        startY = y;
        this.width = width;
        this.height = height;
        this.player = player;

        hitBox = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width - 20, height - 20);
        g.setColor(Color.RED);
        g.fillOval(x + 1, y + 1, width - 22, height - 22);
    }

    public int set(int playerY) {
        y = startY + playerY;
        if (!hitBox.intersects(player.hitBox)) hitBox.y = y;

        return y;
    }
}
