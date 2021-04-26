package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.camera.SmallMap;
import game.controllers.AudioResourceController;
import game.controllers.ImgController;
import game.controllers.SceneController;
import game.gameobj.Background;
import game.gameobj.Brick;
import game.gameobj.GameObject;
import game.gameobj.Player;
import game.gameobj.Bullet;
import game.gameobj.Coin;
import game.gameobj.Skill;
import game.gameobj.Monster;
import game.gameobj.RebornPoint;
import game.gameobj.Rect;
import game.menu.BackgroundType;
import game.utils.BrickAnimator;
import game.utils.BrickAnimator.BrickType;
import game.utils.CoinAnimator;
import game.utils.CoinAnimator.CoinType;
import game.utils.CommandSolver;
import game.utils.Delay;
import game.utils.SkillAnimator;
import game.utils.SkillAnimator.SkillType;
import static game.utils.Global.*;
import game.utils.Global.Direction;
import game.utils.PlayerAnimator.*;
import game.utils.Global;
import game.utils.MonsterAnimator;
import game.utils.MonsterAnimator.MonsterType;
import game.utils.Path;
import game.utils.RebornPointAnimator;
import game.utils.Vector;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import maploader.MapInfo;
import maploader.MapLoader;

public class PlayScene extends Scene {

    public enum Mode {
        SEC300(300),
        SEC600(600),
        GOAL(-99),
        DEBUG(10);

        private int value;

        Mode(int value) {
            this.value = value;
        }

        private int getValue() {
            return this.value;
        }
    }
    private Mode mode;
    private double gameTime;
    private ArrayList<Player> players;//玩家
    private ArrayList<Brick> bricks;//磚塊
    private int gravity;//地圖的重力
    private ArrayList<Camera> cameraArr;// 多鏡頭管理
    private ArrayList<RebornPoint> rebornPoints;//重生點
    private ArrayList<Coin> coins;//金幣

    private ArrayList<Skill> skills;//技能
    //
    private ArrayList<Background> p1Backgrounds;
    private ArrayList<Background> p2Backgrounds;
    private ArrayList<Monster> monsters;   // 怪物
    private Image naruto;
    private Image sasuke;
    //計分
    private Image pointCount;
    private Image pointCountOrange;
    private CoinAnimator coinCount;//畫出右上角的計算金幣
    private MonsterAnimator monsterCount;//畫出怪物計分
    //時間
    private double beginTime;
    private double passedTime;
    private boolean isFinish;
    private Delay finishDelay;
    private Image imgFinish;
    //
    private Image imgInk;
    private boolean p1Ink;
    private boolean p2Ink;
    private Delay p1InkDelay;
    private Delay p2InkDelay;

    public PlayScene(Mode mode) {
        this.mode = mode;
        this.gameTime = mode.getValue();
    }

    @Override
    public void sceneBegin() {
        System.out.println("遊戲場景開始");
        AudioResourceController.getInstance().stop(new Path().sound().hanamatsuri()); // 停音樂
        //時間
        beginTime = System.nanoTime();
        finishDelay = new Delay(90);
        finishDelay.stop();
        //地圖
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);// left, top, right, bottom
        p1Backgrounds = new ArrayList<>();
        p1Backgrounds.add(new Background(BACKGROUND_BOTTOM_WIDTH / 2, BACKGROUND_BOTTOM_HEIGHT / 2, BACKGROUND_BOTTOM_WIDTH, BACKGROUND_BOTTOM_HEIGHT, Background.BackgroundType.BOTTOM));
        p1Backgrounds.add(new Background(BACKGROUND_BOTTOM_WIDTH / 2 * 3, BACKGROUND_BOTTOM_HEIGHT / 2, BACKGROUND_BOTTOM_WIDTH, BACKGROUND_BOTTOM_HEIGHT, Background.BackgroundType.BOTTOM));
        p1Backgrounds.add(new Background(BACKGROUND_MIDDLE_WIDTH / 2, BACKGROUND_MIDDLE_HEIGHT / 2, BACKGROUND_MIDDLE_WIDTH, BACKGROUND_MIDDLE_HEIGHT, Background.BackgroundType.MIDDLE));
        p1Backgrounds.add(new Background(BACKGROUND_MIDDLE_WIDTH / 2 * 3 - 10, BACKGROUND_MIDDLE_HEIGHT / 2, BACKGROUND_MIDDLE_WIDTH, BACKGROUND_MIDDLE_HEIGHT, Background.BackgroundType.MIDDLE));
        p2Backgrounds = new ArrayList<>();
        p2Backgrounds.add(new Background(BACKGROUND_BOTTOM_WIDTH / 2, BACKGROUND_BOTTOM_HEIGHT / 2, BACKGROUND_BOTTOM_WIDTH, 960, Background.BackgroundType.BOTTOM));
        p2Backgrounds.add(new Background(BACKGROUND_BOTTOM_WIDTH / 2 * 3, BACKGROUND_BOTTOM_HEIGHT / 2, BACKGROUND_BOTTOM_WIDTH, 960, Background.BackgroundType.BOTTOM));
        p2Backgrounds.add(new Background(BACKGROUND_MIDDLE_WIDTH / 2, BACKGROUND_MIDDLE_HEIGHT / 2, BACKGROUND_MIDDLE_WIDTH, BACKGROUND_MIDDLE_HEIGHT, Background.BackgroundType.MIDDLE));
        p2Backgrounds.add(new Background(BACKGROUND_MIDDLE_WIDTH / 2 * 3, BACKGROUND_MIDDLE_HEIGHT / 2, BACKGROUND_MIDDLE_WIDTH, BACKGROUND_MIDDLE_HEIGHT, Background.BackgroundType.MIDDLE));
        //墨水
        imgInk = SceneController.instance().irc().tryGetImage(new Path().img().effects().ink());
        p1InkDelay = new Delay(EFFECT_DELAY);
        p2InkDelay = new Delay(EFFECT_DELAY);
        //計分
        pointCount = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().pointCount());
        pointCountOrange = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().pointCountOrange());
        imgFinish = SceneController.instance().irc().tryGetImage(new Path().img().objs().finish());
        naruto = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHDSmall());
        sasuke = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHDSmall());
        gravity = 2;
        players = new ArrayList<>();
        players.add(new Player(ActorType.NARUTOHD, 10, 300));
        players.add(new Player(ActorType.SASUKEHD, 10, 700));//50100,300 BOSS前
        cameraArr = new ArrayList<>();
        cameraArr.add(new Camera.Builder(SCREEN_X, SCREEN_Y / 2)// 創建一個400*300的鏡頭
                .setCameraStartLocation(0, 0)
                .setChaseObj(players.get(0), 5, 8)// 追蹤p1 追焦係數設為30
                .setCameraWindowLocation(0, 0)// 鏡頭顯示在視窗的上半邊
                //                .setCameraLockDirection(true, true, false, true)// 限定畫面只能往右移
                .gen());
        cameraArr.add(new Camera.Builder(SCREEN_X, SCREEN_Y / 2)// 創建一個400*300的鏡頭
                .setChaseObj(players.get(1), 5, 8)// 追蹤p2 追焦係數設為30
                //                .setCameraLockDirection(true, true, false, true)// 限定畫面只能往右移
                .setCameraWindowLocation(0, SCREEN_Y / 2)// 鏡頭顯示在視窗的下半邊
                .setCameraStartLocation(0, 0)
                .gen());
        //建構復活點
        rebornPoints = new ArrayList<>();
        //建構磚塊
        bricks = new ArrayList<>();
        //建構金幣
        coins = new ArrayList<>();
        //建構計算金幣的圖
        coinCount = new CoinAnimator(CoinType.COINCOUNT);
        //建構計算怪物的圖
        monsterCount = new MonsterAnimator(MonsterType.PIG);
        //建構怪物
        monsters = new ArrayList<>();
        //建構火球
        skills = new ArrayList<>();
        mapInitialize();  // 地圖載入器方法
