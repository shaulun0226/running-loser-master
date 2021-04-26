package game.gameobj;

import game.camera.MapInformation;
import game.controllers.AudioResourceController;
import game.controllers.SceneController;
import game.utils.CommandSolver;
import game.utils.Delay;
import static game.utils.Global.*;
import game.utils.Global.Direction;
import game.utils.PlayerAnimator;
import game.utils.PlayerAnimator.State;
import game.utils.PlayerAnimator.ActorType;
import game.utils.Path;
import game.utils.Vector;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;

public class Player extends GameObject implements CommandSolver.KeyListener {

    private PlayerAnimator animator;//動畫
    private final ActorType actorType;//角色類型
    private Direction dir;//方向
    private boolean canMove;//
    private boolean isJumping;//跳躍中
    private boolean isSquating;//蹲下中

    private int[] rebornPoint;//重生點{x,y}
    private Delay deathDelay;//死亡的delay
    //記分區
    private int coinCount;//獲得金幣數
    private int monsterCount;//擊殺怪物數
    //速度區
    private Vector Speed;//移動速度
    private int ySpeed;//重力
    private boolean[] pressed = {false, false};//用按鍵判斷給予的向量
    private int brickSpeed;//有速度的方塊給予的速度
    //技能區
    private boolean canGetEffect;//可以撞?方塊
    private Delay skillDelay;//可以撞?方塊的delay
    private boolean isInvincible;//無敵狀態
    private int extraSpeed;//技能給予額外速度
    private Delay effectDelay;
    private int downSpeed;
    private boolean isChange;//交換位置
    private Delay paintDelay;//無敵閃爍
    private boolean paint;//是否閃爍
    private boolean isChaos;
    //火球術
    private boolean canShot;
    private Delay fireBallDelay;
    //ID
    private int Id;
    //effect
    private Image imgInvincible;
    private Image imgSpeedUp;
    private Image imgSpeedDown;
    private Image imgChaos;

    public Player(int x, int y) {
        this(ActorType.SASUKEHD, x, y);
    }

    public Player(ActorType actorType, int x, int y) {
        super(x, y, ACTOR_WIDTH, ACTOR_HEIGHT);
        dir = Direction.RIGHT;
        animator = new PlayerAnimator(State.JUMP, actorType);
        this.Speed = new Vector(0, 0);
        this.isJumping = true;
        isSquating = false;
        this.ySpeed = 0;
        this.actorType = actorType;
        this.rebornPoint = new int[2];
        this.rebornPoint[0] = x;
        this.rebornPoint[1] = y;
        this.deathDelay = new Delay(45);
        this.deathDelay.stop();
        this.coinCount = 0;
        this.canGetEffect = true;
        this.skillDelay = new Delay(PLAYER_EFFECT_CD);
        this.skillDelay.stop();
        this.extraSpeed = 0;
        this.isInvincible = false;
        this.paintDelay = new Delay(3);//無敵狀態閃爍
        this.paint = true;
        this.canShot = true;
        this.fireBallDelay = new Delay(PLAYER_SKILL_CD);
        this.effectDelay = new Delay(EFFECT_DELAY);
        this.imgInvincible = SceneController.instance().irc().tryGetImage(new Path().img().effects().invincible());
        this.imgSpeedUp = SceneController.instance().irc().tryGetImage(new Path().img().effects().speedUp());
        this.imgSpeedDown = SceneController.instance().irc().tryGetImage(new Path().img().effects().speedDown());
        this.imgChaos = SceneController.instance().irc().tryGetImage(new Path().img().effects().chaos());
    }

    public int getYSpeed() {
        return this.ySpeed;
    }

