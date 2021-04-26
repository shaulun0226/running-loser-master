/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameobj;

import game.controllers.SceneController;
import game.utils.Delay;
import game.utils.Global;
import game.utils.Global.Direction;
import game.utils.MonsterAnimator;
import game.utils.MonsterAnimator.MonsterType;
import game.utils.Path;
import game.utils.Vector;
import java.awt.Graphics;
import java.awt.Image;

public class Monster extends GameObject {
    
    public enum State {   // 飛彈用狀態
        NORMAL,         // 正常
        BOOM,            // 爆炸
        REMOVED      // 移除
    }

    private MonsterAnimator monsterAnimator;
    private Direction dir;
    private MonsterType monsterType;
    private Vector vector;
    private boolean moveReverse;
    private boolean remove;
    private Delay delay;
    private int count;
    private State state;
    private Image imgB;
    //花
    private Delay delayPiranha;
    private int[] arr;
    
    private int ySpeed; // 重力加速度
    private boolean canMove;

    public Monster(int x, int y, MonsterType monsterType, Vector vector, Delay delay) {
        super(x, y, 20, 20);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.delay = delay;
        this.delay.loop();
        this.delayPiranha = new Delay(32);
        this.delayPiranha.loop();
        this.dir = Global.Direction.RIGHT;
        this.ySpeed = 0;
    }

    public Monster(int x, int y, int width, int height, MonsterType monsterType, Vector vector, Delay delay) {
        super(x, y, width, height);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.count = 0;
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.delay = delay;
        this.delay.loop();
        this.delayPiranha = new Delay(32);
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.arr = new int[] {0,1,2,3,4,3,2,1}; // 噴火用
        this.delayPiranha.loop();
        this.dir = Direction.RIGHT;
        this.ySpeed = 0;
    }

    public Monster(int x, int y, int width, int height, MonsterType monsterType, Vector vector, Delay delay, Direction dir) {
        super(x, y, width, height);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.delay = delay;
        this.delay.loop();
        this.delayPiranha = new Delay(32);
        this.delayPiranha.loop();
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.dir = dir;
        this.state = State.NORMAL;        // 飛彈狀態
        imgB = SceneController.instance().irc().tryGetImage(new Path().img().monsters().boom2()); // 飛彈爆炸
        this.ySpeed = 0;
    }
    
    public Monster(int x, int y, MonsterType monsterType, Vector vector) {
        super(x, y, 20, 20);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.dir = Global.Direction.RIGHT;
        this.ySpeed = 0;
    }

    public MonsterType getMonsterType() {
        return this.monsterType;
    }

    public boolean getRemove() {
        return this.remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
    
    public void setState(State state) {         // 設定狀態
        this.state = state;
    }
    public State getState() {                      
        return state;
    }
    
    public int getYSpeed() {
        return this.ySpeed;
    }

    public void setYSpeed(int y) {
        this.ySpeed = y;
    }

    public void fall() {//重力往下掉
            translateY(ySpeed);
    }

    public boolean getCanMove() {
        return this.canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    public Direction getDirection(){
        return this.dir;
    }
    
    

    @Override
    public void paintComponent(Graphics g) {
        if (monsterType == MonsterType.BULLET && this.state == State.BOOM) {
            g.drawImage(imgB, painter().left() - painter().width()/2, painter().top() - painter().height()/2, painter().width()*2, 
                        painter().height()*2,null);
        } else {
            monsterAnimator.paint(monsterType, dir, painter().left(), painter().top(),
                    painter().right(), painter().bottom(), g);
        }
    }

    @Override
    public void update() {
        if (monsterType == MonsterType.GHOST || monsterType == MonsterType.PIG          // 移動的怪
                || monsterType == MonsterType.KAPA || monsterType == MonsterType.TACO 
                || monsterType == MonsterType.GREENBOSS) {
            if (moveReverse) {   
                this.vector = new Vector(Math.abs(this.vector.vx()), -Math.abs(this.vector.vy()));
                this.dir = Direction.RIGHT;
            } else {
                this.vector = new Vector(-Math.abs(this.vector.vx()), Math.abs(this.vector.vy()));
                this.dir = Direction.LEFT;
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
            monsterAnimator.update();
        }
        
        if (monsterType == MonsterType.FIRE) {
            if (moveReverse) {   
                this.vector = new Vector(Math.abs(this.vector.vx()), -Math.abs(this.vector.vy()));
                this.dir = Direction.JUMP;
            } else {
                this.vector = new Vector(-Math.abs(this.vector.vx()), Math.abs(this.vector.vy()));
                this.dir = Direction.DOWN;
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
            monsterAnimator.update();
        }
        
        if (monsterType == MonsterType.PIRANHA) {
            if (moveReverse) {
                translateX((int) this.vector.vx());
                translateY(-(int) this.vector.vy());
                if (delay.count()) {
                    this.vector = new Vector(-this.vector.vx(), -this.vector.vy());
                }
            }
            if (delayPiranha.count()) {
                    if (moveReverse) {
                        moveReverse = false;
                    } else {
                        moveReverse = true;
                    }
                }
            monsterAnimator.update();
        }
        if (monsterType == MonsterType.BULLET) {    // 飛彈的更新
            if (state == State.NORMAL) {          // 若 正常狀態
                translateX((int) vector.vx());
                translateY((int) vector.vy());
            } else if (state == State.BOOM) {    // 若 爆炸狀態
                delay.play();                                // 
            }
            if (delay.count()) {
                remove = true;
            }
            monsterAnimator.update();
        }
        
        if (monsterType == MonsterType.FIREPILLAR) {  // 噴火
                if (delay.count()) {   // 用delay來跑count
                    count = ++count % arr.length;
                    if (count == 5 || count == 6 || count == 7 || count == 0){
                        setTop(this.collider().top() + 30);
                    } else {
                        setTop(this.collider().top() - 30);
                    }
                }
            monsterAnimator.update();
        }    
    }

}
