package game.gameobj;

import game.controllers.ImgController;
import game.utils.Delay;
import game.utils.Path;
import java.awt.Graphics;
import java.awt.Image;

public class Bullet extends GameObject {


    public enum State {
        NORMAL,
        BOOM,
        REMOVED
    }

    private Image img1, img2;
    private State state;
    private Delay delay;

    public Bullet(int x, int y) {
        super(x, y, 32, 32, x, y, 32, 32);
        state = State.NORMAL;
        img1 = new ImgController().tryGetImage(new Path().img().monsters().bullet());
        delay = new Delay(30);  
    }

    @Override
    public void paintComponent(Graphics g) {
                g.drawImage(img1,
                        painter().left(),
                        painter().top(),
                        painter().width(),
                        painter().height(), null);
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    @Override
    public void update() {
        if (state == State.NORMAL) {
            translateX(-8);
        }
        /*else if (state == State.BOOM) {    
        delay.play();                                
    }

    if (delay.count()) {    // �Ydelay.count()�Otrue
        state = State.REMOVED;          
    }*/
    }
}