//        AudioResourceController.getInstance().loop("resources\\sounds\\background-2.wav", 99);
        AudioResourceController.getInstance().loop(new Path().sound().background(), 99);
    }

    @Override
    public void sceneEnd() {
        this.p1Backgrounds = null;
        this.p2Backgrounds = null;
        this.bricks = null;
        this.cameraArr = null;
        this.coinCount = null;
        this.monsters = null;
        this.players = null;
        this.rebornPoints = null;
        this.skills = null;
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < cameraArr.size(); i++) {
            cameraArr.get(i).start(g);//啟動鏡頭
            if (i == 0) {
                for (int j = 0; j < p1Backgrounds.size(); j++) {
//                    setBackgroundPrint(p1Backgrounds.get(j), cameraArr.get(i));
//                    backgroundOutOfScreen(p1Backgrounds, cameraArr.get(i));
                    if (cameraArr.get(i).isCollision(p1Backgrounds.get(j))) {
                        p1Backgrounds.get(j).paint(g);
                    }
                }
            } else {
                for (int j = 0; j < p2Backgrounds.size(); j++) {
//                    setBackgroundPrint(p2Backgrounds.get(j), cameraArr.get(i));
//                    backgroundOutOfScreen(p2Backgrounds, cameraArr.get(i));
                    if (cameraArr.get(i).isCollision(p2Backgrounds.get(j))) {
                        p2Backgrounds.get(j).paint(g);
                    }
                }
            }
            for (int j = 0; j < coins.size(); j++) {
                if (cameraArr.get(i).isCollision(coins.get(j))) {
                    // 在鏡頭範圍中才進行繪製金幣
                    coins.get(j).paint(g);
                }
            }

            for (int j = 0; j < rebornPoints.size(); j++) {
                if (cameraArr.get(i).isCollision(rebornPoints.get(j))) {
                    //在鏡頭範圍中繪製重生點
                    rebornPoints.get(j).paint(g);
                }
            }
            for (int j = 0; j < players.size(); j++) {
                if (cameraArr.get(i).isCollision(players.get(j))) {
                    // 在鏡頭範圍中才繪製角色
                    players.get(j).paint(g);
                }
            }

            for (int j = 0; j < monsters.size(); j++) {
                if (cameraArr.get(i).isCollision(monsters.get(j))) {
                    // 在鏡頭範圍中才進行繪製怪物
                    monsters.get(j).paint(g);
                }
            }

            for (int j = 0; j < skills.size(); j++) {
                if (cameraArr.get(i).isCollision(skills.get(j))) {
                    //在鏡頭範圍中繪製火球
                    skills.get(j).paint(g);
                }
            }
            for (int j = 0; j < bricks.size(); j++) {
                if (cameraArr.get(i).isCollision(bricks.get(j))) {
                    // 在鏡頭範圍中才進行繪製磚塊
                    bricks.get(j).paint(g);
                }
            }
            if (p1Ink && i == 0) {
                g.drawImage(imgInk, cameraArr.get(i).collider().left() + WINDOW_WIDTH/15, cameraArr.get(i).collider().top(), WINDOW_WIDTH, WINDOW_HEIGHT/2, null);
            }
            if (p2Ink && i == 1) {
                g.drawImage(imgInk, cameraArr.get(i).collider().left() +  WINDOW_WIDTH/15, cameraArr.get(i).collider().top(), WINDOW_WIDTH, WINDOW_HEIGHT/2, null);
            }
            cameraArr.get(i).end(g);
        }
        g.fillRect(0, SCREEN_Y / 2, SCREEN_X, 5);//雙鏡頭分隔線
        //繪製距離
        g.drawImage(pointCountOrange, DISTANCE_X - 50, DISTANCE_Y - 40, 350, 60, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", Font.BOLD, 30));
        g.drawString(/*"残り時間:" + passedTime + "\t秒" +*/"走る距離: " + players.get(0).collider().right() / 10 + " M", DISTANCE_X, DISTANCE_Y);
        g.drawImage(pointCount, DISTANCE_X - 50, SCREEN_Y - 60, 350, 60, null);
        g.drawString(/*"残り時間:" + passedTime + "\t秒"+*/"走る距離: " + players.get(1).collider().right() / 10 + " M", DISTANCE_X, SCREEN_Y - 20);
        //繪製金幣獲得數量-------->在不同電腦上會顯示不同位置
        coinCount.paint(COINCOUNT_X, COINCOUNT_Y, COINCOUNT_X + 41, COINCOUNT_Y + 48, g);
        monsterCount.paint(MonsterType.PIG, Direction.LEFT, COINCOUNT_X, MONSTERCOUNT_Y, COINCOUNT_X + 41, MONSTERCOUNT_Y + 48, g);
        g.setColor(Color.WHITE);
//        g.setFont(new Font("Dialog", Font.BOLD, 40));
        g.drawString("X" + players.get(0).getCoinCount(), COINCOUNT_X + 41, COINCOUNT_Y + 40);
        g.drawString("X" + players.get(0).getMonsterCount(), COINCOUNT_X + 41, MONSTERCOUNT_Y + 40);
        coinCount.paint(COINCOUNT_X, SCREEN_Y - 54, COINCOUNT_X + 41, +SCREEN_Y - 6, g);
        monsterCount.paint(MonsterType.PIG, Direction.LEFT, COINCOUNT_X, SCREEN_Y - 112, COINCOUNT_X + 41, SCREEN_Y - 64, g);
        g.drawString("X" + players.get(1).getCoinCount(), COINCOUNT_X + 41, SCREEN_Y - 18);
        g.drawString("X" + players.get(1).getMonsterCount(), COINCOUNT_X + 41, SCREEN_Y - 68);
        g.drawImage(naruto, players.get(0).collider().right() / 100 * 3, SCREEN_Y / 2 - 64,
                players.get(0).collider().right() / 100 * 3 + 64, SCREEN_Y / 2,
                0, 0, 115, 142, null);
        g.drawImage(sasuke, players.get(1).collider().right() / 100 * 3, SCREEN_Y / 2,
                players.get(1).collider().right() / 100 * 3 + 64, SCREEN_Y / 2 + 64,
                0, 0, 115, 142, null);
        //繪製時間
        if (gameTime != -99) {
            g.setColor(Color.BLACK);
            g.drawString("残り時間:" + passedTime + "\t秒", TIME_X + 3, TIME_Y + 24);
            g.drawString("残り時間:" + passedTime + "\t秒", TIME_X + 3, SCREEN_Y - 24);
            g.setColor(Color.WHITE);
            g.drawString("残り時間:" + passedTime + "\t秒", TIME_X, TIME_Y + 24);
            g.drawString("残り時間:" + passedTime + "\t秒", TIME_X, SCREEN_Y - 24);
        } else {
            g.drawString("残り時間:∞", TIME_X, TIME_Y + 24);
            g.drawString("残り時間:∞", TIME_X, SCREEN_Y - 24);
        }
        if (isFinish) {
            g.drawImage(imgFinish, 500, SCREEN_Y / 2 - 300, null);
            g.drawImage(imgFinish, 500, SCREEN_Y / 2 + 200, null);
        }

    }

    @Override
    public void update() {
        //時間更新
        if (gameTime != -99) {
            passedTime = ((double) (int) ((gameTime - (System.nanoTime() - beginTime) / 1000000000) * 10) / 10);
            if (passedTime <= 0) {
                passedTime = 0;
                isFinish = true;
                finishDelay.play();
            }
        }
        if (finishDelay.count()) {
            SceneController.instance().change(new CaulateScene(players.get(0).collider().right() / 10,
                    players.get(0).getCoinCount(),
                    players.get(0).getMonsterCount(),
                    players.get(1).collider().right() / 10,
                    players.get(1).getCoinCount(),
                    players.get(1).getMonsterCount()));
        }
        if (!isFinish) {
            // 多鏡頭更新
            for (int i = 0; i < cameraArr.size(); i++) {
                cameraArr.get(i).update();
                if (i == 0) {
                    for (int j = 0; j < p1Backgrounds.size(); j++) {
                        setBackgroundPrint(p1Backgrounds.get(j), cameraArr.get(i));
                        backgroundOutOfScreen(p1Backgrounds, cameraArr.get(i));
                    }
                } else {
                    for (int j = 0; j < p2Backgrounds.size(); j++) {
                        setBackgroundPrint(p2Backgrounds.get(j), cameraArr.get(i));
                        backgroundOutOfScreen(p2Backgrounds, cameraArr.get(i));
                    }
                }
            }
            //磚塊更新
            for (int i = 0; i < bricks.size(); i++) {
                Brick brick = bricks.get(i);
                brick.update();
                if (brick.getVanish()) {//如果磚塊的vanish是ture就讓它消失
                    bricks.remove(i--);
                }
                if (brick.getBrickType() == BrickType.FORT_L && brick.getBornBullet()) {//若磚塊為砲管則新增飛彈
                    int shoot = Global.random(0, 100);
                    if (shoot <= 80) {
                        monsters.add(new Monster(brick.collider().left() - 100, brick.collider().top(),/////新增子彈
                                brick.collider().width(), brick.collider().height(),
                                MonsterType.BULLET, new Vector(-3, 0), new Delay(360), Direction.LEFT));
                    }
                }
            }
            //怪物更新
            for (int i = 0; i < monsters.size(); i++) {
                Monster monster = monsters.get(i);
                monster.update();
                if (monster.getMonsterType() == MonsterType.GREENBOSS) {
                    if (Global.random(0, 100) >= 99) {
                        if (monster.getDirection() == Direction.LEFT) {
                            monsters.add(new Monster(monster.collider().left() - 50, monster.collider().top(),/////新增子彈
                                    UNIT_X, UNIT_X,
                                    MonsterType.BULLET, new Vector(-7, 0), new Delay(360), Direction.LEFT));
                            monsters.add(new Monster(monster.collider().left() - 50, monster.collider().top(),/////新增子彈
                                    UNIT_X, UNIT_X,
                                    MonsterType.BULLET, new Vector(-7, 1), new Delay(360), Direction.LEFT));
                            monsters.add(new Monster(monster.collider().left() - 50, monster.collider().top(),/////新增子彈
                                    UNIT_X, UNIT_X,
                                    MonsterType.BULLET, new Vector(-7, -1), new Delay(360), Direction.LEFT));
                        } else {
                            monsters.add(new Monster(monster.collider().right() + 50, monster.collider().top(),/////新增子彈
                                    UNIT_X, UNIT_X,
                                    MonsterType.BULLET, new Vector(5, 0), new Delay(360), Direction.RIGHT));
                            monsters.add(new Monster(monster.collider().right() + 50, monster.collider().top(),/////新增子彈
                                    UNIT_X, UNIT_X,
                                    MonsterType.BULLET, new Vector(5, 1), new Delay(360), Direction.RIGHT));
                            monsters.add(new Monster(monster.collider().right() + 50, monster.collider().top(),/////新增子彈
                                    UNIT_X, UNIT_X,
                                    MonsterType.BULLET, new Vector(5, -1), new Delay(360), Direction.RIGHT));
                        }
                    }
                }
                if (monster.getMonsterType() == MonsterType.KAPA || monster.getMonsterType() == MonsterType.PIG) {
                    //改變豬 河童 Yspeed
                    monsterFallSpeed(monster);
                    monster.setCanMove(true);
                    //用替身判斷角色站在磚塊上
                    monsterTouchBrickY(bricks, monster);
                    if (monster.getCanMove()) {
                        //角色向下掉
                        monster.fall();
                    }
                }
                if (monster.getMonsterType() == MonsterType.BULLET) {
                    if (monsters.get(i).getState() != Monster.State.NORMAL || monster.getRemove()) {//若remove為true則remove
                        monsters.remove(i--);
                    }
                    for (int n = 0; n < bricks.size(); n++) {
                        if (monster.isCollision(bricks.get(n)) && monster.getMonsterType() == MonsterType.BULLET) {  // 若第n項磚塊碰到子彈
                            monster.setState(Monster.State.BOOM);   // 子彈狀態為:爆炸                              
                            break;
                        }
                    }
                }
            }
            //金幣更新
            for (int i = 0; i < coins.size(); i++) {
                coins.get(i).update();
            }
            //重生點更新
            for (int i = 0; i < rebornPoints.size(); i++) {
                rebornPoints.get(i).update();
            }
            //火球更新
            for (int i = 0; i < skills.size(); i++) {
                skills.get(i).update();
                skillHitMonster(monsters, skills.get(i));
                if (skills.get(i).isRemove()) {
                    skills.remove(i--);
                }
//                //火球是否相撞
//                if (skills.size() >= 2) {
//                    SkillCollision(skills);
////                break;
//                }
            }
            //判斷角色動作
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (Global.IS_DEBUG) {
//                System.out.println(i + 1 + " " + player.playerAnimator().State() + player.collider().centerX() + " " + player.collider().centerY());
                }
                //更新角色
                player.update();
                if (player.playerAnimator().State() != State.DEAD) {
                    //改變角色Yspeed
                    playerFallSpeed(player);
                    player.setCanMove(true);
                    //用替身判斷角色站在磚塊上
                    playerTouchBrickY(bricks, player);
                    if (player.getCanMove()) {
                        //角色向下掉
                        player.fall();
                        if (player.playerAnimator().State() != State.JUMP) {
                            player.setState(State.JUMP);
                        }
                        player.setJumping(true);
                    }
                    //判斷角色踩到怪物
                    playerStepOnMonster(monsters, player);
                    //判斷角色互相踩到頭
                    playerStepOnHead(players, player);
                    player.setCanMove(true);
                    //判斷角色是否撞牆
                    playerTouchBrickX(bricks, player);
                    if (player.getCanMove()) {
                        //背景移動
                        if (i == 0) {
                            backgroundMove(p1Backgrounds, player);
                        } else {
                            backgroundMove(p2Backgrounds, player);
                        }
                        //角色移動
                        player.move();
                    }
                    //判斷角色是否碰到怪物
                    playerHitMonster(monsters, player);
                    //判斷角色是否相撞
                    playerCollison(players, player);
                    //判斷吃金幣
                    playerTouchCoin(player, coins);
                    //判斷重置重生點
                    playerSetRebornPoint(rebornPoints, player);
                }
            }
            //判斷是否交換位置
            if (players.get(0).isChange()) {
                players.get(0).playerChangePosition(players.get(1));
            }
            if (players.get(1).isChange()) {
                players.get(1).playerChangePosition(players.get(0));
            }
            //墨汁重製
            if (p1InkDelay.count()) {
                p1Ink = false;
                p1InkDelay.stop();
            }
            if (p2InkDelay.count()) {
                p2Ink = false;
                p2InkDelay.stop();
            }
            coinCount.update();
        }
    }

    private void playerStepOnHead(ArrayList<Player> players, Player player) {//踩到角色的頭
        for (int i = 0; i < players.size(); i++) {
            Player player2 = players.get(i);
            if (player == player2) {
                ;
            } else if (player.isCollision(player2)
                    && player.collider().top() < player2.collider().top()
                    && player.collider().left() != player2.collider().right()
                    && player.collider().right() != player2.collider().left() && player.isJumping()
                    && player2.playerAnimator().State() != State.DEAD) {
                player.jump(40);//踩到的角色彈跳
                player2.setYSpeed(5);//被踩到的往下掉落
            }
        }
    }

    private void playerCollison(ArrayList<Player> players, Player player) {//角色間碰撞
        for (int i = 0; i < players.size(); i++) {
            Player player2 = players.get(i);
            if (player == player2 || player.isJumping()) {
                ;
            } else if (player.isCollision(player2)
                    && player.collider().bottom() == player2.collider().bottom()
                    && player.collider().top() == player2.collider().top()
                    && player2.playerAnimator().State() != State.DEAD) {
                if (player.isInvincible() && !player2.isInvincible()) {
                    AudioResourceController.getInstance().play(new Path().sound().dead());
                    player2.playerAnimator().setState(State.DEAD);//可以撞死對方
                }
                if (player.collider().right() > player2.collider().left()
                        && player.collider().left() < player2.collider().left()) {
                    player.changeRight(player2.collider().left());
                    player2.translateX(5);//往左推
                } else if (player.collider().left() < player2.collider().right()
                        && player.collider().right() > player2.collider().right()) {
                    player.changeLeft(player2.collider().right());
                    player2.translateX(-5);//往右推
                }
            }
        }
    }

    private void playerTouchBrickY(ArrayList<Brick> bricks, Player player) {//判斷Y軸碰撞
        Player tmp = new Player(player.collider().centerX(), player.collider().centerY());//建立替身
        tmp.translateY(player.getYSpeed());//用替身判斷y方向的移動會不會碰到障礙
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (tmp.isCollision(brick) /*&& tmp.collider().left() != brick.collider().right()
                    && tmp.collider().right() != brick.collider().left()*/) {
                if (tmp.collider().bottom() > brick.collider().top()
                        && tmp.collider().top() < brick.collider().top()) {//從上往下撞
                    player.changeBottom(brick.collider().top());
                    player.setJumping(false);//碰到地板將isjumping設為false
                    player.setYSpeed(0);//y方向速度設為0
                    player.setCanMove(false);//因為替身碰撞到磚塊，將本尊可移動設為flase
                    player.setBrickSpeed(brick.getBrickSpeed());//設定方塊加速度
                    if (brick.getBrickType() == BrickType.MOVEBRICK
                            || brick.getBrickType() == BrickType.MOVE_BRICK_2
                            || brick.getBrickType() == BrickType.STAIR) {//判斷方塊種類
                        if (player.playerAnimator().State() != State.WALK
                                && player.playerAnimator().State() != State.DEAD) {//站在會動的平台上速度會跟平台一樣
                            if (player.getSpeed().vx() == 0) {//沒有按方向鍵時給速度
                                player.setSpeed(brick.getVector());
                                player.playerAnimator().setState(State.STOP);
                            }
                        }
                        break;
                    }
                    if (brick.getBrickType() == BrickType.VANISH_MOGU_M
                            || brick.getBrickType() == BrickType.VANISH_MOGU_L
                            || brick.getBrickType() == BrickType.VANISH_MOGU_R) {//踩到會消失的磚塊會讓磚塊的消失delay開始跑   
                        if (!brick.getVanish()) {//還沒被踩過為flase
                            brick.getDelay().loop();//用delay算時間時間到就會將vanish設為true
                        }
                        break;
                    }
                    if (brick.getBrickType() == BrickType.ENDING) {
                        isFinish = true;
                        finishDelay.play();
                        player.setCoinCount(player.getCoinCount()+100);
                        break;
                    }
                    if (brick.getBrickType() == BrickType.SPRING) {//踩到彈簧往上跳80
                        AudioResourceController.getInstance().shot(new Path().sound().spring());
                        player.jump(80);
                        break;
                    }
                    if (player.playerAnimator().State() != State.WALK
                            && player.playerAnimator().State() != State.STOP
                            && player.playerAnimator().State() != State.SQUAT
                            && player.playerAnimator().State() != State.DEAD) {//設定踩到磚塊時的狀態
                        if (player.getSpeed().vx() == 0 && !player.isSquating()) {//沒按方向的時候STOP
                            player.setState(State.STOP);
                        } else {
                            player.setState(State.WALK);
                        }
                    }
                } else if (tmp.collider().top() < brick.collider().bottom()
                        && tmp.collider().bottom() > brick.collider().bottom()) {//從下往上撞到磚塊
                    player.changeTop(brick.collider().bottom() + 1);//設定top為磚塊bottom+1
                    player.translateY(10);//角色往下移動10
                    player.setYSpeed(0);//y方向設為0
                    player.setCanMove(false);//因為替身碰撞到磚塊，將本尊可移動設為flase
                    if (brick.getCrash()) {//若是磚塊為可破壞的則remove
                        AudioResourceController.getInstance().shot(new Path().sound().breakBrick());
                        bricks.remove(i--);
                        return;
                    }
                    if (brick.getBrickType() == BrickType.BRICK_ASK) {//若是磚塊為?磚且為有技能的
                        if (brick.getReload() && player.isCanGetEffect()) {
                            AudioResourceController.getInstance().play(new Path().sound().magicBrick());
                            int effectNum = Global.random(0, 100);
                            if (effectNum <= 10) {
                                player.effect(3);//換位置
                            }
                            if (effectNum > 10 && effectNum <= 25) {//墨汁
                                if (player == players.get(0)) {
                                    p2Ink = true;
                                    p2InkDelay.play();
                                } else {
                                    p1Ink = true;
                                    p1InkDelay.play();
                                }
                            }
                            if (effectNum > 25 && effectNum <= 45) {
                                player.effect(1);//加速
                            }
                            if (effectNum > 45 && effectNum <= 70) {
                                player.effect(2);//無敵
                            }
                            if (effectNum > 70 && effectNum <= 85) {//減速
                                if (player == players.get(0)) {
                                    players.get(1).setDownSpeed();
                                } else {
                                    players.get(0).setDownSpeed();
                                }
                            }
                            if (effectNum > 85 && effectNum <= 100) {//混亂
                                if (player == players.get(0)) {
                                    players.get(1).chaos();
                                } else {
                                    players.get(0).chaos();
                                }
                            }
                            brick.setReload(false);
                            brick.getDelay().loop();
                            player.setCanGetEffect(false);
                            player.getSkillDelay().loop();
                        }
                        
                    }
                    break;
                }
            }
        }
    }

    private void playerTouchBrickX(ArrayList<Brick> bricks, Player player) {//判斷x軸碰撞
        Player tmp = new Player(player.collider().centerX(), player.collider().centerY());//創建替身
        tmp.translateX((int) player.getSpeed().vx());//替身移動x方向速度
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (tmp.isCollision(brick)
                    && tmp.collider().bottom() != brick.collider().top()
                    && tmp.collider().top() != brick.collider().bottom()) {
                if (tmp.collider().right() > brick.collider().left()
                        && tmp.collider().left() < brick.collider().left()
                        && !player.isSquating()
                        && player.playerAnimator().State() != State.SQUAT) {//撞到磚塊左邊
                    player.changeRight(brick.collider().left() - 1);//改變本體right為磚塊left-1
                    player.setCanMove(false);//因為替身碰到磚塊，將本尊可移動設為false
                    break;
                } else if (tmp.collider().left() < brick.collider().right()
                        && tmp.collider().right() > brick.collider().right()
                        && !player.isSquating()
                        && player.playerAnimator().State() != State.SQUAT) {
                    player.changeLeft(brick.collider().right() + 1);//改變本體left為磚塊right+1
                    player.setCanMove(false);//因為替身碰到磚塊，將本尊可移動設為false
                }
                break;
            }
        }
    }

    private void playerStepOnMonster(ArrayList<Monster> monsters, Player player) {//踩到怪
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            if (player.isCollision(monsters.get(i))) {
                if (player.collider().top() < monster.collider().top()
                        && player.collider().bottom() < monster.collider().bottom()
                        && player.isJumping()) {   // 魔王也踩不死
                    if (monster.getMonsterType() == MonsterType.BULLET
                            || monster.getMonsterType() == MonsterType.GHOST
                            || monster.getMonsterType() == MonsterType.PIG
                            || monster.getMonsterType() == MonsterType.TACO
                            || monster.getMonsterType() == MonsterType.KAPA) {
                        if (monster.getMonsterType() != MonsterType.KAPA) {
                            AudioResourceController.getInstance().play(new Path().sound().pop());
                            monsters.remove(i--);//remove怪物
                            player.setMonsterCount(player.getMonsterCount() + 1);
                        }
                        player.jump(20);//角色向上彈跳
                    }
                } else if (player.collider().top() > monster.collider().top() && !player.isInvincible()) {
                    if (!IS_DEBUG) {
                        AudioResourceController.getInstance().play(new Path().sound().dead());
                        player.setState(State.DEAD);
                    }
                }
                break;
            }
        }
    }

    private void playerHitMonster(ArrayList<Monster> monsters, Player player) {//碰到怪
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            if (player.isCollision(monster)) {
                if (monster.getMonsterType() == MonsterType.PIRANHA || monster.getMonsterType() == MonsterType.THORN_UP
                        || monster.getMonsterType() == MonsterType.THORN_DOWN || monster.getMonsterType() == MonsterType.THORN_RIGHT
                        || monster.getMonsterType() == MonsterType.THORN_LEFT || monster.getMonsterType() == MonsterType.FIREPILLAR) {//碰到花直接死
                    if (!player.isInvincible()) {
                        if (!IS_DEBUG) {
                            AudioResourceController.getInstance().play(new Path().sound().dead());
                            player.setState(State.DEAD);
                        }
                    }
                }
                if (player.collider().top() <= monster.collider().top()
                        && player.collider().bottom() >= monster.collider().bottom()
                        && !player.isInvincible()) {//撞擊到角色身體
                    if (!IS_DEBUG) {
                        AudioResourceController.getInstance().play(new Path().sound().dead());
                        player.setState(State.DEAD);
                    }
                }
                if (player.isInvincible() // 無敵狀態
                        && monster.getMonsterType() != MonsterType.THORN_DOWN //刺也不能踩死
                        && monster.getMonsterType() != MonsterType.THORN_LEFT
                        && monster.getMonsterType() != MonsterType.THORN_RIGHT
                        && monster.getMonsterType() != MonsterType.THORN_UP
                        && monster.getMonsterType() != MonsterType.FIREPILLAR
                        && monster.getMonsterType() != MonsterType.GREENBOSS) { // 噴火無敵
                    AudioResourceController.getInstance().play(new Path().sound().pop());
                    monsters.remove(i--);
                    player.setMonsterCount(player.getMonsterCount() + 1);
                }
                break;
            }
        }
    }

    private void playerTouchCoin(Player player, ArrayList<Coin> coins) {//碰到錢
        for (int i = 0; i < coins.size(); i++) {
            if (player.isCollision(coins.get(i))) {
                AudioResourceController.getInstance().shot(new Path().sound().coin());
                coins.remove(i--);
                player.setCoinCount(player.getCoinCount() + 1);
                break;
            }
        }
    }

    private void playerSetRebornPoint(ArrayList<RebornPoint> rebornPoints, Player player) {//設定重生點
        for (int i = 0; i < rebornPoints.size(); i++) {
            if (player.collider().left() > rebornPoints.get(i).collider().left()) {//超過重生點就設定重生點
//                AudioResourceController.getInstance().play(new Path().sound().coin());
                player.setRebornPoint(rebornPoints.get(i).collider().centerX(),
                        rebornPoints.get(i).collider().centerY());//設定重生點
                rebornPoints.get(i).getRebornPointAnimator().setType(RebornPointAnimator.RebornPointType.TOUCHED);//改變旗子
            }
        }
    }

    private void playerFallSpeed(Player player) {//改變角色y方向速度
        if (player.getYSpeed() < 0) {
            player.setYSpeed((int) (player.getYSpeed() * 0.9));
            player.setJumping(true);
        }
        if (player.getYSpeed() >= 0) {
            player.setYSpeed(player.getYSpeed() + gravity);
        }
    }

    private void monsterTouchBrickY(ArrayList<Brick> bricks, Monster monster) {//判斷Y軸碰撞
        //Monster tmp = new Monster(monster.collider().centerX(), monster.collider().centerY());//建立替身
        monster.translateY(monster.getYSpeed());//判斷y方向的移動會不會碰到障礙
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (monster.isCollision(brick) /*&& tmp.collider().left() != brick.collider().right()
                    && tmp.collider().right() != brick.collider().left()*/) {
                if (monster.collider().bottom() > brick.collider().top()
                        && monster.collider().top() < brick.collider().top()) {//從上往下撞
                    monster.changeBottom(brick.collider().top());
                    monster.setYSpeed(0);//y方向速度設為0
                    monster.setCanMove(false);//因為替身碰撞到磚塊，將本尊可移動設為flase
                }
                break;
            }
        }
    }

    private void monsterFallSpeed(Monster monster) {//改變角色y方向速度
        if (monster.getYSpeed() < 0) {
            monster.setYSpeed((int) (monster.getYSpeed() * 0.9));
        }
        if (monster.getYSpeed() >= 0) {
            monster.setYSpeed(monster.getYSpeed() + gravity);
        }
    }

    private void SkillTouchAll(ArrayList<Monster> monsters, ArrayList<Player> players, Skill skill) { // 火球攻擊對象
        for (int i = 0; i < this.monsters.size(); i++) {
            if (monsters.get(i).isCollision(skill)
                    && monsters.get(i).getMonsterType() != MonsterType.PIRANHA
                    && monsters.get(i).getMonsterType() != MonsterType.THORN_DOWN //刺也不能被招式打掉
                    && monsters.get(i).getMonsterType() != MonsterType.THORN_LEFT
                    && monsters.get(i).getMonsterType() != MonsterType.THORN_RIGHT
                    && monsters.get(i).getMonsterType() != MonsterType.THORN_UP
                    && monsters.get(i).getMonsterType() != MonsterType.FIREPILLAR // 噴火也不會被打掉
                    && monsters.get(i).getMonsterType() != MonsterType.GREENBOSS) {  // 魔王不怕技能
                if (monsters.get(i).getMonsterType() != MonsterType.BULLET) {
                    skill.setRemove(true);
                }
                AudioResourceController.getInstance().play(new Path().sound().boom());
                monsters.remove(i--);
                if (skill.getSkillAnimator().getSkillType() == SkillType.RASENGAN) {
                    players.get(0).setMonsterCount(players.get(0).getMonsterCount() + 1);
                } else {
                    players.get(1).setMonsterCount(players.get(1).getMonsterCount() + 1);
                }
                break;
            }
        }
    }

    private void skillHitMonster(ArrayList<Monster> monsters, Skill skill) { // 火球攻擊對象
        for (int i = 0; i < this.monsters.size(); i++) {
            if (monsters.get(i).isCollision(skill)
                    && monsters.get(i).getMonsterType() != MonsterType.PIRANHA
                    && monsters.get(i).getMonsterType() != MonsterType.THORN_DOWN //刺也不能被招式打掉
                    && monsters.get(i).getMonsterType() != MonsterType.THORN_LEFT
                    && monsters.get(i).getMonsterType() != MonsterType.THORN_RIGHT
                    && monsters.get(i).getMonsterType() != MonsterType.THORN_UP
                    && monsters.get(i).getMonsterType() != MonsterType.FIREPILLAR // 噴火也不會被打掉
                    && monsters.get(i).getMonsterType() != MonsterType.GREENBOSS) {  // 魔王不怕技能
                if (monsters.get(i).getMonsterType() != MonsterType.BULLET) {
                    skill.setRemove(true);
                }
                AudioResourceController.getInstance().play(new Path().sound().boom());
                if (skill.getSkillAnimator().getSkillType() == SkillType.RASENGAN
                        && monsters.get(i).getMonsterType() != MonsterType.BULLET) {
                    players.get(0).setMonsterCount(players.get(0).getMonsterCount() + 1);
                }
                if (skill.getSkillAnimator().getSkillType() == SkillType.FIRE
                        && monsters.get(i).getMonsterType() != MonsterType.BULLET) {
                    players.get(1).setMonsterCount(players.get(1).getMonsterCount() + 1);
                }
                monsters.remove(i--);
                break;
            }
        }
    }

    private void playerShotFire(Player player, SkillType skillType) {
        if (player.getDirection() == Direction.RIGHT) {
            skills.add(new Skill(player.collider().centerX() + 20,
                    player.collider().centerY() - 30,
                    120, 120, skillType, player.getDirection(), new Delay(120)));
        } else {
            skills.add(new Skill(player.collider().centerX() - 20,
                    player.collider().centerY() - 30,
                    120, 120, skillType, player.getDirection(), new Delay(120)));
        }
        player.setCanShot(false);
        player.getFireBallDelay().play();
    }

    private void SkillCollision(ArrayList<Skill> Skills) {
        Skill fireball = Skills.get(0);
        for (int i = 1; i < Skills.size(); i++) {
            if (fireball.isCollision(Skills.get(i))) {
                AudioResourceController.getInstance().play(new Path().sound().boom());
                Skills.clear();
            }
        }
    }

    private void setBackgroundPrint(Background background, Camera camera) {
        //設定左邊
        if (background.collider().left() < camera.collider().left()) {
            background.setPaintLeft(camera.collider().left());
        } else {
            background.setPaintLeft(background.painter().left());
        }
        //設定右邊
        if (background.collider().right() > camera.collider().right()) {
            background.setPaintRight(camera.collider().right());
        } else {
            background.setPaintRight(background.painter().right());
        }
        //設定上面
        if (background.collider().top() < camera.collider().top()) {
            background.setPaintTop(camera.collider().top());
        } else {
            background.setPaintTop(background.painter().top());
        }
        //設定下面
        if (background.collider().bottom() > camera.collider().bottom()) {
            background.setPaintBottom(camera.collider().bottom());
        } else {
            background.setPaintBottom(background.painter().bottom());
        }

    }

    private void backgroundMove(ArrayList<Background> backgrounds, Player player) {
        for (int i = 2; i < backgrounds.size(); i++) {
            backgrounds.get(i).translateX((int) player.getSpeed().vx() / 8);
        }
        Background backgrounds2 = backgrounds.get(2);
        Background backgrounds3 = backgrounds.get(3);
//        System.out.println("鏡頭1左" + cameraArr.get(0).collider().left() + "鏡頭右" + cameraArr.get(0).collider().right() + "鏡頭上" + cameraArr.get(0).collider().top() + "鏡頭下" + cameraArr.get(0).collider().bottom());
////        System.out.println("鏡頭2左"+cameraArr.get(1).collider().left()+"鏡頭右"+cameraArr.get(1).collider().right());
//        System.out.println("地圖1左" + backgrounds0.collider().left() + "地圖1右" + backgrounds0.collider().right() + "地圖印左" + backgrounds0.getPaintLeft() + "地圖印右" + backgrounds0.getPaintRight());
//        System.out.println("地圖2左" + backgrounds1.collider().left() + "地圖2右" + backgrounds1.collider().right() + "地圖印左" + backgrounds1.getPaintLeft() + "地圖印右" + backgrounds1.getPaintRight());
//        System.out.println("地圖3左" + backgrounds2.collider().left() + "地圖3右" + backgrounds2.collider().right() + "地圖印左" + backgrounds2.getPaintLeft() + "地圖印右" + backgrounds2.getPaintRight());
//        System.out.println("地圖4左" + backgrounds3.collider().left() + "地圖3右" + backgrounds3.collider().right() + "地圖印左" + backgrounds3.getPaintLeft() + "地圖印右" + backgrounds3.getPaintRight());
//        System.out.println("地圖1上" + backgrounds0.collider().top() + "地圖1下" + backgrounds0.collider().bottom() + "地圖印上" + backgrounds0.getPaintTop() + "地圖印下" + backgrounds0.getPaintBottom());
//        System.out.println("地圖2上" + backgrounds1.collider().top() + "地圖2下" + backgrounds1.collider().bottom() + "地圖印上" + backgrounds1.getPaintTop() + "地圖印下" + backgrounds1.getPaintBottom());
//        System.out.println("地圖3上" + backgrounds2.collider().top() + "地圖3下" + backgrounds2.collider().bottom() + "地圖印上" + backgrounds2.getPaintTop() + "地圖印下" + backgrounds2.getPaintBottom());
//        System.out.println("地圖4上" + backgrounds3.collider().top() + "地圖3下" + backgrounds3.collider().bottom() + "地圖印上" + backgrounds3.getPaintTop() + "地圖印下" + backgrounds3.getPaintBottom());
//        backgrounds2.translateX((int) player.getSpeed().vx() / 8);
//        backgrounds3.translateX((int) player.getSpeed().vx() / 8);
    }

    private void backgroundOutOfScreen(ArrayList<Background> backgrounds, Camera camera) {
        for (int i = 0; i < backgrounds.size(); i++) {
            if (i % 2 == 0) {
                //往右跑
                if (backgrounds.get(i).painter().right() <= camera.collider().left()) {
                    backgrounds.get(i).setRect(new Rect(backgrounds.get(i + 1).collider().right() - 10,
                            backgrounds.get(i).collider().top(),
                            backgrounds.get(i + 1).collider().right() + backgrounds.get(i).collider().width() - 10,
                            backgrounds.get(i).collider().bottom()));
                }
                //往左跑
                if (backgrounds.get(i).painter().left() >= camera.collider().right()) {
                    backgrounds.get(i).setRect(new Rect(backgrounds.get(i + 1).collider().left() - backgrounds.get(i).collider().width() + 10,
                            backgrounds.get(i).collider().top(),
                            backgrounds.get(i + 1).collider().left() + 10,
                            backgrounds.get(i).collider().bottom()));
                }
            } else {
                //往右跑
                if (backgrounds.get(i).painter().right() <= camera.collider().left()) {
                    backgrounds.get(i).setRect(new Rect(backgrounds.get(i - 1).collider().right() - 10,
                            backgrounds.get(i).collider().top(),
                            backgrounds.get(i - 1).collider().right() + backgrounds.get(i).collider().width() - 10,
                            backgrounds.get(i).collider().bottom()));
                }
                //往左跑
                if (backgrounds.get(i).painter().left() >= camera.collider().right()) {
                    backgrounds.get(i).setRect(new Rect(backgrounds.get(i - 1).collider().left() - backgrounds.get(i).collider().width() + 10,
                            backgrounds.get(i).collider().top(),
                            backgrounds.get(i - 1).collider().left() + 10,
                            backgrounds.get(i).collider().bottom()));
                }
            }
        }
//        Background backgrounds0 = backgrounds.get(0);
//        Background backgrounds1 = backgrounds.get(1);
//        Background backgrounds2 = backgrounds.get(2);
//        Background backgrounds3 = backgrounds.get(3);
//        //往右跑
//        if (backgrounds0.painter().right() <= camera.collider().left()) {
//            backgrounds0.translateX(BACKGROUND_BOTTOM_WIDTH);
//        }
//        if (backgrounds1.painter().right() <= camera.collider().left()) {
//            backgrounds1.translateX(BACKGROUND_BOTTOM_WIDTH);
//        }
//        if (backgrounds2.painter().right() <= camera.collider().left()) {
//            backgrounds2.translateX(BACKGROUND_MIDDLE_WIDTH);
//        }
//        if (backgrounds3.painter().right() <= camera.collider().left()) {
//            backgrounds3.translateX(BACKGROUND_MIDDLE_WIDTH);
//        }
//        //往左跑
//        if (backgrounds0.painter().left() >= camera.collider().right()) {
//            backgrounds0.translateX(-BACKGROUND_BOTTOM_WIDTH);
//        }
//        if (backgrounds1.painter().left() >= camera.collider().right()) {
//            backgrounds1.translateX(-BACKGROUND_BOTTOM_WIDTH);
//        }
//        if (backgrounds2.painter().left() >= camera.collider().right()) {
//            backgrounds2.translateX(-BACKGROUND_MIDDLE_WIDTH);
//        }
//        if (backgrounds3.painter().left() >= camera.collider().right()) {
//            backgrounds3.translateX(-BACKGROUND_MIDDLE_WIDTH);
//        }
    }

    private void mapInitialize() {//建構地圖
        try {
            ArrayList<MapInfo> mapInfo = new MapLoader("genMap.bmp", "genMap.txt").combineInfo();
            for (MapInfo tmp : mapInfo) {  // 地圖產生器內物件的 {左, 上, 寬, 高}
                switch (tmp.getName()) {
                    case "moguL1": //  需中心座標     會破的蘑菇左端
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.CRASH_MOGU_L1));
                        break;

                    case "moguM1":  // 會破的蘑菇中間
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.CRASH_MOGU_M1));
                        break;

                    case "moguR1": // 會破的蘑菇右邊
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.CRASH_MOGU_R1));
                        break;

                    case "brick1":  // 會破 brick1
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.CRASHBRICK_1));
                        break;

                    case "coin1":       // 錢
                        coins.add(new Coin(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y, CoinType.COIN));
                        break;

                    case "block1":   // 草地
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.BLOCK_GRASS));
                        break;

                    case "block2":  // 土地
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.BLOCK_EARTH));
                        break;

                    case "pipeUp":   // 水管朝上
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() + 5,
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickType.PIPE_UP));
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() - 1, // 食人花
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() + 20,
                                UNIT_X + 5, UNIT_Y * 2,
                                MonsterType.PIRANHA, new Vector(0, 2), new Delay(32)));
                        break;

                    case "pipeBody":  // 水管中段
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY(),
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickType.PIPE_BODY));
                        break;

                    case "pipeDown":  // 水管朝下
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY(),
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickType.PIPE_DOWN));
                        break;

                    case "moguL2":    // 永恆蘑菇左端
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.MOGU_L2));
                        break;

                    case "moguM2":    // 永恆蘑菇中段
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.MOGU_M2));
                        break;

                    case "moguR2":     // 永恆蘑菇右端
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.MOGU_R2));
                        break;

                    case "brick0":  // 不破磚塊
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.BRICK_0));
                        break;

                    case "fort1":     // 砲管向左
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.FORT_L, new Delay(120)));
                        break;

                    case "fort2":     // 砲管向右
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.FORT_R, new Delay(120)));
                        break;

                    case "spring": // 彈簧
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY(),
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickType.SPRING));
                        break;

                    case "brickAsk":    // 問號
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.BRICK_ASK, new Delay(800)));
                        break;

                    case "ending":     // 終點線
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.ENDING));
                        break;

                    case "blockBoss":        // 刺向上
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.NORMALBRICK));
                        break;

                    case "brick":     // 會動 brick(左右)
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X,
                                BrickType.MOVEBRICK, new Vector(3, 0), new Delay(150)));
                        break;

                    case "brick2":      // 上下動
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X * 3,
                                BrickType.MOVE_BRICK_2, new Vector(0, 2), new Delay(200)));
                        break;

                    case "stair":       // 斜動/
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X,
                                BrickType.STAIR, new Vector(2, 2), new Delay(200)));
                        break;

                    case "moguL":// 會消失
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                BrickType.VANISH_MOGU_L, new Delay(30)));
                        break;

                    case "moguM": // 會消失
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                BrickType.VANISH_MOGU_M, new Delay(30)));
                        break;

                    case "moguR": // 會消失
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                BrickType.VANISH_MOGU_R, new Delay(30)));
                        break;

                    case "thorn1": // 刺向上
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                MonsterType.THORN_UP, new Vector(0, 0)));
                        break;
                    case "thorn2": // 刺向下
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                MonsterType.THORN_DOWN, new Vector(0, 0)));
                        break;
                    case "thron3": // 刺向右
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                MonsterType.THORN_RIGHT, new Vector(0, 0)));
                        break;

                    case "thron4": // 刺向左
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                MonsterType.THORN_LEFT, new Vector(0, 0)));
                        break;
                    case "monster1": // 左右(豬)
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 3 / 2, UNIT_Y * 3 / 2,
                                MonsterType.PIG, new Vector(-2, 0), new Delay(100)));
                        break;
                    case "monster2": // 左右(河童)
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 3 / 2, UNIT_Y * 3 / 2,
                                MonsterType.KAPA, new Vector(-2, 0), new Delay(300)));
                        break;

                    case "monster3": // 飛左右(章魚)
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 3 / 2, UNIT_Y * 3 / 2,
                                MonsterType.TACO, new Vector(-3, 0), new Delay(300)));
                        break;

                    case "monster4": // 飛上下
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 3 / 2, UNIT_Y * 3 / 2,
                                MonsterType.GHOST, new Vector(0, 3), new Delay(100)));
                        break;

                    case "monster5": // 大魔王
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() * 3 / 2,
                                UNIT_X * tmp.getSizeX() * 3, UNIT_Y * tmp.getSizeY() * 3,
                                MonsterType.GREENBOSS, new Vector(2, 0), new Delay(120)));
                        break;

                    case "fireDown": // 火上下
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X * tmp.getSizeX(), UNIT_Y * tmp.getSizeY(),
                                MonsterType.FIRE, new Vector(0, 2), new Delay(120)));
                        break;
                    case "fire":  // 噴火
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                32, 30,
                                MonsterType.FIREPILLAR, new Vector(0, 0), new Delay(15)));
                        break;

                    case "pipeUp2": // 水管朝上(無花版本)
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() + 5,
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickType.PIPE_UP));
                        break;

                    case "fortseat": // 重生點
                        rebornPoints.add(new RebornPoint(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY(),
                                UNIT_X * 2, UNIT_Y * 2));
                        break;

                    case "arrow_LEFT":  // 向左箭頭
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.ARROW_LEFT));
                        break;
                    case "arrow_RIGHT":  // 加速箭頭
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickType.ARROW_RIGHT));
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PlayScene.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() { //匿名內部類
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (commandCode / 5 == 0) {
                    if (commandCode == P1_SHOT
                            && players.get(0).canShot()
                            && !players.get(0).isSquating()
                            && players.get(0).playerAnimator().State() != State.DEAD) {
                        playerShotFire(players.get(0), SkillType.RASENGAN);
                        AudioResourceController.getInstance().play(new Path().sound().rasengan());
                    } else {
                        players.get(0).keyPressed(commandCode, trigTime);
                    }
                } else {
                    if (commandCode == P2_SHOT
                            && players.get(1).canShot()
                            && !players.get(1).isSquating()
                            && players.get(1).playerAnimator().State() != State.DEAD) {
                        playerShotFire(players.get(1), SkillType.FIRE);
                        AudioResourceController.getInstance().play(new Path().sound().fireball());
                    } else {
                        players.get(1).keyPressed(commandCode % 5, trigTime);
                    }
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if (commandCode / 5 == 0) {
                    players.get(0).keyReleased(commandCode, trigTime);
                } else {
                    players.get(1).keyReleased(commandCode % 5, trigTime);
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {
//                
            }
        };
    }
}
