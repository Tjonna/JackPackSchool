package GameStatus;

import java.awt.*;

public abstract class GameStatus {

    protected GameStatusManager gsm;

    public abstract void init();
    public abstract void update();
    public abstract void draw(Graphics2D g);
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);
}
