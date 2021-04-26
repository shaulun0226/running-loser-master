package game.scene;

import game.camera.Camera;
import game.camera.MapInformation;
import game.controllers.AudioResourceController;
import game.controllers.SceneController;
import game.gameobj.Background;
import game.gameobj.Brick;
import game.gameobj.Coin;
import game.gameobj.Monster;
import game.gameobj.Player;
import game.gameobj.RebornPoint;
import game.gameobj.Rect;
import game.gameobj.Skill;
import game.internet.ClientClass;
import game.utils.BrickAnimator;
import game.utils.CoinAnimator;
import game.utils.CommandSolver;
import game.utils.Delay;
import game.utils.Global;
import static game.utils.Global.*;
import game.utils.MonsterAnimator;
import game.utils.Path;
import game.utils.PlayerAnimator;
import game.utils.RebornPointAnimator;
import game.utils.SkillAnimator;
import game.utils.Vector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import maploader.MapInfo;
import maploader.MapLoader;

public class InternetScene extends Scene {

    private ArrayList<Player> players;//玩家
    private ArrayList<Brick> bricks;//磚塊
    private int gravity;//地圖的重力
    private Camera camera;// 多鏡頭管理
    private ArrayList<RebornPoint> rebornPoints;//重生點
    private ArrayList<Coin> coins;//金幣
    private CoinAnimator coinCount;//畫出右上角的計算金幣
    private ArrayList<Skill> skills;//技能
    private ArrayList<Background> p1Backgrounds;
    private ArrayList<Monster> monsters;   // 怪物

    //時間
    private double beginTime;
    private double passedTime;
    private boolean isFinish;
    private Delay finishDelay;
    private Image imgFinish;

