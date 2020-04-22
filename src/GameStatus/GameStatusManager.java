package GameStatus;

import java.awt.*;
import java.util.ArrayList;

public class GameStatusManager {

    private ArrayList<GameStatus> gameState;
    private int currentState;

    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 0;

    public GameStatusManager() {
        gameState = new ArrayList<GameStatus>();

        currentState = MENUSTATE;
        gameState.add(new MenuState(this));
    }

    public void setState(int state) {
        currentState = state;
        gameState.get(currentState).init();
    }

    private void init() {
    }

    public void update(){
        gameState.get(currentState).update();
    }

    public void draw(Graphics2D g){
        gameState.get(currentState).draw(g);
    }

    public void keyPressed(int k) {
        gameState.get(currentState).keyPressed(k);
    }

    public void keyReleased(int k) {
        gameState.get(currentState).keyReleased(k);
    }
}
