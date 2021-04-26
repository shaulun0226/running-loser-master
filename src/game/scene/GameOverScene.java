/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.scene;

import game.utils.CommandSolver;
import game.utils.Global;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author user1
 */
public class GameOverScene extends Scene {

    @Override
    public void sceneBegin() {

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.drawString("Game Over", Global.SCREEN_X / 2, Global.SCREEN_Y / 2);
    }

    @Override
    public void update() {

    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
