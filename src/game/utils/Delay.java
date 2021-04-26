/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.utils;

/**
 *
 * @author user1
 */
public class Delay {

    private int count;
    private int countLimit;
    private boolean isPause;
    private boolean isLoop;

    public Delay(int countLimit) {
        this.countLimit = countLimit;
        count = 0;
        isPause = true;
        this.isLoop = false;
    }

    public void setLimit(int limit) {
        this.countLimit = limit;
    }

    public void stop() {
        isPause = true;
        isLoop = false;
        count = 0;
    }

    public void pause() {
        isPause = true;
    }

    public void play() {
        isPause = false;
        isLoop = false;
    }

    public void loop() {
        isPause = false;
        isLoop = true;
    }

    public boolean count() {
        if (isPause) {
            return false;
        }
        if (count >= countLimit) { // count > limit時才true
            if (isLoop) {       // 
                count = 0;
            }else{
                stop();
            }
            return true;
        }
        count++;  
        return false;   // +到countLimit後，又是loop才會變true
    }
}