    @Override
    public void sceneBegin() {
        //時間
        beginTime = System.nanoTime();
        finishDelay = new Delay(90);
        finishDelay.stop();
        //地圖
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);// left, top, right, bottom
        p1Backgrounds = new ArrayList<>();
        p1Backgrounds.add(new Background(1280, 480, 2560, 960, Background.BackgroundType.BOTTOM));
        p1Backgrounds.add(new Background(1280 * 3, 480, 2560, 960, Background.BackgroundType.BOTTOM));
        p1Backgrounds.add(new Background(849, 480, 1698, 960, Background.BackgroundType.MIDDLE));
        p1Backgrounds.add(new Background(849 * 3, 480, 1698, 960, Background.BackgroundType.MIDDLE));
        imgFinish = SceneController.instance().irc().tryGetImage(new Path().img().objs().finish());
//        naruto = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHDSmall());
//        sasuke = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHDSmall());
        gravity = 2;
        players = new ArrayList<>();
        players.add(new Player(PlayerAnimator.ActorType.NARUTOHD, 10, 300));
        camera = new Camera.Builder(SCREEN_X, SCREEN_Y)// 創建一個400*300的鏡頭
                .setCameraStartLocation(0, 0)
                .setChaseObj(players.get(0), 5, 8)// 追蹤p1 追焦係數設為30
                .setCameraWindowLocation(0, 0)// 鏡頭顯示在視窗的上半邊
                //                .setCameraLockDirection(true, true, false, true)// 限定畫面只能往右移
                .gen();
        //建構復活點
        rebornPoints = new ArrayList<>();
        //建構磚塊
        bricks = new ArrayList<>();
        //建構金幣
        coins = new ArrayList<>();
        //建構計算金幣的圖
        coinCount = new CoinAnimator(CoinAnimator.CoinType.COINCOUNT);
        //建構怪物
        monsters = new ArrayList<>();
        //建構火球
        skills = new ArrayList<>();
        mapInitialize();  // 地圖載入器方法
//        AudioResourceController.getInstance().loop("resources\\sounds\\background-2.wav", 99);
        AudioResourceController.getInstance().loop("resources\\sounds\\background.wav", 99);
        try {
            ClientClass.getInstance().connect("127.0.0.1", 12345);
        } catch (IOException ex) {
            Logger.getLogger(InternetScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        players.get(0).setId(ClientClass.getInstance().getID());
        ArrayList<String> arrs = new ArrayList<>();
        arrs.add(players.get(0).collider().centerX() + "");
        arrs.add(players.get(0).collider().centerY() + "");
        ClientClass.getInstance().sent(Msg.CONNECT, arrs);
    }

    private class Msg {

        private static final int CONNECT = 0;
        private static final int POSITION = 1;
    }

    @Override
    public void sceneEnd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void paint(Graphics g) {
        camera.start(g);//啟動鏡頭
        for (int j = 0; j < p1Backgrounds.size(); j++) {
            if (camera.isCollision(p1Backgrounds.get(j))) {
                p1Backgrounds.get(j).paint(g);
            }
        }
        for (int j = 0; j < coins.size(); j++) {
            if (camera.isCollision(coins.get(j))) {
                // 在鏡頭範圍中才進行繪製金幣
                coins.get(j).paint(g);
            }
        }
        for (int j = 0; j < rebornPoints.size(); j++) {
            if (camera.isCollision(rebornPoints.get(j))) {
                //在鏡頭範圍中繪製重生點
                rebornPoints.get(j).paint(g);
            }
        }
        for (int j = 0; j < players.size(); j++) {
            if (camera.isCollision(players.get(j))) {
                // 在鏡頭範圍中才繪製角色
                players.get(j).paint(g);
            }
        }

        for (int j = 0; j < monsters.size(); j++) {
            if (camera.isCollision(monsters.get(j))) {
                // 在鏡頭範圍中才進行繪製怪物
                monsters.get(j).paint(g);
            }
        }

        for (int j = 0; j < skills.size(); j++) {
            if (camera.isCollision(skills.get(j))) {
                //在鏡頭範圍中繪製火球
                skills.get(j).paint(g);
            }
        }
        for (int j = 0; j < bricks.size(); j++) {
            if (camera.isCollision(bricks.get(j))) {
                // 在鏡頭範圍中才進行繪製磚塊
                bricks.get(j).paint(g);
            }
        }
        camera.end(g);
    }

    @Override
    public void update() {
        ClientClass.getInstance().consume((int serialNum, int commandCode1, ArrayList<String> strs1) -> {
            System.out.println("!!!!");
            if (serialNum == players.get(0).getId()) {
                return;
            }
            switch (commandCode1) {
                case Msg.CONNECT:
                    boolean isborn = false;
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getId() == serialNum) {
                            isborn = true;
                            break;
                        }
                    }
                    if (!isborn) {
                        System.out.println("!!!!");
                        Player p = new Player(Integer.parseInt(strs1.get(0)) + serialNum, Integer.parseInt(strs1.get(1)));
                        p.setId(serialNum);
                        players.add(p);
                        System.out.println("!!!!");
                        ArrayList<String> arrs = new ArrayList<>();
                        arrs.add(players.get(0).collider().centerX() + "");
                        arrs.add(players.get(0).collider().centerY() + "");
                        ClientClass.getInstance().sent(Msg.CONNECT, arrs);
                    }
                    break;
                case Msg.POSITION:
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getId() == serialNum) {
                            players.get(i).setRect(Rect.genWithCenter(Integer.parseInt(strs1.get(0)), Integer.parseInt(strs1.get(1)), ACTOR_WIDTH, ACTOR_HEIGHT));
                            break;
                        }
                    }
                    break;
            }

        });
        passedTime = ((double) (int) ((GAME_TIME - (System.nanoTime() - beginTime) / 1000000000) * 10) / 10);
        if (passedTime <= 0) {
            passedTime = 0;
            isFinish = true;
            finishDelay.play();
        }
