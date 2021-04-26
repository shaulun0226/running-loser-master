package game.gameobj;

import game.controllers.SceneController;
import game.utils.Path;
import java.awt.Graphics;
import java.awt.Image;

public class Background extends GameObject {

    public enum BackgroundType {
        BOTTOM,
        MIDDLE;
    }
    private Image img;
    private BackgroundType backgroundType;
    private int paintRight;
    private int paintLeft;
    private int paintTop;
    private int paintBottom;

    public Background(int x, int y, int width, int height, BackgroundType backgroundType) {
        super(x, y, width, height);
        this.backgroundType = backgroundType;
        switch (backgroundType) {
            case MIDDLE:
                img = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().backgroundMiddleW3390H960());
                break;
            case BOTTOM:
                img = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().backgroundBottomW2560H960());
                break;
        }
        this.paintLeft =painter().left();
        this.paintRight = painter().right();
        this.paintTop = painter().top();
        this.paintBottom = painter().bottom();

    }

    public void setPaintRight(int right) {
        this.paintRight = right;
    }

    public void setPaintLeft(int left) {
        this.paintLeft = left;
    }
    public void setPaintTop(int top){
        this.paintTop = top;
    }
    public void setPaintBottom(int bottom){
        this.paintBottom = bottom;
    }
    
    public int getPaintRight(){
        return this.paintRight;
    }
    public int getPaintLeft(){
        return this.paintLeft;
    }
    public int getPaintTop(){
        return this.paintTop;
    }
    public int getPaintBottom(){
        return this.paintBottom;
    }

    @Override
    public void paintComponent(Graphics g) {
                g.drawImage(img, paintLeft, paintTop, paintRight, paintBottom,
                        paintLeft-painter().left(), paintTop, paintRight-painter().left(), paintBottom, null);
    }

    @Override
    public void update() {
    }

}
