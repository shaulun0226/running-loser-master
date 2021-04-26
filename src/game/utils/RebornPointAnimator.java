package game.utils;

import game.controllers.ImgController;
import game.controllers.SceneController;
import java.awt.Graphics;
import java.awt.Image;

public class RebornPointAnimator {

    public enum RebornPointType {
        UNTOUCH(new int[]{0, 1, 2, 3}, 5),
        TOUCHED(new int[]{0, 1, 2, 3}, 1);
        private int arr[];
        private int speed;

        RebornPointType(int arr[], int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }
    private Image img;
    private Image imgTouch;
    private final Delay delay;
    private int count;
    private RebornPointType type;

    public RebornPointAnimator() {
        this.img = SceneController.instance().irc().tryGetImage(new Path().img().objs().rebornPoint1());
        this.imgTouch = SceneController.instance().irc().tryGetImage(new Path().img().objs().rebornPoint2());
        this.type = RebornPointType.UNTOUCH;
        this.delay = new Delay(60);
        this.delay.loop();
        this.count = 0;
    }

    public void setType(RebornPointType type) {
        this.type = type;
    }

    public void paint(int left, int top, int right, int bottom, Graphics g) {
        switch (type) {
            case UNTOUCH:
                g.drawImage(img,
                        left, top,
                        right, bottom,
                        0, 32 * type.arr[count],
                        32, 32 + 32 * type.arr[count], null);
                break;
            case TOUCHED:
                g.drawImage(imgTouch,
                        left, top,
                        right, bottom,
                        0, 32 * type.arr[count],
                        32, 32 + 32 * type.arr[count], null);
                break;
        }
    }

    public void update() {
        if (delay.count()) {
            count = ++count % type.arr.length;
        }
    }
}