//        if (finishDelay.count()) {
//            SceneController.instance().change(new CaulateScene(players.get(0).collider().right() / 10,
//                    players.get(0).getCoinCount(),
//                    players.get(1).collider().right() / 10,
//                    players.get(1).getCoinCount()));
//        }
        if (!isFinish) {
            // 多鏡頭更新
            camera.update();
            for (int j = 0; j < p1Backgrounds.size(); j++) {
                setBackgroundPrint(p1Backgrounds.get(j), camera);
                backgroundOutOfScreen(p1Backgrounds, camera);

            }
            //磚塊更新
            for (int i = 0; i < bricks.size(); i++) {
                Brick brick = bricks.get(i);
                brick.update();
                if (brick.getVanish()) {//如果磚塊的vanish是ture就讓它消失
                    bricks.remove(i--);
                }
                if (brick.getBrickType() == BrickAnimator.BrickType.FORT_L && brick.getBornBullet()) {//若磚塊為砲管則新增飛彈
                    int shoot = Global.random(0, 100);
                    if (shoot <= 66) {
                        monsters.add(new Monster(brick.collider().left() - 100, brick.collider().top(),/////新增子彈
                                brick.collider().width(), brick.collider().height(),
                                MonsterAnimator.MonsterType.BULLET, new Vector(-3, 0), new Delay(360), Global.Direction.LEFT));
                    }
                }
            }
            //怪物更新
            for (int i = 0; i < monsters.size(); i++) {
                Monster monster = monsters.get(i);
                monster.update();
                if (monster.getRemove()) {//若remove為true則remove
                    monsters.remove(i);
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
                fireBallTouchAll(monsters, players, skills.get(i));
                if (skills.get(i).isRemove()) {
                    skills.remove(i--);
                }
                //火球是否相撞
                if (skills.size() >= 2) {
                    fireBallCollision(skills);
//                break;
                }
            }
            //判斷角色動作
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (Global.IS_DEBUG) {
//                System.out.println(i + 1 + " " + player.playerAnimator().State() + player.collider().centerX() + " " + player.collider().centerY());
                }
                //更新角色
                player.update();
                if (player.playerAnimator().State() != PlayerAnimator.State.DEAD) {
                    //改變角色Yspeed
                    playerFallSpeed(player);
                    player.setCanMove(true);
                    //用替身判斷角色站在磚塊上
                    playerTouchBrickY(bricks, player);
                    if (player.getCanMove()) {
                        //角色向下掉
                        player.fall();
                        if (player.playerAnimator().State() != PlayerAnimator.State.JUMP) {
                            player.setState(PlayerAnimator.State.JUMP);
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
                        backgroundMove(p1Backgrounds, player);
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
//            if (players.get(0).isChange()) {
//                players.get(0).playerChangePosition(players.get(1));
//            }
//            if (players.get(1).isChange()) {
//                players.get(1).playerChangePosition(players.get(0));
//            }
            coinCount.update();
            ArrayList<String> arrs = new ArrayList<>();
            arrs.add(players.get(0).collider().centerX() + "");
            arrs.add(players.get(0).collider().centerY() + "");
            ClientClass.getInstance().sent(Msg.POSITION, arrs);
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
                    && player2.playerAnimator().State() != PlayerAnimator.State.DEAD) {
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
                    && player2.playerAnimator().State() != PlayerAnimator.State.DEAD) {
                if (player.isInvincible() && !player2.isInvincible()) {
                    player2.playerAnimator().setState(PlayerAnimator.State.DEAD);//可以撞死對方
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
                    if (brick.getBrickType() == BrickAnimator.BrickType.MOVEBRICK
                            || brick.getBrickType() == BrickAnimator.BrickType.MOVE_BRICK_2
                            || brick.getBrickType() == BrickAnimator.BrickType.STAIR) {//判斷方塊種類
                        if (player.playerAnimator().State() != PlayerAnimator.State.WALK
                                && player.playerAnimator().State() != PlayerAnimator.State.DEAD) {//站在會動的平台上速度會跟平台一樣
                            if (player.getSpeed().vx() == 0) {//沒有按方向鍵時給速度
                                player.setSpeed(brick.getVector());
                                player.playerAnimator().setState(PlayerAnimator.State.STOP);
                            }
                        }
                    }
                    if (brick.getBrickType() == BrickAnimator.BrickType.VANISH_MOGU_M
                            || brick.getBrickType() == BrickAnimator.BrickType.VANISH_MOGU_L
                            || brick.getBrickType() == BrickAnimator.BrickType.VANISH_MOGU_R) {//踩到會消失的磚塊會讓磚塊的消失delay開始跑   
                        if (!brick.getVanish()) {//還沒被踩過為flase
                            brick.getDelay().loop();//用delay算時間時間到就會將vanish設為true
                        }
                    }
                    if (brick.getBrickType() == BrickAnimator.BrickType.SPRING) {//踩到彈簧往上跳40
                        player.jump(80);
                    }
                    if (player.playerAnimator().State() != PlayerAnimator.State.WALK
                            && player.playerAnimator().State() != PlayerAnimator.State.STOP
                            && player.playerAnimator().State() != PlayerAnimator.State.SQUAT
                            && player.playerAnimator().State() != PlayerAnimator.State.DEAD) {//設定踩到磚塊時的狀態
                        if (player.getSpeed().vx() == 0) {//沒按方向的時候STOP
                            player.setState(PlayerAnimator.State.STOP);
                        } else {
                            player.setState(PlayerAnimator.State.WALK);
                        }
                    }
                } else if (tmp.collider().top() < brick.collider().bottom()
                        && tmp.collider().bottom() > brick.collider().bottom()) {//從下往上撞到磚塊
                    player.changeTop(brick.collider().bottom() + 1);//設定top為磚塊bottom+1
                    player.translateY(10);//角色往下移動5
                    player.setYSpeed(0);//y方向設為0
                    player.setCanMove(false);//因為替身碰撞到磚塊，將本尊可移動設為flase
                    if (brick.getCrash()) {//若是磚塊為可破壞的則remove
                        bricks.remove(i--);
                        return;
                    }
                    if (brick.getBrickType() == BrickAnimator.BrickType.BRICK_ASK) {//若是磚塊為?磚且為有技能的
                        if (brick.getReload() && player.isCanGetEffect()) {
                            int skillNum = Global.random(1, 100);
                            if (skillNum <= 10) {
                                player.effect(3);//換位置
                            } else if (skillNum > 10 && skillNum < 60) {
                                player.effect(1);//加速
                            } else if (skillNum >= 60 && skillNum <= 100) {
                                player.effect(2);//無敵
                            }
                            brick.setReload(false);
                            brick.getDelay().loop();
                            player.setCanGetEffect(false);
                            player.getSkillDelay().loop();
                        }
                    }
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
                        && player.playerAnimator().State() != PlayerAnimator.State.SQUAT) {//撞到磚塊左邊
                    player.changeRight(brick.collider().left() - 1);//改變本體right為磚塊left-1
                    player.setCanMove(false);//因為替身碰到磚塊，將本尊可移動設為false
                } else if (tmp.collider().left() < brick.collider().right()
                        && tmp.collider().right() > brick.collider().right()
                        && !player.isSquating()
                        && player.playerAnimator().State() != PlayerAnimator.State.SQUAT) {
                    player.changeLeft(brick.collider().right() + 1);//改變本體left為磚塊right+1
                    player.setCanMove(false);//因為替身碰到磚塊，將本尊可移動設為false
                }
            }
        }
    }

    private void playerStepOnMonster(ArrayList<Monster> monsters, Player player) {//踩到怪
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            if (player.isCollision(monsters.get(i))) {
                if (player.collider().top() < monster.collider().top()
                        && player.collider().bottom() < monster.collider().bottom()
                        && player.isJumping()
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.PIRANHA //只有踩到怪的上半部碰撞判斷區以及不是花才可以踩死
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.THORN_DOWN //刺也不能踩死
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.THORN_LEFT
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.THORN_RIGHT
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.THORN_UP) {
                    monsters.remove(i--);//remove怪物
//                    monster.setRemove(true);
                    player.jump(20);//角色向上彈跳
                } else if (player.collider().top() > monster.collider().top() && !player.isInvincible()) {
                    player.setState(PlayerAnimator.State.DEAD);
                }
            }
        }
    }

    private void playerHitMonster(ArrayList<Monster> monsters, Player player) {//碰到怪
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            if (player.isCollision(monster)) {
                if (monster.getMonsterType() == MonsterAnimator.MonsterType.PIRANHA || monster.getMonsterType() == MonsterAnimator.MonsterType.THORN_UP
                        || monster.getMonsterType() == MonsterAnimator.MonsterType.THORN_DOWN || monster.getMonsterType() == MonsterAnimator.MonsterType.THORN_RIGHT
                        || monster.getMonsterType() == MonsterAnimator.MonsterType.THORN_LEFT) {//碰到花直接死
                    if (!player.isInvincible()) {
                        player.setState(PlayerAnimator.State.DEAD);
                    }
                }
                if (player.collider().top() <= monster.collider().top()
                        && player.collider().bottom() >= monster.collider().bottom()
                        && !player.isInvincible()) {//撞擊到角色身體
                    player.setState(PlayerAnimator.State.DEAD);
                }
                if (player.isInvincible()
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.THORN_DOWN //刺也不能踩死
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.THORN_LEFT
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.THORN_RIGHT
                        && monster.getMonsterType() != MonsterAnimator.MonsterType.THORN_UP) {
                    monsters.remove(i--);
                }
            }
        }
    }

    private void playerTouchCoin(Player player, ArrayList<Coin> coins) {//碰到錢
        for (int i = 0; i < coins.size(); i++) {
            if (player.isCollision(coins.get(i))) {
                coins.remove(i--);
                player.setCoinCount(player.getCoinCount() + 1);
            }
        }
    }

    private void playerSetRebornPoint(ArrayList<RebornPoint> rebornPoints, Player player) {//設定重生點
        for (int i = 0; i < rebornPoints.size(); i++) {
            if (player.collider().left() > rebornPoints.get(i).collider().left()) {//超過重生點就設定重生點
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

    private void fireBallTouchAll(ArrayList<Monster> monsters, ArrayList<Player> players, Skill skill) { // 火球攻擊對象
        for (int i = 0; i < this.monsters.size(); i++) {
            if (monsters.get(i).isCollision(skill)
                    && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.PIRANHA
                    && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.THORN_DOWN //刺也不能踩死
                    && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.THORN_LEFT
                    && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.THORN_RIGHT
                    && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.THORN_UP) {
                monsters.remove(i--);
            }
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isCollision(skill)) {
                if (!players.get(i).isInvincible()) {
                    players.get(i).setState(PlayerAnimator.State.DEAD);
                }
            }
        }
    }

    private void playerShotFire(Player player, SkillAnimator.SkillType skillType) {
        if (player.getDirection() == Global.Direction.RIGHT) {
            skills.add(new Skill(player.collider().centerX() + 80,
                    player.collider().centerY() - 30,
                    120, 120, skillType, player.getDirection(), new Delay(120)));
        } else {
            skills.add(new Skill(player.collider().centerX() - 80,
                    player.collider().centerY() - 30,
                    120, 120, skillType, player.getDirection(), new Delay(120)));
        }
        player.setCanShot(false);
        player.getFireBallDelay().play();
    }

    private void fireBallCollision(ArrayList<Skill> fireballs) {
        Skill fireball = fireballs.get(0);
        for (int i = 1; i < fireballs.size(); i++) {
            if (fireball.isCollision(fireballs.get(i))) {
                fireballs.clear();
            }
        }
    }

    private void setBackgroundPrint(Background background, Camera camera) {
        if (background.collider().left() < camera.collider().left()) {
            background.setPaintLeft(camera.collider().left());
        } else {
            background.setPaintLeft(background.painter().left());
        }
        if (background.collider().right() > camera.collider().right()) {
            background.setPaintRight(camera.collider().right());
        } else {
            background.setPaintRight(background.painter().right());
        }
        if (background.collider().top() < camera.collider().top()) {
            background.setPaintTop(camera.collider().top());
        } else {
            background.setPaintTop(background.painter().top());
        }
    }

    private void backgroundMove(ArrayList<Background> backgrounds, Player player) {
        for (int i = 0; i < backgrounds.size(); i++) {
            if (i / 2 < 1) {
                backgrounds.get(i).translateX((int) -player.getSpeed().vx() / 6);
                backgrounds.get(i).translateX((int) -player.getSpeed().vx() / 6);
                backgrounds.get(i).translateX((int) -player.getSpeed().vx() / 4);
                backgrounds.get(i).translateX((int) -player.getSpeed().vx() / 4);
            }
        }

    }

    private void backgroundOutOfScreen(ArrayList<Background> backgrounds, Camera camera) {
        for (int i = 0; i < backgrounds.size(); i++) {
            if (i % 2 != 0) {
                //往右跑
                if (backgrounds.get(i).painter().right() <= camera.collider().left()) {
                    backgrounds.get(i).setRect(new Rect(backgrounds.get(i + 1).collider().right(),
                            backgrounds.get(i).collider().top(),
                            backgrounds.get(i + 1).collider().right() + backgrounds.get(i).collider().width(),
                            backgrounds.get(i).collider().bottom()));
                }
                //往左跑
                if (backgrounds.get(i).painter().left() >= camera.collider().right()) {
                    backgrounds.get(i).setRect(new Rect(backgrounds.get(i + 1).collider().left() - backgrounds.get(i).collider().width(),
                            backgrounds.get(i).collider().top(),
                            backgrounds.get(i + 1).collider().left(),
                            backgrounds.get(i).collider().bottom()));
                }
            } else {
                //往右跑
                if (backgrounds.get(i).painter().right() <= camera.collider().left()) {
                    backgrounds.get(i).setRect(new Rect(backgrounds.get(i - 1).collider().right(),
                            backgrounds.get(i).collider().top(),
                            backgrounds.get(i - 1).collider().right() + backgrounds.get(i).collider().width(),
                            backgrounds.get(i).collider().bottom()));
                }
                //往左跑
                if (backgrounds.get(i).painter().left() >= camera.collider().right()) {
                    backgrounds.get(i).setRect(new Rect(backgrounds.get(i - 1).collider().left() - backgrounds.get(i).collider().width(),
                            backgrounds.get(i).collider().top(),
                            backgrounds.get(i - 1).collider().left(),
                            backgrounds.get(i).collider().bottom()));
                }
            }
        }
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
                                BrickAnimator.BrickType.CRASH_MOGU_L1));
                        break;

                    case "moguM1":  // 會破的蘑菇中間
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.CRASH_MOGU_M1));
                        break;

                    case "moguR1": // 會破的蘑菇右邊
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.CRASH_MOGU_R1));
                        break;

                    case "brick1":  // 會破 brick1
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.CRASHBRICK_1));
                        break;

                    case "coin1":       // 錢
                        coins.add(new Coin(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y, CoinAnimator.CoinType.COIN));
                        break;

                    case "block1":   // 草地
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BLOCK_GRASS));
                        break;

                    case "block2":  // 土地
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BLOCK_EARTH));
                        break;

                    case "pipeUp":   // 水管朝上
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() + 5,
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickAnimator.BrickType.PIPE_UP));
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() - 1, // 食人花
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() + 20,
                                UNIT_X + 5, UNIT_Y * 2,
                                MonsterAnimator.MonsterType.PIRANHA, new Vector(0, 2), new Delay(32)));
                        break;

                    case "pipeBody":  // 水管中段
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY(),
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickAnimator.BrickType.PIPE_BODY));
                        break;

                    case "pipeDown":  // 水管朝下
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY(),
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickAnimator.BrickType.PIPE_DOWN));
                        break;

                    case "moguL2":    // 永恆蘑菇左端
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.MOGU_L2));
                        break;

                    case "moguM2":    // 永恆蘑菇中段
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.MOGU_M2));
                        break;

                    case "moguR2":     // 永恆蘑菇右端
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.MOGU_R2));
                        break;

                    case "brick0":  // 不破磚塊
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BRICK_0));
                        break;

                    case "fort1":     // 砲管向左
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.FORT_L, new Delay(120)));
                        break;

                    case "fort2":     // 砲管向右
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.FORT_R, new Delay(120)));
                        break;

                    case "spring": // 彈簧
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.SPRING));
                        break;

                    case "brickAsk":    // 問號
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BRICK_ASK, new Delay(800)));
                        break;

                    case "ending":     // 終點線
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.ENDING));
                        break;

                    case "blockBoss":        // 刺向上
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.NORMALBRICK));
                        break;

                    case "brick":     // 會動 brick(左右)
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X,
                                BrickAnimator.BrickType.MOVEBRICK, new Vector(3, 0), new Delay(100)));
                        break;

                    case "brick2":      // 上下動
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X * 3,
                                BrickAnimator.BrickType.MOVE_BRICK_2, new Vector(0, 2), new Delay(100)));
                        break;

                    case "stair":       // 斜動/
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X,
                                BrickAnimator.BrickType.STAIR, new Vector(2, 2), new Delay(200)));
                        break;

                    case "moguL":// 會消失
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                BrickAnimator.BrickType.VANISH_MOGU_L, new Delay(30)));
                        break;

                    case "moguM": // 會消失
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                BrickAnimator.BrickType.VANISH_MOGU_M, new Delay(30)));
                        break;

                    case "moguR": // 會消失
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                BrickAnimator.BrickType.VANISH_MOGU_R, new Delay(30)));
                        break;

                    case "thorn1": // 刺向上
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                MonsterAnimator.MonsterType.THORN_UP, new Vector(0, 0)));
                        break;
                    case "thorn2": // 刺向下
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                MonsterAnimator.MonsterType.THORN_DOWN, new Vector(0, 0)));
                        break;
                    case "thron3": // 刺向右
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                MonsterAnimator.MonsterType.THORN_RIGHT, new Vector(0, 0)));
                        break;

                    case "thron4": // 刺向左
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                MonsterAnimator.MonsterType.THORN_LEFT, new Vector(0, 0)));
                        break;
                    case "monster1": // 左右(豬)
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 3 / 2, UNIT_Y * 3 / 2,
                                MonsterAnimator.MonsterType.PIG, new Vector(-2, 0), new Delay(100)));
                        break;
                    case "monster2": // 左右(河童)
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 3 / 2, UNIT_Y * 3 / 2,
                                MonsterAnimator.MonsterType.KAPA, new Vector(-2, 0), new Delay(300)));
                        break;

                    case "monster3": // 飛左右(章魚)
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 3 / 2, UNIT_Y * 3 / 2,
                                MonsterAnimator.MonsterType.TACO, new Vector(-3, 0), new Delay(300)));
                        break;

                    case "monster4": // 飛上下
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 3 / 2, UNIT_Y * 3 / 2,
                                MonsterAnimator.MonsterType.GHOST, new Vector(0, 4), new Delay(100)));
                        break;

                    case "monster5": // 大魔王
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() * 3 / 2,
                                UNIT_X * tmp.getSizeX() * 3, UNIT_Y * tmp.getSizeY() * 3,
                                MonsterAnimator.MonsterType.GREENBOSS, new Vector(2, 0), new Delay(60)));
                        break;

                    case "fireDown": // 火向下
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X * tmp.getSizeX(), UNIT_Y * tmp.getSizeY(),
                                MonsterAnimator.MonsterType.FIRE, new Vector(0, 2), new Delay(150)));
                        break;

                    case "pipeUp2": // 水管朝上(無花版本)
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX(),
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() + 5,
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickAnimator.BrickType.PIPE_UP));
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
                                BrickAnimator.BrickType.ARROW_LEFT));
                        break;
                    case "arrow_RIGHT":  // 加速箭頭
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.ARROW_RIGHT));
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
                            && players.get(0).playerAnimator().State() != PlayerAnimator.State.DEAD) {
                        playerShotFire(players.get(0), SkillAnimator.SkillType.RASENGAN);
                        AudioResourceController.getInstance().play("resources\\sounds\\rasengan.wav");
                    } else {
                        players.get(0).keyPressed(commandCode, trigTime);
                    }
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if (commandCode / 5 == 0) {
                    players.get(0).keyReleased(commandCode, trigTime);
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {
//                
            }
        };
    }
}
