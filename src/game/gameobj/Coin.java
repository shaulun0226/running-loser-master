/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameobj;

import game.utils.CoinAnimator;
import game.utils.CoinAnimator.CoinType;
import static game.utils.Global.*;
import java.awt.Graphics;

/**
 *
 * @author user
 */
public class Coin extends GameObject {

    private CoinAnimator coinAnimator;

    public Coin(int x, int y,int width,int height,CoinType coinType) {
        super(x, y, width, height);
        coinAnimator = new CoinAnimator(coinType);
    }

    @Override
    public void paintComponent(Graphics g) {
        coinAnimator.paint(painter().left(), painter().top(), painter().right(), painter().bottom(), g);
    }

    @Override
    public void update() {
        coinAnimator.update();
    }

}
