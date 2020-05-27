import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class GenereerbaarObject {

    int x;
    int y;
    int width;
    int height;
    int startY;

    Player player;

    Rectangle hitBox;
    BufferedImage image;

    public abstract void draw(Graphics2D g) throws IOException;

    public abstract int set(int playerY);
}
