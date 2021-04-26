package game.scene;

import game.controllers.AudioResourceController;
import game.controllers.SceneController;
import game.gameobj.Player;
import game.gameobj.Rect;
import game.menu.Theme;
import game.menu.impl.MouseTriggerImpl;
import game.utils.CoinAnimator;
import game.utils.CommandSolver;
import game.utils.Delay;
import static game.utils.Global.*;
import game.utils.MonsterAnimator;
import game.utils.MonsterAnimator.MonsterType;
import game.utils.Path;
import game.utils.PlayerAnimator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CaulateScene extends Scene {

    private ArrayList<Player> players;
    private ArrayList<PlayerAnimator> playerAnis;
    private Image p1Actor;
    private Image p2Actor;
    private Image win;
    private CoinAnimator coinCount;//畫出右上角的計算金幣
    private MonsterAnimator monsterCount;//畫怪物
    private final int p1TotalDis;
    private int p1DistanceCount;
    private final int p1TotalCoin;
    private int p1CoinCount;
    private int p1CoinPointCount;
    private int p1TotalPoint;
    private final int p1TotalMonsterCount;
    private int p1MonsterCount;
    private int p1MonsterPointCount;
    private final int p2TotalDis;
    private int p2DistanceCount;
    private int p2CoinCount;
    private final int p2TotalCoin;
    private int p2CoinPointCount;
    private int p2TotalPoint;
    private final int p2TotalMonsterCount;
    private int p2MonsterCount;
    private int p2MonsterPointCount;
    private boolean p1countEnd;
    private boolean p2countEnd;
    private Delay endDelay;

//    public CaulateScene(ArrayList<Player> players) {
//        this.players = players;
//        this.p1TotalDis = players.get(0).collider().right() / 10;
//        this.p2TotalDis = players.get(1).collider().right() / 10;
//        this.p1TotalCoin = players.get(0).getCoinCount();
//        this.p2TotalCoin = players.get(1).getCoinCount();
//        this.p1DistanceCount = 0;
//        this.p1CoinCount = 0;
//        this.p1CoinPointCount = 0;
//        this.p2DistanceCount = 0;
//        this.p2CoinCount = 0;
//        this.p2CoinPointCount = 0;
//        for (int i = 0; i < players.size(); i++) {
//            players.get(i).setDir(Direction.RIGHT);
//            players.get(i).setState(PlayerAnimator.State.WALK);
//        }
//        players.get(0).setRect(new Rect(SCREEN_X / 2 + 40, 50, SCREEN_X / 2 + 40 + ACTOR_WIDTH, 50 + ACTOR_HEIGHT));
//        players.get(1).setRect(new Rect(0 + 40, SCREEN_Y / 2 + 26, 0 + 40 + ACTOR_WIDTH, SCREEN_Y / 2 + 26 + ACTOR_HEIGHT));
//    }
    public CaulateScene(int p1TotalDis, int p1TotalCoin, int p1TotalMonsterCount, int p2TotalDis, int p2TotalCoin, int p2TotalMonsterCount) {
        this.p1TotalDis = p1TotalDis;
        this.p1TotalCoin = p1TotalCoin;
        this.p1TotalMonsterCount = p1TotalMonsterCount;
        this.p2TotalDis = p2TotalDis;
        this.p2TotalCoin = p2TotalCoin;
        this.p2TotalMonsterCount = p2TotalMonsterCount;
        this.players = new ArrayList<>();
        this.players.add(new Player(PlayerAnimator.ActorType.NARUTOHD, SCREEN_X / 2 + 55, 85));
        this.players.add(new Player(PlayerAnimator.ActorType.SASUKEHD, 0 + 55, SCREEN_Y / 2 + 61));
        this.playerAnis = new ArrayList<>();
        this.playerAnis.add(new PlayerAnimator(PlayerAnimator.State.WALK, PlayerAnimator.ActorType.NARUTOHD));
        this.playerAnis.add(new PlayerAnimator(PlayerAnimator.State.WALK, PlayerAnimator.ActorType.SASUKEHD));
        this.p1DistanceCount = 0;
        this.p1CoinCount = 0;
        this.p1CoinPointCount = 0;
        this.p2DistanceCount = 0;
        this.p2CoinCount = 0;
        this.p2CoinPointCount = 0;
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setDir(Direction.RIGHT);
            players.get(i).setState(PlayerAnimator.State.WALK);
        }
//        players.get(0).setRect(new Rect(SCREEN_X / 2 + 40, 50, SCREEN_X / 2 + 40 + ACTOR_WIDTH, 50 + ACTOR_HEIGHT));
//        players.get(1).setRect(new Rect(0 + 40, SCREEN_Y / 2 + 26, 0 + 40 + ACTOR_WIDTH, SCREEN_Y / 2 + 26 + ACTOR_HEIGHT));
    }

    @Override
    public void sceneBegin() {
        p1Actor = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHD());
        p2Actor = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHD());
        win = SceneController.instance().irc().tryGetImage(new Path().img().objs().win());
        //建構計算金幣的圖
        coinCount = new CoinAnimator(CoinAnimator.CoinType.COINCOUNT);
        monsterCount = new MonsterAnimator(MonsterType.PIG);
        endDelay = new Delay(120);
    }

    @Override
    public void sceneEnd() {
        p1Actor = null;
        p2Actor = null;
        coinCount = null;
        AudioResourceController.getInstance().stop("resources\\sounds\\background.wav");
        SceneController.instance().irc().clear();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, SCREEN_X, SCREEN_Y);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Dialog", Font.BOLD, 50));
        playerAnis.get(0).paint(Direction.RIGHT, SCREEN_X / 2 + 40, 50, SCREEN_X / 2 + 40 + ACTOR_WIDTH, 50 + ACTOR_HEIGHT, g);
