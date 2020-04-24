import java.awt.*;

public class Platform {

    int x;
    int y;
    int width;
    int height;

    Rectangle hitBox;

    public Platform(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        hitBox = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g){
        g.setColor(Color.BLACK);
        g.drawRect(x,y,width,height);
        g.setColor(Color.YELLOW);
        g.fillRect(x + 1,y + 1,width - 2,height - 2);
    }
}
