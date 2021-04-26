package game.scene;

import game.controllers.AudioResourceController;
import game.controllers.SceneController;
import game.menu.Button;
import game.menu.Theme;
import game.menu.impl.MouseTriggerImpl;
import game.scene.Scene;
import game.utils.CommandSolver;
import static game.utils.Global.SCREEN_X;
import static game.utils.Global.SCREEN_Y;
import static game.utils.Global.WINDOW_HEIGHT;
import static game.utils.Global.WINDOW_WIDTH;
import game.utils.Path;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

public class DescriptionScene extends Scene{
    private Image background, aP1, aP2, con1, con2, tool;
    private Button home;

    @Override
    public void sceneBegin() {
        background = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2());
        con1 = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().controlP1());
        con2 = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().controlP2());
        tool = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().tool());
        aP1 = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHD());
        aP2 = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHD());
        home = new Button(WINDOW_WIDTH/2 , WINDOW_HEIGHT - 180 , Theme.get(3));
        home.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().shot(new Path().sound().button());
            SceneController.instance().change(new MenuScene());
        });
    }

    @Override
    public void sceneEnd() {
        aP1 = null;
        aP2 = null;
        con1 = null;
        con2 = null;
        tool = null;
        home = null;
        background = null;
        SceneController.instance().irc().clear();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        g.drawImage(aP1, 100, 150, SCREEN_X / 2, SCREEN_Y, 0, 582 , 160 ,764, null);
        g.drawImage(aP2, SCREEN_X / 2 + 100, 150, SCREEN_X, SCREEN_Y, 0,716,238,900, null);
        g.drawImage(con1, 0, SCREEN_Y / 2 - 150, 500, 400, null);  
        g.drawImage(con2, SCREEN_X / 2, SCREEN_Y / 2 - 150, 500, 330, null);
        g.drawImage(tool, 0, 0, WINDOW_WIDTH, 180, null);     // 磚塊說明
        home.paint(g);
    }

    @Override
    public void update() {
        home.update();
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(home, e, state);
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