//        players.get(0).paint(g);
        coinCount.paint(SCREEN_X / 2 + 40, 165, SCREEN_X / 2 + 40 + 41, 213, g);
        monsterCount.paint(MonsterType.PIG, Direction.LEFT, SCREEN_X / 2 + 40, 238, SCREEN_X / 2 + 40 + 41, 286, g);
        g.drawString(this.p1DistanceCount + "   M   =   " + this.p1DistanceCount + "   Pts", SCREEN_X / 2 + 100, 100 + 24);
        g.drawString(this.p1CoinCount + "    X   10    =    " + this.p1CoinPointCount + "   Pts", SCREEN_X / 2 + 100, 200 + 9);
        g.drawString(this.p1MonsterCount + "    X   50    =    " + this.p1MonsterPointCount + "   Pts", SCREEN_X / 2 + 100, 200 + 82);
        g.fillRect(100, 300, 1800, 20);
        g.setFont(new Font("Dialog", Font.BOLD, 75));
        g.drawString("TOTAL : " + this.p1TotalPoint + " Pts", SCREEN_X / 2 + 100, 400 + 24);
        g.drawImage(p1Actor, 0, 0, SCREEN_X / 2, SCREEN_Y / 2, 0, 432, 200, 554, null);
        g.setColor(Color.BLUE);
        g.setFont(new Font("Dialog", Font.BOLD, 50));
