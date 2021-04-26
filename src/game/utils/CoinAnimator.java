/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.utils;

import game.controllers.ImgController;
import game.controllers.SceneController;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author user
 */
public class CoinAnimator {

    public enum CoinType {
        COIN(new int[]{0, 1, 2, 3}, 8),
        COINCOUNT(new int[]{0, 1, 2, 3}, 15);
        private int arr[];
        private int speed;

        CoinType(int arr[], int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    private Image img;
    private final Delay delay;
    private int count;
    private CoinType coinType;

    public CoinAnimator(CoinType coinType) {
        switch (coinType) {
            case COIN:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().coin());
                break;
            case COINCOUNT:
                img = SceneController.instance().irc().tryGetImage(new Path().img().objs().coinCount());
                break;
        }
        this.coinType = coinType;
        this.delay = new Delay(coinType.speed);
        this.delay.loop();
        this.count = 0;
    }

    public void paint(int left, int top, int right, int bottom, Graphics g) {
        g.drawImage(img,
                left, top,
                right, bottom,
                0, 56 * coinType.arr[count],
                42, 56 + 56 * coinType.arr[count], null);
    }

    public void update() {
        if (delay.count()) {
            count = ++count % coinType.arr.length;
        }
    }
}
