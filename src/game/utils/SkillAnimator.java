/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.utils;

import game.controllers.SceneController;
import java.awt.Graphics;
import java.awt.Image;


public class SkillAnimator {

    public enum SkillType {
        FIRE(new int[]{0,0, 1,2, 2,3,3,3,4,4,4,5,5,5,6,6,6,5,5,5,6,6,6,5,5,5,6,6,6}, 3),
        RASENGAN(new int[]{0, 0, 1, 1, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 4, 4, 4, 5, 5, 5, 4, 4, 4, 5, 5, 5, 5}, 2);
        private int arr[];
        private int speed;

        SkillType(int arr[], int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    private Image img;
    private final Delay delay;
    private int count;
    private SkillType skillType;

    public SkillAnimator(SkillType skillType) {
        switch (skillType) {
            case FIRE:
                img = SceneController.instance().irc().tryGetImage(new Path().img().actors().fireball());
                break;
            case RASENGAN:
                img = SceneController.instance().irc().tryGetImage(new Path().img().actors().rasengan());
                break;
        }
        this.skillType = skillType;
        this.delay = new Delay(skillType.speed);
        this.delay.loop();
        this.count = 0;
    }

    public SkillType getSkillType() {
        return this.skillType;
    }

    public void paint(Global.Direction dir, int left, int top, int right, int bottom, Graphics g) {
        switch (skillType) {
            case FIRE:
                if (dir == Global.Direction.LEFT) {
                    switch (skillType.arr[count]) {
                        case 0:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    24, 0,
                                    0, 40, null);
                            return;
                        case 1:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    75, 0,
                                    24, 40, null);
                            return;
                        case 6:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    419, 0,
                                    355, 40, null);
                            return;
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    75 + 70 + 70 * (skillType.arr[count] - 2), 0,
                                    75 + 70 * (skillType.arr[count] - 2), 40, null);
                            return;
                    }
                } else {
                    switch (skillType.arr[count]) {
                        case 0:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    0, 0,
                                    24, 40, null);
                            return;
                        case 1:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    24, 0,
                                    75, 40, null);
                            return;
                        case 6:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    355, 0,
                                    419, 40, null);
                            return;
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    75 + 70 * (skillType.arr[count] - 2), 0,
                                    75 + 70 + 70 * (skillType.arr[count] - 2), 40, null);
                            return;
                    }
                }
                break;
            case RASENGAN:
                if (dir == Global.Direction.LEFT) {
                    switch (skillType.arr[count]) {
                        case 0:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    23, 0,
                                    0, 30, null);
                            break;
                        case 1:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    55, 0,
                                    23, 30, null);
                            break;
                        case 2:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    96, 0,
                                    55, 30, null);
                            break;
                        case 3:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    128, 0,
                                    101, 30, null);
                            break;
                        case 4:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    154, 0,
                                    128, 30, null);
                            break;
                        case 5:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    186, 0,
                                    154, 30, null);
                            break;
                    }
                } else {
                    switch (skillType.arr[count]) {
                        case 0:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    0, 0,
                                    23, 30, null);
                            break;
                        case 1:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    23, 0,
                                    55, 30, null);
                            break;
                        case 2:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    55, 0,
                                    96, 30, null);
                            break;
                        case 3:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    101, 0,
                                    128, 30, null);
                            break;
                        case 4:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    128, 0,
                                    154, 30, null);
                            break;
                        case 5:
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    154, 0,
                                    186, 30, null);
                            break;
                    }
                    break;
                }
        }
    }

    public void update() {
        if (delay.count()) {
            count = ++count % skillType.arr.length;
        }
    }
}
