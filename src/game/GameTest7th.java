package game;

import game.utils.GameKernel;
import static game.utils.Global.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author user1
 */
public class GameTest7th {
    
    public static void main(String[] args) throws IOException {
        JFrame jf = new JFrame();// 遊戲視窗本體
        jf.setTitle("Running Loser");// 視窗標題
        jf.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);// 視窗大小
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 關閉視窗時關閉程式
        
        GI gi = new GI();// 遊戲的本體(邏輯 + 畫面處理)
        int[][] commands = {//鍵盤按鍵對應的指令
            {KeyEvent.VK_A, P1_LEFT},
            {KeyEvent.VK_D, P1_RIGHT},
            {KeyEvent.VK_S, P1_DOWN},
            {KeyEvent.VK_W, P1_JUMP},
            {KeyEvent.VK_SPACE, P1_SHOT},
            {KeyEvent.VK_LEFT,P2_LEFT},
            {KeyEvent.VK_RIGHT, P2_RIGHT},
            {KeyEvent.VK_DOWN, P2_DOWN},
            {KeyEvent.VK_UP, P2_JUMP},
            {KeyEvent.VK_CONTROL, P2_SHOT},
            };
        
        GameKernel gk = new GameKernel.Builder(gi, LIMIT_DELTA_TIME, NANOSECOND_PER_UPDATE)
                .initListener(commands)
                .enableKeyboardTrack(gi)
//                .keyTypedMode()
                .keyCleanMode()
//                .initListener()
                .enableMouseTrack(gi)
                .gen();
        
        jf.add(gk);
        
        jf.setVisible(true);
        gk.run(IS_DEBUG);
    }
}
