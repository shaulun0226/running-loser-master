package game.scene;

import game.controllers.AudioResourceController;
import game.controllers.SceneController;
import game.gameobj.Player;
import game.gameobj.Rect;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import game.menu.*;
import game.menu.Style.StyleRect;
import game.menu.impl.MouseTriggerImpl;
import game.scene.Scene;
import game.utils.CommandSolver;
import game.utils.CommandSolver.MouseCommandListener;
import game.utils.Delay;
import static game.utils.Global.*;
import game.utils.Path;
import game.utils.PlayerAnimator;
import game.utils.PlayerAnimator.ActorType;
import java.awt.Image;
import java.util.ArrayList;

public class EndingScene extends Scene {

    private Image background;
    private Image title;
    private ArrayList<Player> players;
    private PlayerAnimator p1;
    private PlayerAnimator p2;
    private PlayerAnimator p3;
    private Player winner;
    private Image heart;
    private int[] arr = {0, 1, 2, 3, 4, 5, 6};
    private int count;
    private Delay delay;
    private Button home;

    public EndingScene(Player winner) {
        this.winner = winner;
        winner.resetRect(Rect.genWithCenter(100, 735, 65, 100));
        winner.playerAnimator().setState(PlayerAnimator.State.WALK);
    }

    @Override
    public void sceneBegin() {
        background = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background1());
        AudioResourceController.getInstance().stop(new Path().sound().background()); // 停音樂
        AudioResourceController.getInstance().loop(new Path().sound().menu(), 99);  // 放音樂
//        title = SceneController.instance().irc().tryGetImage(new Path().img().objs().title());
        //testPop = new PopupWindowScene(300, 200, 650, 450);  // 建構彈出視窗
        //testPop.setCancelable();
        //a = new Label(430, 122);
//        p1 = new PlayerAnimator(PlayerAnimator.State.WALK,ActorType.NARUTOHD);
//        p2 = new PlayerAnimator(PlayerAnimator.State.WALK,ActorType.SASUKEHD);
        p3 = new PlayerAnimator(PlayerAnimator.State.SAKURASTOP, ActorType.SAKURAHD);
        this.heart = SceneController.instance().irc().tryGetImage(new Path().img().actors().heart());
        delay = new Delay(5);
        delay.loop();
        home = new Button(WINDOW_WIDTH / 2 - 150, WINDOW_HEIGHT / 2 + 300, Theme.get(3));
        home.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().play(new Path().sound().button());
            SceneController.instance().change(new MenuScene());
        });
//        b = new Button(WINDOW_WIDTH/2 -150, WINDOW_HEIGHT/2, Theme.get(0)); // 開始
//        b.setClickedActionPerformed((int x, int y) -> {
//            AudioResourceController.getInstance().shot(new Path().sound().button());
//            SceneController.instance().change(new StoryScene());
//        });
//        b2 = new Button(WINDOW_WIDTH/2 -150, WINDOW_HEIGHT/2 + 150, Theme.get(1)); // 說明
//        b2.setClickedActionPerformed((int x, int y) -> {
//            AudioResourceController.getInstance().shot(new Path().sound().button());
//            SceneController.instance().change(new DescriptionScene());
//        });
//        b3 = new Button(WINDOW_WIDTH/2 -150, WINDOW_HEIGHT/2 +300, Theme.get(2)); // 離開遊戲
//        b3.setClickedActionPerformed((int x, int y) -> {
//            AudioResourceController.getInstance().shot(new Path().sound().button());
//            System.exit(0);
//        });
    }

    @Override
    public void sceneEnd() {
        home = null;
        p1 = null;
        p2 = null;
        p3 = null;
        background = null;
        title = null;
        System.out.println("主選單場景結束");
        SceneController.instance().irc().clear();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
//        g.drawImage(title, 520, 50, 800, 400, null);
//        p1.paint(Direction.RIGHT, 500, 700, 600, 800, g);
//        p2.paint(Direction.RIGHT, 400, 700, 500, 800, g);
        p3.paint(Direction.LEFT, 1450, 680, 1515, 780, g);
        winner.paint(g);

        //a.paint(g);
//        b.paint(g);
//        b2.paint(g);
//        b3.paint(g);
        //ee.paint(g);
        /*if (testPop.isShow()) {
            testPop.paint(g);
        }*/
        if (winner.painter().right() > 1420) {
            g.drawImage(heart, 1450 + 10, 600 + 20, 1450 + 48 + 10, 600 + 43 + 20, 40 + 48 * arr[count], 0, 40 + 48 + 48 * arr[count], 43, null);
            home.paint(g);
        }
    }

    @Override
    public void update() {
        if (winner.painter().right() < 1440) {
            winner.translateX(10);
        } else if (winner.playerAnimator().State() != PlayerAnimator.State.STOP) {
            winner.playerAnimator().setState(PlayerAnimator.State.STOP);
        }
//        p1.update();
//        p2.update();
        winner.playerAnimator().update();
        p3.update();
        if (delay.count()) {
            count = ++count % arr.length;
        }
    }

    @Override
    public MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(home, e, state);
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
            }

            @Override
            public void keyTyped(char c, long trigTime) {
            }
        };
    }
}
