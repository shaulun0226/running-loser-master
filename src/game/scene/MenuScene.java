package game.scene;

import game.controllers.AudioResourceController;
import game.controllers.SceneController;
import game.gameobj.Player;
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
import static game.utils.Global.*;
import game.utils.Path;
import game.utils.PlayerAnimator;
import game.utils.PlayerAnimator.ActorType;
import java.awt.Image;
import java.util.ArrayList;

public class MenuScene extends Scene {

    private PopupWindowScene testPop;
    //private Label a;
    private Button b, b2, b3;
    //private EditText ee;
    private Image background;
    private Image title;
    private ArrayList<Player> players;
    private PlayerAnimator p1;
    private PlayerAnimator p2;
    private PlayerAnimator p3;

    @Override
    public void sceneBegin() {
        System.out.println("主選單場景開始");
        AudioResourceController.getInstance().stop(new Path().sound().menu()); // 停音樂
        AudioResourceController.getInstance().stop(new Path().sound().hanamatsuri()); // 停音樂
        background = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background1());
        title = SceneController.instance().irc().tryGetImage(new Path().img().objs().title());
        initTheme(); // 主題
        //testPop = new PopupWindowScene(300, 200, 650, 450);  // 建構彈出視窗
        //testPop.setCancelable();
        //a = new Label(430, 122);
        p1 = new PlayerAnimator(PlayerAnimator.State.WALK,ActorType.NARUTOHD);
        p2 = new PlayerAnimator(PlayerAnimator.State.WALK,ActorType.SASUKEHD);
        p3 = new PlayerAnimator(PlayerAnimator.State.SAKURASTOP,ActorType.SAKURAHD);
        b = new Button(WINDOW_WIDTH/2 -(WINDOW_WIDTH/7)/2, WINDOW_HEIGHT/2-WINDOW_HEIGHT/5, Theme.get(0)); // 開始
        b.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().shot(new Path().sound().button());
            SceneController.instance().change(new StoryScene());
        });
        b2 = new Button(WINDOW_WIDTH/2 -(WINDOW_WIDTH/7)/2, WINDOW_HEIGHT/2 , Theme.get(1)); // 說明
        b2.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().shot(new Path().sound().button());
            SceneController.instance().change(new DescriptionScene());
        });
        b3 = new Button(WINDOW_WIDTH/2 -(WINDOW_WIDTH/7)/2, WINDOW_HEIGHT/2 +WINDOW_HEIGHT/5, Theme.get(2)); // 離開遊戲
        b3.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().shot(new Path().sound().button());
            System.exit(0);
        });
        AudioResourceController.getInstance().loop(new Path().sound().hanamatsuri(), 99);  // 放音樂
    }

    @Override
    public void sceneEnd() {
        b = null;
        b2 = null;
        b3 = null;
        p1=null;
        p2=null;
        p3=null;
        background = null;
        title = null;
        System.out.println("主選單場景結束");
        SceneController.instance().irc().clear();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        g.drawImage(title, WINDOW_WIDTH/2-(WINDOW_WIDTH*3/5)/2, WINDOW_HEIGHT-WINDOW_HEIGHT*93/100, WINDOW_WIDTH*2/3, WINDOW_HEIGHT/5, null);
        p1.paint(Direction.RIGHT, WINDOW_WIDTH-WINDOW_WIDTH*8/9, WINDOW_HEIGHT-WINDOW_HEIGHT*30/100, WINDOW_WIDTH-WINDOW_WIDTH*8/9+100, WINDOW_HEIGHT-WINDOW_HEIGHT*30/100+100, g);
        p2.paint(Direction.RIGHT, WINDOW_WIDTH-WINDOW_WIDTH*7/9, WINDOW_HEIGHT-WINDOW_HEIGHT*30/100, WINDOW_WIDTH-WINDOW_WIDTH*7/9+100, WINDOW_HEIGHT-WINDOW_HEIGHT*30/100+100, g);
        p3.paint(Direction.LEFT, WINDOW_WIDTH-WINDOW_WIDTH*2/9, WINDOW_HEIGHT-WINDOW_HEIGHT*30/100, WINDOW_WIDTH-WINDOW_WIDTH*2/9+100, WINDOW_HEIGHT-WINDOW_HEIGHT*30/100+80, g);
        //a.paint(g);
        b.paint(g);
        b2.paint(g);
        b3.paint(g);
        
        //ee.paint(g);
        /*if (testPop.isShow()) {
            testPop.paint(g);
        }*/
    }

    @Override
    public void update() {
        p1.update();
        p2.update();
        p3.update();
    }

    @Override
    public MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            //MouseTriggerImpl.mouseTrig(a, e, state);
            MouseTriggerImpl.mouseTrig(b, e, state);
            MouseTriggerImpl.mouseTrig(b2, e, state);
            MouseTriggerImpl.mouseTrig(b3, e, state);
            //MouseTriggerImpl.mouseTrig(ee, e, state);
            /*if (testPop.isShow()) {
                testPop.mouseListener().mouseTrig(e, state, trigTime);
            }*/
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
    
    private static void initTheme() {
        Style sure = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10, true, new BackgroundType.BackgroundColor(Color.YELLOW))
                .setTextColor(new Color(128, 128, 128))
                .setHaveBorder(true)
                .setBorderColor(new Color(255, 215, 0))
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("SURE?");
        Style home = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10,true,new BackgroundType.BackgroundImage(SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2())))
                .setTextColor(Color.RED)
                .setHaveBorder(true)
                .setBorderColor(new Color(255, 215, 0))
                .setBorderThickness(10)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("HOME");
        Style play = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10,true,new BackgroundType.BackgroundImage(SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2())))/*new Style.StyleOval(300, 100, new BackgroundType.BackgroundImage(SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2())))*/
                .setHaveBorder(true)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setTextColor(Color.RED)
                .setText("PLAY");
        
        Style description = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10,true,new BackgroundType.BackgroundImage(SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2())))
                .setHaveBorder(true)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 20))
                .setTextColor(Color.RED)
                .setText("DESCRIPTION");
        
        Style exit = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10,true,new BackgroundType.BackgroundImage(SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2())))
                .setHaveBorder(true)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setTextColor(Color.RED)
                .setText("EXIT");
        
        Style ready = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10, true, new BackgroundType.BackgroundColor(Color.YELLOW))
                .setHaveBorder(true)
                .setBorderColor(Color.ORANGE)
                .setBorderThickness(10)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setTextColor(new Color(128, 128, 128))
                .setText("RUN?");
        
        Style mode300 = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10,true,new BackgroundType.BackgroundImage(SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2())))
                .setTextColor(Color.RED)
                .setHaveBorder(true)
                .setBorderColor(new Color(230, 184, 0))
                .setBorderThickness(10)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("300sec");
        
        Style mode600 = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10,true,new BackgroundType.BackgroundImage(SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2())))
                .setTextColor(Color.RED)
                .setHaveBorder(true)
                .setBorderColor(new Color(230, 184, 0))
                .setBorderThickness(10)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("600sec");
                
        Style modeGoal = new Style.StyleRect(WINDOW_WIDTH/7,WINDOW_HEIGHT/10,true,new BackgroundType.BackgroundImage(SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().background2())))
                .setTextColor(Color.RED)
                .setHaveBorder(true)
                .setBorderColor(new Color(230, 184, 0))
                .setBorderThickness(10)
                .setTextFont(new Font("", Font.TYPE1_FONT, 30))
                .setText("unlimited");

        Theme.add(new Theme(play, sure, home)); // 開始主題(平常，滑鼠移上去，按下) 0
        Theme.add(new Theme(description, sure, home)); // 說明主題 1
        Theme.add(new Theme(exit, sure, home)); // 離開主題 2
        Theme.add(new Theme(home, sure, home)); // 回主選單 3
        Theme.add(new Theme(mode300, ready, sure));
        Theme.add(new Theme(mode600, ready, sure));
        Theme.add(new Theme(modeGoal, ready, sure));
    }
}
