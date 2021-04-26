package game.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import game.menu.*;
import game.menu.impl.MouseTriggerImpl;
import game.utils.CommandSolver;
import static game.utils.Global.WINDOW_HEIGHT;
import static game.utils.Global.WINDOW_WIDTH;

public class PopupWindowScene extends PopupWindow {

    private Button p1;
    private Button p2;

    public PopupWindowScene(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void sceneBegin() {
        p1 = new Button(WINDOW_WIDTH/2 - 850, WINDOW_HEIGHT/2 , Theme.get(3));
        p2 = new Button(WINDOW_WIDTH/2, WINDOW_HEIGHT/2, Theme.get(4));
        p1.setClickedActionPerformed((int x, int y) -> System.out.println("ClickedAction"));
    }

    @Override
    public void sceneEnd() {
        p1 = null;
        p2 = null;
    }

    @Override
    public void paintWindow(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
        p1.paint(g);
        p2.paint(g);
    }

    @Override
    public void update() {

    }

    @Override
    protected void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        MouseTriggerImpl.mouseTrig(p1, e, state);
        MouseTriggerImpl.mouseTrig(p2, e, state);
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
