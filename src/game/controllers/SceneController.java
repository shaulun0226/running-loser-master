/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.controllers;

import game.scene.Scene;
import game.utils.CommandSolver;
import java.awt.Graphics;

/**
 *
 * @author user1
 */
public class SceneController {

    private static SceneController sceneController;

    private SceneController() {
        lastIrc = new ImgController();
        currentIrc = new ImgController();
    }

    public static SceneController instance() {
        if (sceneController == null) {
            sceneController = new SceneController();
        }
        return sceneController;
    }

    private Scene lastScene;
    private Scene currentScene;
    private ImgController lastIrc;
    private ImgController currentIrc;

    public void change(Scene scene) {
        lastScene = currentScene;

        ImgController tmp = currentIrc;
        currentIrc = lastIrc;
        lastIrc = tmp;

        if (scene != null) {
            scene.sceneBegin();
        }
        currentScene = scene;
    }

    public void paint(Graphics g) {
        if (currentScene != null) {
            currentScene.paint(g);
        }
    }

    public void update() {
        if (lastScene != null) {
            lastScene.sceneEnd();
            lastIrc.clear();
            lastScene = null;
        }
        if (currentScene != null) {
            currentScene.update();
        }
    }

    public CommandSolver.MouseCommandListener mouseListener() {
        return currentScene.mouseListener();
    }

    public CommandSolver.KeyListener keyListener() {
        return currentScene.keyListener();
    }

    public ImgController irc() {
        return currentIrc;
    }
}