//        players.get(1).paint(g);
        playerAnis.get(1).paint(Direction.RIGHT, 0 + 40, SCREEN_Y / 2 + 26, 0 + 40 + ACTOR_WIDTH, SCREEN_Y / 2 + 26 + ACTOR_HEIGHT, g);
        coinCount.paint(40, SCREEN_Y / 2 + 141, +40 + 41, SCREEN_Y / 2 + 189, g);
        monsterCount.paint(MonsterType.PIG, Direction.LEFT, 40, SCREEN_Y / 2 + 206, +40 + 41, SCREEN_Y / 2 + 254, g);
        g.drawString(this.p2DistanceCount + "   M   =   " + this.p2DistanceCount + "   Pts", 0 + 100, SCREEN_Y / 2 + 100);
        g.drawString(this.p2CoinCount + "    X   10    =     " + p2CoinPointCount + "    Pts", 0 + 100, SCREEN_Y / 2 + 185);
        g.drawString(this.p2MonsterCount + "    X   50    =    " + this.p2MonsterPointCount + "   Pts", 0 + 100, SCREEN_Y / 2 + 253);
        g.fillRect(0, SCREEN_Y / 2 + 300, 1800, 20);
        g.setFont(new Font("Dialog", Font.BOLD, 75));
        g.drawString("TOTAL : " + this.p2TotalPoint + " Pts", 0 + 100, SCREEN_Y / 2 + 400);
        g.drawImage(p2Actor, SCREEN_X / 2, SCREEN_Y / 2, SCREEN_X, SCREEN_Y, 0, 432, 242, 554, null);
        if (p1countEnd && p2countEnd && p1TotalPoint > p2TotalPoint) {
            g.drawImage(win, 0, SCREEN_Y / 2 - 200, 600, 270, null);
        }
        if (p1countEnd && p2countEnd && p1TotalPoint < p2TotalPoint) {
            g.drawImage(win, SCREEN_X - 700, SCREEN_Y - 200, 600, 270, null);
        }
    }

    @Override
    public void update() {
        if (p1DistanceCount < p1TotalDis) {
            p1DistanceCount += 19;
        } else if (p1DistanceCount >= p1TotalDis) {
            p1DistanceCount = p1TotalDis;
            p1countEnd = true;
        }
        if (p1CoinCount < p1TotalCoin) {
            p1CoinCount += 1;
        } else if (p1CoinCount >= p1TotalCoin) {
            p1CoinCount = p1TotalCoin;
        }
        if (p1MonsterCount < p1TotalMonsterCount) {
            p1MonsterCount += 1;
        } else if (p1MonsterCount >= p1TotalMonsterCount) {
            p1MonsterCount = p1TotalMonsterCount;
        }
        if (p2DistanceCount < p2TotalDis) {
            p2DistanceCount += 19;
        } else if (p2DistanceCount >= p2TotalDis) {
            p2DistanceCount = p2TotalDis;
            p2countEnd = true;
        }
        if (p2CoinCount < p2TotalCoin) {
            p2CoinCount += 1;
        } else if (p2CoinCount >= p2TotalCoin) {
            p2CoinCount = p2TotalCoin;
        }
        if (p2MonsterCount < p2TotalMonsterCount) {
            p2MonsterCount += 1;
        } else if (p2MonsterCount >= p2TotalMonsterCount) {
            p2MonsterCount = p2TotalMonsterCount;
        }
        this.p1CoinPointCount = this.p1CoinCount * 10;
        this.p2CoinPointCount = this.p2CoinCount * 10;
        this.p1MonsterPointCount = this.p1MonsterCount * 50;
        this.p2MonsterPointCount = this.p2MonsterCount * 50;
        this.p1TotalPoint = this.p1DistanceCount + this.p1CoinPointCount + this.p1MonsterPointCount;
        this.p2TotalPoint = this.p2DistanceCount + this.p2CoinPointCount + this.p2MonsterPointCount;
        for (int i = 0; i < players.size(); i++) {
            players.get(i).update();
        }
        for (int i = 0; i < playerAnis.size(); i++) {
            playerAnis.get(i).update();
        }
        if (p1countEnd && p2countEnd && p1TotalPoint > p2TotalPoint) {
            endDelay.play();
        }
        if (p1countEnd && p2countEnd && p1TotalPoint < p2TotalPoint) {
            endDelay.play();
        }
        if (endDelay.count()) {
            if (p1countEnd && p2countEnd && p1TotalPoint > p2TotalPoint) {
                SceneController.instance().change(new EndingScene(new Player(PlayerAnimator.ActorType.NARUTOHD, 10, 300)));
            }
            if(p1countEnd && p2countEnd && p1TotalPoint < p2TotalPoint){
                SceneController.instance().change(new EndingScene(new Player(PlayerAnimator.ActorType.SASUKEHD, 10, 300)));
            }
        }
        coinCount.update();
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
