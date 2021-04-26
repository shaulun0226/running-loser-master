/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameobj;

import game.camera.MapInformation;
import game.utils.GameKernel.GameInterface;
import game.utils.Global;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author user1
 */
public abstract class GameObject implements GameInterface {

    private  Rect collider;
    private  Rect painter;
    private Rect lastRect;

    public GameObject(int x, int y, int width, int height) {
        this(x, y, width, height, x, y, width, height);
    }

    public GameObject(Rect rect) {
        collider = new Rect(rect);
        painter = new Rect(rect);
    }

    public GameObject(int x, int y, int width, int height,
            int x2, int y2, int width2, int height2) {
        collider = Rect.genWithCenter(x, y, width, height);
        painter = Rect.genWithCenter(x2, y2, width2, height2);
    }

    public GameObject(Rect rect, Rect rect2) {
        collider = new Rect(rect);
        painter = new Rect(rect2);
    }

    public void changeBottom(int y) {
        translateY(y - collider.bottom());
    }

    public void changeTop(int y) {
        translateY(y - collider.top());
    }

    public void changeLeft(int x) {
        translateX(x - collider.left());
    }

    public void changeRight(int x) {
        translateX(x - collider.right());
    }

    public boolean outOfScreen() {
        if (painter.bottom() <= 0) {
            return true;
        }
        if (painter.right() <= 0) {
            return true;
        }
        if (painter.left() >= MapInformation.mapInfo().right()) {
            return true;
        }
        return painter.top() >= MapInformation.mapInfo().bottom();
    }

    public boolean touchTop() {
        return collider.top() <= 0;
    }

    public boolean touchLeft() {
        return collider.left() <= 0;
    }

    public boolean touchRight() {
        return collider.right() >= MapInformation.mapInfo().right();
    }

    public boolean touchBottom() {
        return collider.bottom() >= MapInformation.mapInfo().bottom();
    }

    public boolean isCollision(GameObject obj) {
        return collider.overlap(obj.collider);
    }

    public final void setRect(Rect rect) {
        collider.setCenter(rect.centerX(), rect.centerY());
        painter.setCenter(rect.centerX(), rect.centerY());
    }
    public final void resetRect(Rect rect){
        painter = new Rect(rect);
        collider = new Rect(rect);
    }

    public final void translate(int x, int y) {
        collider.translate(x, y);
        painter.translate(x, y);
    }

    public final void translateX(int x) {
        collider.translateX(x);
        painter.translateX(x);
    }

    public final void translateY(int y) {
        collider.translateY(y);
        painter.translateY(y);
    }
    public Rect getLast(){
        return this.lastRect;
    }
    public void setLast(Rect rect){
        this.lastRect = new Rect(rect);
    }
    public final Rect collider() {
        return collider;
    }

    public final Rect painter() {
        return painter;
    }


    public void setPainter(Rect rect){
        this.painter = new Rect(rect);
    }
    
    public void setTop(int top) {      // 為了噴火加的
        this.collider.setTop(top);
        this.painter.setTop(top);
    }


    @Override
    public final void paint(Graphics g) {
        paintComponent(g);
        if (Global.IS_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect(this.painter.left(), this.painter.top(), this.painter.width(), this.painter.height());
            g.setColor(Color.GREEN);
            g.drawRect(this.collider.left(), this.collider.top(), this.collider.width(), this.collider.height());
            g.setColor(Color.BLACK);
        }

    }

    public abstract void paintComponent(Graphics g);
}
