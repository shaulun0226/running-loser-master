package game.gameobj;

import game.utils.BrickAnimator;
import game.utils.BrickAnimator.BrickType;
import game.utils.Delay;
import java.awt.Color;
import java.awt.Graphics;
import static game.utils.Global.*;
import game.utils.Vector;
import java.awt.Rectangle;
import javafx.scene.transform.Rotate;

public class Brick extends GameObject {

    BrickAnimator brickAnimator;
    private int x;
    private int y;
    private BrickType brickType;
    //可以撞的要用到的
    private boolean crash;
    //會不見的
    private Delay delay;
    private boolean vanish;
    //移動的磚塊要用到的
    private Vector vector;
    private boolean moveReverse;
    private int moveDisLimit;
    private int moveDis;
    //砲管
    private boolean bornBullet;
    //?用
    private boolean reload;
    //加速方塊用
    private int brickSpeed;

    public Brick(int x, int y, int width, int height, BrickType brickType) {       // 不會動
        super(x, y, width, height);
        this.brickAnimator = new BrickAnimator(brickType);
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.brickType = brickType;
        this.delay = new Delay(1);
        this.delay.stop();
        this.moveReverse = false;
        if (brickType == BrickType.CRASHBRICK_1 || brickType == BrickType.CRASH_MOGU_L1 || brickType == BrickType.CRASH_MOGU_M1
                || brickType == BrickType.CRASH_MOGU_R1 || brickType == BrickType.COIN) {  // 會破           
            this.crash = true;
        } else {                                // 不會破
            this.crash = false;
        }
        if(brickType==BrickType.ARROW_LEFT){
            this.brickSpeed=BRICKSPEED_LEFT;
        }else if(brickType==BrickType.ARROW_RIGHT){
            this.brickSpeed=BRICKSPEED_RIGHT;
        }else{
            this.brickSpeed=0;
        }
    }

    public Brick(int x, int y, int width, int height, BrickType brickType, Delay delay) {       // 不會動
        super(x, y, width, height);
        this.brickAnimator = new BrickAnimator(brickType);
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.brickType = brickType;
        this.delay = delay;
        this.delay.loop();
        this.moveReverse = false;
        if (brickType == BrickType.CRASHBRICK_1 || brickType == BrickType.CRASH_MOGU_L1 || brickType == BrickType.CRASH_MOGU_M1
                || brickType == BrickType.CRASH_MOGU_R1 || brickType == BrickType.COIN) {  // 會破           
            this.crash = true;
        } else {                                // 不會破
            this.crash = false;
        }
        this.reload = true;
        if(brickType==BrickType.ARROW_LEFT){
            this.brickSpeed=BRICKSPEED_LEFT;
        }else if(brickType==BrickType.ARROW_RIGHT){
            this.brickSpeed=BRICKSPEED_RIGHT;
        }else{
            this.brickSpeed=0;
        }
    }

    public Brick(int x, int y, int width, BrickType brickType, Vector vector, Delay delay) {  // 會動
        super(x, y, width, UNIT_Y);
        brickAnimator = new BrickAnimator(brickType);
        this.x = x - width / 2;
        this.y = y - UNIT_Y / 2;
        this.brickType = brickType;
        this.vector = vector;
        this.moveReverse = false;
        this.delay = delay;
        this.delay.loop();
        if (brickType == BrickType.CRASHBRICK_1 || brickType == BrickType.CRASH_MOGU_L1 || brickType == BrickType.CRASH_MOGU_M1
                || brickType == BrickType.CRASH_MOGU_R1 || brickType == BrickType.COIN) {
            this.crash = true;
        } else {
            this.crash = false;
        }
        this.reload = true;
        if(brickType==BrickType.ARROW_LEFT){
            this.brickSpeed=BRICKSPEED_LEFT;
        }else if(brickType==BrickType.ARROW_RIGHT){
            this.brickSpeed=BRICKSPEED_RIGHT;
        }else{
            this.brickSpeed=0;
        }
    }

    public Brick(int x, int y, BrickType brickType, Delay delay) { // 會消失
        super(x, y, UNIT_X, UNIT_Y);
        brickAnimator = new BrickAnimator(brickType);
        this.x = x - UNIT_X / 2;
        this.y = y - UNIT_Y / 2;
        this.brickType = brickType;
        this.vector = new Vector(0, 0);
        this.moveReverse = false;
        this.vanish = false;
        this.delay = delay;
        this.delay.loop();
        if (brickType == BrickType.CRASHBRICK_1 || brickType == BrickType.CRASH_MOGU_L1 || brickType == BrickType.CRASH_MOGU_M1
                || brickType == BrickType.CRASH_MOGU_R1 || brickType == BrickType.COIN) {
            this.crash = true;
        } else {
            this.crash = false;
        }
        if (brickType == BrickType.VANISH_MOGU_M
                || brickType == BrickType.VANISH_MOGU_L
                || brickType == BrickType.VANISH_MOGU_R) {  // 會消失
            this.delay.stop();
        }
        this.reload = true;
        if(brickType==BrickType.ARROW_LEFT){
            this.brickSpeed=BRICKSPEED_LEFT;
        }else if(brickType==BrickType.ARROW_RIGHT){
            this.brickSpeed=BRICKSPEED_RIGHT;
        }else{
            this.brickSpeed=0;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    public int getBrickSpeed(){
        return this.brickSpeed;
    }

    public boolean getCrash() {
        return this.crash;
    }

    public Vector getVector() {
        return this.vector;
    }

    public boolean getBornBullet() {
        return this.bornBullet;
    }

    public BrickType getBrickType() {
        return this.brickType;
    }

    public Delay getDelay() {
        return this.delay;
    }

    public boolean getReload() {
        return this.reload;
    }

    public boolean getVanish() {
        return this.vanish;
    }

    public void setBornBullet(boolean bornBullet) {
        this.bornBullet = bornBullet;
    }

    public void setMoveDisLimit(int moveDisLimit) {
        this.moveDisLimit = moveDisLimit;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (brickAnimator.getBrickType() == BrickType.BRICK_ASK) {
            brickAnimator.paint(reload, painter().left(), painter().top(), painter().right(), painter().bottom(), g);
        } else {
            brickAnimator.paint(painter().left(), painter().top(), painter().right(), painter().bottom(), g);
        }
    }

    @Override
    public void update() {
        if (brickType == BrickType.MOVEBRICK
                || brickType == BrickType.MOVE_BRICK_2
                || brickType == BrickType.STAIR) {
            if (moveReverse) {
                this.vector = new Vector(-Math.abs(this.vector.vx()), Math.abs(this.vector.vy()));
            } else {
                this.vector = new Vector(Math.abs(this.vector.vx()), -Math.abs(this.vector.vy()));
            }
            if (delay.count()) {
                if (moveReverse) {
                    moveReverse = false;
                } else {
                    moveReverse = true;
                }
            }
            translateX((int) this.vector.vx());
            translateY((int) this.vector.vy());
        }
        if (brickType == BrickType.VANISH_MOGU_M
                || brickType == BrickType.VANISH_MOGU_L
                || brickType == BrickType.VANISH_MOGU_R) {
            if (delay.count()) {
                vanish = true;
            }
        }
        if (brickType == BrickType.BRICK_ASK || brickType == BrickType.SPRING) {
            brickAnimator.update();
        }
        if (brickType == BrickType.FORT_L) {
            if (delay.count()) {
                bornBullet = true;
            } else {
                bornBullet = false;
            }
        }
        if (brickType == BrickType.BRICK_ASK) {
            if (delay.count()) {
                reload = true;
                delay.stop();
            }
        }
    }
}