    public void setYSpeed(int y) {
        this.ySpeed = y;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getId() {
        return this.Id;
    }

    public void setJumping(boolean jumping) {
        this.isJumping = jumping;
    }

    public void setCanGetEffect(boolean canGetSkill) {
        this.canGetEffect = canGetSkill;
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public boolean isCanGetEffect() {
        return this.canGetEffect;
    }

    public boolean isInvincible() {
        return this.isInvincible;
    }

    public boolean isSquating() {
        return this.isSquating;
    }

    public Delay getSkillDelay() {
        return this.skillDelay;
    }

    public Delay getFireBallDelay() {
        return this.fireBallDelay;
    }

    public int getCoinCount() {
        return this.coinCount;
    }

    public PlayerAnimator playerAnimator() {
        return this.animator;
    }

    public Vector getSpeed() {
        return this.Speed;
    }

    public Direction getDir() {
        return this.dir;
    }

    public int getMonsterCount() {
        return this.monsterCount;
    }

    public void setMonsterCount(int monsterCount) {
        this.monsterCount = monsterCount;
    }

    public final void setSpeed(Vector vector) {
        this.Speed = vector;
    }

    public void setState(State state) {
        this.animator.setState(state);
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public void setBrickSpeed(int brickSpeed) {
        this.brickSpeed = brickSpeed;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public void setCanShot(boolean canShot) {
        this.canShot = canShot;
    }

    public void setRebornPoint(int x, int y) {//設定重生點
        this.rebornPoint[0] = x;
        this.rebornPoint[1] = y;
    }

    public void jump(int jump) {//跳躍
        ySpeed = -jump;
        isJumping = true;
        animator.setState(State.JUMP);
    }

    public void move() {//移動
        if (!outOfScreen() && animator.State() != State.DEAD) {
            translateX((int) Speed.vx());
            translateY((int) Speed.vy());
        }
    }

    public void fall() {//重力往下掉
        if (animator.State() != State.DEAD) {
            translateY(this.ySpeed);
        }
    }

    public void chaos() {
        this.isChaos = true;
        this.effectDelay.play();
    }

    public void effect(int effect) {
        switch (effect) {
            case 1://加速
                this.extraSpeed = PLAYER_SPEED / 2;
                break;
            case 2://無敵
                this.isInvincible = true;
                this.paintDelay.loop();
                break;
            case 3://交換位置
                this.isChange = true;
                break;
        }
    }

    public void playerChangePosition(Player player) {
        Rect tmp = Rect.genWithCenter(this.collider().centerX(), this.collider().centerY(), ACTOR_WIDTH, ACTOR_HEIGHT);
        setRect(Rect.genWithCenter(player.collider().centerX(), player.collider().centerY(), ACTOR_WIDTH, ACTOR_HEIGHT));
        player.setRect(tmp);
        isChange = false;
    }

    private void squat() {
        Rect r = Rect.genWithCenter(collider().centerX(), collider().centerY() - 2, ACTOR_WIDTH, ACTOR_HEIGHT / 2);
        resetRect(r);
        isSquating = true;
    }

    public void setDownSpeed() {
        this.downSpeed = 3;
        this.effectDelay.play();
    }

    private void setVector() {
        this.Speed = this.Speed.zero();
        if (pressed[0]) {
            this.Speed = this.Speed.add(new Vector(-PLAYER_SPEED - this.extraSpeed + this.brickSpeed + this.downSpeed, 0));
        }
        if (pressed[1]) {
            this.Speed = this.Speed.add(new Vector(PLAYER_SPEED + this.extraSpeed + this.brickSpeed - this.downSpeed, 0));
        }
    }

    public boolean getCanMove() {
        return this.canMove;
    }

    public boolean isChange() {
        return this.isChange;
    }

    public boolean canShot() {
        return this.canShot;
    }

    public Direction getDirection() {
        return this.dir;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    private void resetEffect() {
        canGetEffect = true;
        extraSpeed = 0;
        isInvincible = false;
        isChange = false;
        skillDelay.stop();
        paintDelay.stop();
        paint = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (isInvincible) {
            g.drawImage(imgInvincible, painter().centerX() - 16, painter().top() - 32, 32, 32, null);
        }
        if (extraSpeed != 0) {
            g.drawImage(imgSpeedUp, painter().centerX() - 16, painter().top() - 32, 32, 32, null);
        }
        if (downSpeed != 0) {
            g.drawImage(imgSpeedDown, painter().centerX() - 16, painter().top() - 64, 32, 32, null);
        }
        if (isChaos) {
            g.drawImage(imgChaos, painter().centerX() - 16, painter().top() - 64, 32, 32, null);
        }
        if (paint) {
            animator.paint(dir, painter().left(), painter().top(),
                    painter().right(), painter().bottom(), g);
        }
    }

    @Override
    public void update() {
        animator.update();
        setVector();
        if (touchLeft()) {
            changeLeft(MapInformation.mapInfo().left());
        }
        if (touchRight()) {
            changeRight(MapInformation.mapInfo().right());
        }
        if (touchTop()) {
            changeTop(MapInformation.mapInfo().top());
        }
        if (touchBottom()) {
            if (!IS_DEBUG) {
                if (animator.State() != State.DEAD) {
                    
                        AudioResourceController.getInstance().play(new Path().sound().dead());
                    animator.setState(State.DEAD);
                    deathDelay.play();
                }
            }
            changeBottom(MAP_HEIGHT);
        }
        if (animator.State() == State.DEAD) {//若狀態為DEAD
            deathDelay.play();//啟動死亡DELAY
            Rect r = Rect.genWithCenter(collider().centerX(), collider().centerY(), ACTOR_WIDTH, ACTOR_HEIGHT);
            resetRect(r);
        }
        if (deathDelay.count()) {//若是死亡DELAY計時到了，設定角色狀態回到重生點
            animator.setState(State.JUMP);
            dir = Direction.RIGHT;
            isJumping = true;
            ySpeed = 0;
            Speed = new Vector(0, 0);
            Rect r = Rect.genWithCenter(collider().centerX(), collider().centerY(), ACTOR_WIDTH, ACTOR_HEIGHT);
            resetRect(r);
            this.downSpeed = 0;
            this.isChaos = false;
            resetEffect();
            if (IS_DEBUG) {

            } else {
                setRect(new Rect(rebornPoint[0], rebornPoint[1] - 100,
                        rebornPoint[0] + ACTOR_WIDTH,
                        rebornPoint[1] + ACTOR_HEIGHT - 100));
            }
        }
        if (skillDelay.count()) {//若是技能delay計時到了，重置為可以獲得技能
            resetEffect();
        }
        if (paintDelay.count()) {
            if (paint) {
                paint = false;
            } else {
                paint = true;
            }
        }
        if (fireBallDelay.count()) {
            canShot = true;
            fireBallDelay.stop();
        }
        if (effectDelay.count()) {
            downSpeed = 0;
            isChaos = false;
            effectDelay.stop();
        }
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        Direction dir = Direction.getDirection(commandCode);
        if (animator.State() != State.DEAD) {
            if (!isChaos) {
                switch (dir) {
                    case LEFT:
                        if (!isSquating) {
                            pressed[0] = true;
                            setDir(Direction.LEFT);
                            if (animator.State() != State.WALK
                                    && !isJumping) {
                                setState(State.WALK);
                            }
                        }
                        break;
                    case RIGHT:
                        if (!isSquating) {
                            pressed[1] = true;
                            setDir(Direction.RIGHT);
                            if (animator.State() != State.WALK
                                    && !isJumping) {
                                setState(State.WALK);
                            }
                        }
                        break;
                    case DOWN:
                        if (!isJumping) {
                            if (!isSquating && animator.State() != State.WALK) {
                                squat();
                            }
                            if (animator.State() != State.SQUAT) {
                                setState(State.SQUAT);
                            }
                        }
                        break;
                    case JUMP:
                        if (IS_DEBUG) {
                            jump(PLAYER_JUMP);
                            setState(State.JUMP);
                            break;
                        }
                        if (!isJumping && !isSquating) {
                            jump(PLAYER_JUMP);
                            if (playerAnimator().State() != State.JUMP) {
                                setState(State.JUMP);
                            }
                        }
                        break;
                }
            } else {
                switch (dir) {
                    case LEFT:
                        if (!isSquating) {
                            pressed[1] = true;
                            setDir(Direction.RIGHT);
                            if (animator.State() != State.WALK
                                    && !isJumping) {
                                setState(State.WALK);
                            }
                        }
                        break;
                    case RIGHT:
                        if (!isSquating) {
                            pressed[0] = true;
                            setDir(Direction.LEFT);
                            if (animator.State() != State.WALK
                                    && !isJumping) {
                                setState(State.WALK);
                            }
                        }
                        break;

                    case DOWN:
                        if (IS_DEBUG) {
                            jump(PLAYER_JUMP);
                            setState(State.JUMP);
                            break;
                        }
                        if (!isJumping && !isSquating) {
                            jump(PLAYER_JUMP);
                            if (playerAnimator().State() != State.JUMP) {
                                setState(State.JUMP);
                            }
                        }
                        break;
                    case JUMP:
                        if (!isJumping) {
                            if (!isSquating && animator.State() != State.WALK) {
                                squat();
                            }
                            if (animator.State() != State.SQUAT) {
                                setState(State.SQUAT);
                            }
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        Direction dir = Direction.getDirection(commandCode);
        if (!isChaos) {
            switch (dir) {
                case LEFT:
                    pressed[0] = false;
                    if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                        setState(State.STOP);
                    }
                    break;
                case RIGHT:
                    pressed[1] = false;
                    if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                        setState(State.STOP);
                    }

                    break;
                case JUMP:
                    if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                        setState(State.STOP);
                    }
                    break;
                case DOWN:
                    resetRect(Rect.genWithCenter(collider().centerX(), collider().bottom() - 18, ACTOR_WIDTH, ACTOR_HEIGHT));
                    setState(State.STOP);
                    isSquating = false;
                    break;
            }
        } else {
            switch (dir) {
                case LEFT:
                    pressed[1] = false;
                    if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                        setState(State.STOP);
                    }
                    break;
                case RIGHT:
                    pressed[0] = false;
                    if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                        setState(State.STOP);
                    }
                    break;
                case JUMP:
                    resetRect(Rect.genWithCenter(collider().centerX(), collider().bottom() - 18, ACTOR_WIDTH, ACTOR_HEIGHT));
                    setState(State.STOP);
                    isSquating = false;
                    break;
                case DOWN:
                    if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                        setState(State.STOP);
                    }
                    break;
            }

        }
        switch (dir) {
            case LEFT:
                pressed[0] = false;
                if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                    setState(State.STOP);
                }
                break;
            case RIGHT:
                pressed[1] = false;
                if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                    setState(State.STOP);
                }

                break;
            case JUMP:
                if (animator.State() != State.JUMP && animator.State() != State.DEAD && !isSquating) {
                    setState(State.STOP);
                }
                break;
            case DOWN:
                resetRect(Rect.genWithCenter(collider().centerX(), collider().bottom() - ACTOR_HEIGHT/2, ACTOR_WIDTH, ACTOR_HEIGHT));
                setState(State.STOP);
                isSquating = false;
                break;
        }
    }

    @Override
    public void keyTyped(char c, long trigTime) {
//        
    }

}
