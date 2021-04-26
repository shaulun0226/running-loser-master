package game.utils;

import game.controllers.ImgController;
import game.controllers.SceneController;
import java.awt.Graphics;
import java.awt.Image;

public class BrickAnimator {

    public enum BrickType {
        // 會破
        COIN, // 錢
        CRASHBRICK_1, // 會破 brick1
        CRASH_MOGU_L1, // 會破的蘑菇左端
        CRASH_MOGU_M1, // 會破的蘑菇中間
        CRASH_MOGU_R1, // 會破的蘑菇右邊

        // 會動
        MOVEBRICK, // 會動 brick(左右)
        MOVE_BRICK_2, // 上下動
        STAIR, // 斜動/

        // 會自己消失
        VANISH_MOGU_M, // 會消失 moguM 蘑菇中段
        VANISH_MOGU_L, // 會消失的蘑菇左端
        VANISH_MOGU_R, // 會消失的蘑菇右端

        // 不會破
        NORMALBRICK, // 普通(不破) (blockBoss)
        BLOCK_GRASS, // 不破磚塊(草)
        BLOCK_EARTH, // 不破磚塊(土)
        PIPE_UP, // 水管朝上
        PIPE_BODY, // 水管中段
        PIPE_DOWN, // 水管朝下
        MOGU_L2, // 永恆蘑菇左端
        MOGU_M2, // 永恆蘑菇中段
        MOGU_R2, // 永恆蘑菇右端
        BRICK_0, // 不破磚塊
        FORT_L, // 砲管向左
        FORT_R, // 砲管向右
        SPRING(new int[]{0, 1, 2, 1}, 10), // 彈簧
        BRICK_ASK(new int[]{0}, 20), // 問號
        ENDING, // 終點線
        
        // 功能
        ARROW_LEFT,  // 向左加速
        ARROW_RIGHT; // 向右加速

        private int[] state;
        private int speed;

        BrickType() {

        }

        BrickType(int[] state, int speed) {
            this.state = state;
            this.speed = speed;
        }
    }
    //用enum決定印出哪種brick
    private Image img;
    private BrickType brickType;
    private final Delay delay;
    private int count;

    public BrickAnimator(BrickType brickType) {
        this.brickType = brickType;
        switch (brickType) {
            case COIN:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().coin());
                break;
            case CRASHBRICK_1:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().brick1());
                break;
            case CRASH_MOGU_L1:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguL1());
                break;
            case CRASH_MOGU_M1:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguM1());
                break;
            case CRASH_MOGU_R1:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguR1());
                break;
            case MOVEBRICK:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().brick());
                break;
            case MOVE_BRICK_2:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().brick2());
                break;
            case STAIR:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().stair());
                break;
            case VANISH_MOGU_M:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguM());
                break;
            case VANISH_MOGU_L:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguL());
                break;
            case VANISH_MOGU_R:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguR());
                break;
            case NORMALBRICK:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().blockBoss());
                break;
            case BLOCK_GRASS:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().block1());
                break;
            case BLOCK_EARTH:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().block2());
                break;
            case PIPE_UP:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().pipeUp());
                break;
            case PIPE_BODY:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().pipeBody());
                break;
            case PIPE_DOWN:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().pipeDown());
                break;
            case MOGU_L2:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguL2());
                break;
            case MOGU_M2:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguM2());
                break;
            case MOGU_R2:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().moguR2());
                break;
            case BRICK_0:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().brick0());
                break;
            case FORT_L:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().fort1());
                break;
            case FORT_R:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().fort2());
                break;
            case SPRING:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().spring());
                break;
            case BRICK_ASK:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().brickAsk());
                break;
            case ENDING:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().ending());
                break;
            case ARROW_LEFT:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().arrow_LEFT());
                break;
            case ARROW_RIGHT:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().arrow_RIGHT());
                break;
        }
        this.delay = new Delay(brickType.speed);
        this.delay.loop();
        this.count = 0;
    }

    public BrickType getBrickType() {
        return this.brickType;
    }
//用多載畫?

    public void paint(int left, int top, int right, int bottom, Graphics g) {//加入enum從外面帶入狀態來決定印哪個磚塊
        if (brickType == brickType.SPRING /*|| brickType == brickType.BRICK_ASK*/) {  // 彈簧或問號磚塊
            g.drawImage(img,
                    left, top, right, bottom,
                    0,
                    0 + 32 * brickType.state[count],
                    32,
                    32 + 32 * brickType.state[count], null);
        } else if(brickType ==brickType.NORMALBRICK){
            g.drawImage(img, left, top, right, bottom, 0, 0, 64, 64, null);
        }else{
            g.drawImage(img, left, top, right, bottom, 0, 0, 32, 32, null);
        }
    }

    public void paint(boolean reload, int left, int top, int right, int bottom, Graphics g) {//畫?
        if (reload) {  //問號磚塊
            g.drawImage(img,
                    left, top, right, bottom,
                    0, 0 + 32,
                    32, 32 + 32, null);

        } else {
            g.drawImage(img, left, top, right, bottom, 0, 0, 32, 32, null);
        }
    }

    public void update() {
        if (delay.count()) {   // 用delay來跑count
            count = ++count % brickType.state.length;
        }
    }
}
