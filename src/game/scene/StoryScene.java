package game.scene;

import game.controllers.AudioResourceController;
import game.controllers.SceneController;
import game.menu.Button;
import game.menu.Theme;
import game.menu.impl.MouseTriggerImpl;
import game.scene.PlayScene.Mode;
import game.utils.CommandSolver;
import static game.utils.Global.WINDOW_HEIGHT;
import static game.utils.Global.WINDOW_WIDTH;
import game.utils.Path;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

public class StoryScene extends Scene{

    private Image background,story;
    private Button mode300, mode600, modeGoal, home;
    
    @Override
    public void sceneBegin() {
         System.out.println("故事場景開始");
        mode300 = new Button(WINDOW_WIDTH/15, WINDOW_HEIGHT-WINDOW_HEIGHT/4, Theme.get(4));
        mode300.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().shot(new Path().sound().button());//按鈕
            SceneController.instance().change(new PlayScene(Mode.SEC300));
        });
        mode600 = new Button(WINDOW_WIDTH/15+ WINDOW_WIDTH/7+ WINDOW_WIDTH/10, WINDOW_HEIGHT-WINDOW_HEIGHT/4, Theme.get(5));
        mode600.setClickedActionPerformed((int x, int y) -> {
            SceneController.instance().change(new PlayScene(Mode.SEC600));
            AudioResourceController.getInstance().shot(new Path().sound().spring());//按鈕
        });
        modeGoal = new Button(WINDOW_WIDTH/15+ WINDOW_WIDTH/7*2+ WINDOW_WIDTH/10*2, WINDOW_HEIGHT-WINDOW_HEIGHT/4, Theme.get(6));
        modeGoal.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().shot(new Path().sound().button());//按鈕
            SceneController.instance().change(new PlayScene(Mode.GOAL));
        });
        home = new Button(WINDOW_WIDTH/15+ WINDOW_WIDTH/7*3+ WINDOW_WIDTH/10*3, WINDOW_HEIGHT-WINDOW_HEIGHT/4, Theme.get(3));
        home.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().shot(new Path().sound().button());
            SceneController.instance().change(new MenuScene());
                });
        background = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2());
        story = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().story());
    }

    @Override
    public void sceneEnd() {
        System.out.println("故事場景結束");
        background = null;
        story = null;
        mode300 = null;
        mode600 = null;
        modeGoal = null;
        SceneController.instance().irc().clear();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        g.drawImage(story, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        mode300.paint(g);
        mode600.paint(g);
        modeGoal.paint(g);
        home.paint(g);
    }

    @Override
    public void update() {
        mode300.update();
        mode600.update();
        modeGoal.update();
        home.update();
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(mode300, e, state);
            MouseTriggerImpl.mouseTrig(mode600, e, state);
            MouseTriggerImpl.mouseTrig(modeGoal, e, state);
            MouseTriggerImpl.mouseTrig(home, e, state);
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
