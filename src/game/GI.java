package game;


import game.controllers.SceneController;
import game.gameobj.Player;
import game.scene.EndingScene;
import game.scene.InternetScene;
import game.scene.MenuScene;
import game.utils.*;
import game.utils.CommandSolver.KeyListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GI implements GameKernel.GameInterface, CommandSolver.MouseCommandListener, CommandSolver.KeyListener {

    public GI() throws IOException {
        SceneController.instance().change(new MenuScene());
        //        ArrayList<Player> players = new ArrayList<>();
//        players.add(new Player(PlayerAnimator.ActorType.NARUTOHD, 5000, 300));
//        players.add(new Player(PlayerAnimator.ActorType.SASUKEHD, 10000, 700));
//        players.get(0).setCoinCount(10000);
//        players.get(1).setCoinCount(2000);
    }
        


    @Override
    public void paint(Graphics g) { //繪畫
        SceneController.instance().paint(g);
    }

    @Override
    public void update() { //邏輯
        SceneController.instance().update();
    }

    @Override
    public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if (SceneController.instance().mouseListener() != null) {
            SceneController.instance().mouseListener().mouseTrig(e, state, trigTime);
        }
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        KeyListener kl = SceneController.instance().keyListener();
        if (kl != null) {
            kl.keyPressed(commandCode, trigTime);
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        KeyListener kl = SceneController.instance().keyListener();
        if (kl != null) {
            kl.keyReleased(commandCode, trigTime);
        }
    }

    @Override
    public void keyTyped(char c, long trigTime) {
        if (SceneController.instance().keyListener() != null) {
            SceneController.instance().keyListener().keyTyped(c, trigTime);
        }
    }

}
