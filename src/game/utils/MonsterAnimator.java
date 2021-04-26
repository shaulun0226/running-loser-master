/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.utils;

import game.controllers.ImgController;
import game.controllers.SceneController;
import game.utils.Delay;
import game.utils.Global;
import game.utils.Global.Direction;
import static game.utils.MonsterAnimator.MonsterType.*;
import game.utils.Path;
import java.awt.Graphics;
import java.awt.Image;

public class MonsterAnimator {

    public enum MonsterType {
        BULLET(new int[]{0,1,2,3}, 5),     //砲彈
        GHOST(new int[]{0, 1, 2, 3, 4}, 10),  // 鬼
        FLYTURTLE(new int[]{0}, 0),  // 暫無
        PIRANHA(new int[]{0,1,2,3}, 5),  // 食人花
        PIG(new int[] {0,1}, 10), // 豬
        KAPA(new int[] {0,1}, 10),  // 河童
        TACO(new int[] {0,1}, 10), // 章魚
        GREENBOSS(new int[] {0,1}, 10),  // 綠色外星人
        FIRE(new int[] {0,1}, 10), // 上下飛的火
        FIREPILLAR(new int[] {0,1,2,3,4,5,6,7}, 10), // 噴火
        
        // 刺刺
        THORN_UP, // 刺向上
        THORN_DOWN, // 刺向下
        THORN_LEFT, // 刺向左
        THORN_RIGHT;  // 刺向右
                
        private int[] arr;
        private int speed;

        MonsterType(int[] arr, int speed) {
            this.arr = arr;
            this.speed = speed;
        }
        MonsterType() {  // 給刺用的建構子
            
        }
    }
    private Image img;
    private final Delay delay;
    private int count;
    private MonsterType monsterType;
    private int state;

    public MonsterAnimator(MonsterType monsterType) {
        this.monsterType = monsterType;
        switch (monsterType) {
            case GHOST:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().ghost());
                break;
            case PIRANHA:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().piranha());
                break;
            case BULLET:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().bullet());
                break;
           case PIG:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().pig());
                break;
            case KAPA:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().kapa());
                break;
            case TACO:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().taco());
                break; 
            case GREENBOSS:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().green());
                break; 
            case FIRE:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().fire());
                break;  
            case THORN_UP:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().thorn_UP());
                break;
            case THORN_DOWN:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().thorn_DOWN());
                break;
            case THORN_LEFT:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().thorn_LEFT());
                break;
            case THORN_RIGHT:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().thorn_RIGHT());
                break;
            case FIREPILLAR:
                img = SceneController.instance().irc().tryGetImage(new Path().img().monsters().firePillar());
        }
        this.delay = new Delay(monsterType.speed);
        this.delay.loop();
        this.count = 0;
    }

// 畫出    小怪
    public void paint(MonsterType monsterType, Global.Direction dir, int left, int top, int right, int bottom, Graphics g) {
        switch (monsterType) {
            case GHOST:
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        0 + 32 * monsterType.arr[count],
                        32,
                        32 + 32 * monsterType.arr[count], null);
                break;
            case PIG:
                int dp;
                if (dir == Direction.RIGHT) {
                    dp = 2;
                } else {
                    dp = 0;
                }
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        0 + 42 * (monsterType.arr[count]+dp),
                        42,
                        42 + 42 * (monsterType.arr[count]+dp), null);
                break;
            case KAPA:
                int dk;
                if (dir == Direction.RIGHT) {
                    dk = 2;
                } else {
                    dk = 0;
                }
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        0 + 32 * (monsterType.arr[count]+dk),
                        32,
                        32 + 32 * (monsterType.arr[count]+dk), null);
                break;
            case TACO:
                int dt;
                if (dir == Direction.RIGHT) {
                    dt = 2;
                } else {
                    dt = 0;
                }
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        0 + 44 * (monsterType.arr[count]+dt),
                        32,
                        44 + 44 * (monsterType.arr[count]+dt), null);
                break;
            case GREENBOSS:
                int dg;
                if (dir == Direction.RIGHT) {
                    dg = 2;
                } else {
                    dg = 0;
                }
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        0 + 42 * (monsterType.arr[count]+dg),
                        32,
                        42 + 42 * (monsterType.arr[count]+dg), null);
                break;
            case FIRE:
                int df;
                if (dir == Direction.DOWN) {
                    df = 2;
                } else {
                    df = 0;
                }
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        0 + 32 * (monsterType.arr[count]+df),
                        32,
                        32 + 32 * (monsterType.arr[count]+df), null);
                break;
            case PIRANHA:
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        42 * monsterType.arr[count],
                        32,
                        42 + 42 * monsterType.arr[count], null);
                break;
            case BULLET:
                if (dir == Direction.LEFT) {
                    g.drawImage(img,
                            left, top, right, bottom,
                            0,
                            0 + 28 * monsterType.arr[count],
                            32,
                            28 + 28 * monsterType.arr[count], null);
                    break;
                }else{
                    g.drawImage(img,
                            left, top, right, bottom,
                            0,
                            112 + 28 * monsterType.arr[count],
                            32,
                            112+28 + 28 * monsterType.arr[count], null);
                    break;
                }
            case FIREPILLAR:
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        0 + 32 * monsterType.arr[count],
                        16,
                        32 + 32 * monsterType.arr[count], null);
                break;
            case THORN_UP:
            case THORN_DOWN:
            case THORN_LEFT:
            case THORN_RIGHT:
                g.drawImage(img, left, top, right, bottom, 0, 0, 32, 32, null);
                break;
        }

    }

    public void update() {
        if (delay.count()) {   // 用delay來跑count
            count = ++count % monsterType.arr.length;
        }
    }
}
