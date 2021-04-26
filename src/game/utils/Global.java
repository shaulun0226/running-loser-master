/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.utils;

/**
 *
 * @author user1
 */
public class Global {
    public enum Direction {
        JUMP(3),
        DOWN(0),
        LEFT(1),
        RIGHT(2),
        SHOT(4);
        private int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Direction getDirection(int value) {
            switch (value) {
                case 0:
                    return DOWN;
                case 1:
                    return LEFT;
                case 2:
                    return RIGHT;
                case 3:
                    return JUMP;
                case 4:
                    return SHOT;
            }
            return LEFT;
        }
    }

    public static final boolean IS_DEBUG = false;

    public static void log(String str) {
        System.out.println(str);
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }
    // 視窗大小
    public static final int WINDOW_WIDTH =  1080;
    public static final int WINDOW_HEIGHT = 600;
    public static final int SCREEN_X = WINDOW_WIDTH - 8 - 8;
    public static final int SCREEN_Y = WINDOW_HEIGHT - 31 - 8;
    //地圖大小
    public static final int MAP_WIDTH = 51200;
    public static final int MAP_HEIGHT = 960;
    // 資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 60;// 每秒更新60次遊戲邏輯
    public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次要花費的奈秒數
    // 畫面更新時間
    public static final int FRAME_LIMIT = 60;
    public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;
    //物件單位大小
    public static final int UNIT_X = 32;
    public static final int UNIT_Y = 32;
    public static final int ACTOR_WIDTH = 56;
    public static final int ACTOR_HEIGHT = 80;
    //持有金幣座標
    public static final int COINCOUNT_X = SCREEN_X - 160;
    public static final int COINCOUNT_Y = 10;//10
    //擊殺怪物座標
    public static final int MONSTERCOUNT_X = SCREEN_X - 160;
    public static final int MONSTERCOUNT_Y = 68;//COINCOUNT_Y+58
    //跑步距離座標
    public static final int DISTANCE_X = SCREEN_X - 500;
    public static final int DISTANCE_Y = 50;
    //剩下時間座標
    public static final int TIME_X = 0;
    public static final int TIME_Y = 10;//10
    //玩家操控command
    public static final int P1_DOWN = 0;
    public static final int P1_LEFT = 1;
    public static final int P1_RIGHT = 2;
    public static final int P1_JUMP = 3;
    public static final int P1_SHOT = 4;
    public static final int P2_DOWN = 5;
    public static final int P2_LEFT = 6;
    public static final int P2_RIGHT = 7;
    public static final int P2_JUMP = 8;
    public static final int P2_SHOT = 9;
    //遊戲時間
    public static final double GAME_TIME =300;
    public static final int BACKGROUND_BOTTOM_WIDTH=2560;
    public static final int BACKGROUND_BOTTOM_HEIGHT=960;
    public static final int BACKGROUND_MIDDLE_WIDTH=3390;
    public static final int BACKGROUND_MIDDLE_HEIGHT=959;
    //磚塊相關
    public static final int BRICKSPEED_LEFT = -6;
    public static final int BRICKSPEED_RIGHT = 6;
    //角色相關
    public static final int PLAYER_EFFECT_CD = 480;//effectCD
    public static final int PLAYER_SKILL_CD = 90;//技能CD
    public static final int PLAYER_SPEED = 8;
    public static final int PLAYER_JUMP=35;
    //effect
    public static final int EFFECT_DELAY=300;
    //鏡頭常數
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    public static final int SPACE = 5;
    public static final int A = 6;
    public static final int D = 7;

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static boolean random(int rate) {
        return random(1, 100) <= rate;
    }
}
