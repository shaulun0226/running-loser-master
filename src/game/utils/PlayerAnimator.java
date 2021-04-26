package game.utils;

import game.controllers.ImgController;
import game.controllers.SceneController;
import game.utils.Global.Direction;
import static game.utils.PlayerAnimator.State.*;
import java.awt.Graphics;
import java.awt.Image;

public class PlayerAnimator {

    public enum ActorType {
        MARIO(1),
        LUIGI(2),
        NARUTO(3),
        SASUKE(4),
        NARUTOHD(5),
        SASUKEHD(6),
        SAKURAHD(7);
        private int value;

        ActorType(int i) {
            this.value = i;
        }
    }

    public enum State {
        WALK(new int[]{0, 1, 2, 3, 4, 5/*, 2, 1*/}, 2),
        STOP(new int[]{0, 1, 2, 3}, 8),
        JUMP(new int[]{0, 1, 1, 1, 2, 2}, 5),
        SQUAT(new int[]{0}, 2),
        DEAD(new int[]{0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, 2),
        REBORN(new int[]{0}, 0),
        SAKURASTOP(new int[]{0,1,2},15);
        private int arr[];
        private int speed;

        State(int arr[], int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    private Image img;
    private Image imgDead;
    private Image imgRun;
    private Image imgJump;
    private Image imgStop;
    private Image imgSquat;
    private final Delay delay;
    private int count;
    private State state;
    private ActorType actorType;
//    private  final int type;

    public PlayerAnimator(State state, ActorType actorType) {  // 用選擇決定img
        switch (actorType) {
            case MARIO:
                img = SceneController.instance().irc().tryGetImage(new Path().img().actors().mario());
                imgDead = SceneController.instance().irc().tryGetImage(new Path().img().actors().marioDead());
                break;
            case LUIGI:
                img = SceneController.instance().irc().tryGetImage(new Path().img().actors().luigi());
                imgDead = SceneController.instance().irc().tryGetImage(new Path().img().actors().luigiDead());
                break;
            case NARUTO:
                img = SceneController.instance().irc().tryGetImage(new Path().img().actors().naruto());
                imgDead = null;
                break;
            case SASUKE:
                img = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasuke());
                imgDead = null;
                break;
            case NARUTOHD:
//                img = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHD());
                imgRun = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHDRun());
                imgJump = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHDJump());
                imgStop = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHDStop());
                imgDead = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHDDead());
                imgSquat = SceneController.instance().irc().tryGetImage(new Path().img().actors().narutoHDSquat());
                break;
            case SASUKEHD:
                imgRun = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHDRun());
                imgJump = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHDJump());
                imgStop = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHDStop());
                imgDead = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHDDead());
                imgSquat = SceneController.instance().irc().tryGetImage(new Path().img().actors().sasukeHDSquat());
                break;
            case SAKURAHD:
                imgStop = SceneController.instance().irc().tryGetImage(new Path().img().actors().sakuraHDStop());
            default:
                img = null;
                imgDead = null;
        }
        delay = new Delay(0);
        delay.loop();
        count = 0;
        this.actorType = actorType;
        setState(state);
    }

    public void setState(State state) {
        this.state = state;
        this.delay.setLimit(state.speed);
        count = 0;
    }

    public State State() {
        return this.state;
    }

    public void paint(Direction dir, int left, int top, int right, int bottom, Graphics g) {
        switch (actorType) {
            case MARIO:
                switch (dir) {
                    case RIGHT:
                        if (state == WALK) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    500 + 30,
                                    30 + 100 * state.arr[count],
                                    500 + 70,
                                    100 + 100 * state.arr[count], null);
                            return;
                        }
                        if (state == JUMP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    500 + 30,
                                    30 + 100 * state.arr[count],
                                    500 + 70,
                                    100 + 100 * state.arr[count], null);
                            return;
                        }
                        if (state == STOP) {
                            if (state.arr[count] == 0) {
                                g.drawImage(img,
                                        left, top,
                                        right, bottom,
                                        630,
                                        430 - 200 * state.arr[count],
                                        670,
                                        500 - 200 * state.arr[count], null);
                                return;
                            }
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    730,
                                    30 + 100 * state.arr[count],
                                    770,
                                    100 + 100 * state.arr[count], null);
                            return;
                        }
                        if (state == SQUAT) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    530,
                                    660,
                                    570,
                                    700, null);
                        }
                    case LEFT:
                        if (state == WALK) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    500 - 70,
                                    830 - 100 * state.arr[count],
                                    500 - 30,
                                    900 - 100 * state.arr[count], null);
                            return;
                        }
                        if (state == JUMP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    500 - 70,
                                    830 - 100 * state.arr[count],
                                    500 - 30,
                                    900 - 100 * state.arr[count], null);
                            return;
                        }
                        if (state == STOP) {
                            if (state.arr[count] == 0) {
                                g.drawImage(img,
                                        left, top,
                                        right, bottom,
                                        330,
                                        430 - 200 * state.arr[count],
                                        370,
                                        500 - 200 * state.arr[count], null);
                                return;
                            }
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    230,
                                    830 - 100 * state.arr[count],
                                    270,
                                    900 - 100 * state.arr[count], null);
                            return;
                        }
                        if (state == SQUAT) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    430,
                                    260,
                                    470,
                                    300, null);
                        }
                    case JUMP:

                }
            case LUIGI:
                switch (dir) {
                    case RIGHT:
                        if (state == WALK) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    500 + 30,
                                    20 + 100 * state.arr[count],
                                    500 + 70,
                                    100 + 100 * state.arr[count], null);
                            return;
                        }
                        if (state == JUMP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    500 + 30,
                                    20 + 100 * state.arr[count],
                                    500 + 70,
                                    100 + 100 * state.arr[count], null);
                            return;
                        }
                        if (state == STOP) {
                            if (state.arr[count] == 0) {
                                g.drawImage(img,
                                        left, top,
                                        right, bottom,
                                        630,
                                        420 - 200 * state.arr[count],
                                        670,
                                        500 - 200 * state.arr[count], null);
                                return;
                            }
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    730,
                                    20 + 100 * state.arr[count],
                                    770,
                                    100 + 100 * state.arr[count], null);
                            return;
                        }
                        if (state == SQUAT) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    530,
                                    660,
                                    570,
                                    700, null);
                        }
                        if (state == DEAD) {
                            g.drawImage(imgDead,
                                    left, top,
                                    right, bottom,
                                    0,
                                    0,
                                    32,
                                    32, null);
                        }
                    case LEFT:
                        if (state == WALK) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    500 - 70,
                                    820 - 100 * state.arr[count],
                                    500 - 30,
                                    900 - 100 * state.arr[count], null);
                            return;
                        }
                        if (state == JUMP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    500 - 70,
                                    820 - 100 * state.arr[count],
                                    500 - 30,
                                    900 - 100 * state.arr[count], null);
                            return;
                        }
                        if (state == STOP) {
                            if (state.arr[count] == 0) {
                                g.drawImage(img,
                                        left, top,
                                        right, bottom,
                                        330,
                                        420 - 200 * state.arr[count],
                                        370,
                                        500 - 200 * state.arr[count], null);
                                return;
                            }
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    230,
                                    820 - 100 * state.arr[count],
                                    270,
                                    900 - 100 * state.arr[count], null);
                            return;
                        }
                        if (state == SQUAT) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    430,
                                    260,
                                    470,
                                    300, null);
                        }
                        if (state == DEAD) {
                            g.drawImage(imgDead,
                                    left, top,
                                    right, bottom,
                                    0,
                                    0,
                                    32,
                                    32, null);
                        }
                    case JUMP:
                }
            case NARUTO:
                switch (dir) {
                    case RIGHT:
                        if (state == WALK) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    235 + 28 * state.arr[count],
                                    206,
                                    235 + 28 + 28 * state.arr[count],
                                    230, null);
                            return;
                        }
                        if (state == JUMP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    257,
                                    230,
                                    283,
                                    265, null);
                            return;
                        }
                        if (state == STOP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    292,
                                    198,
                                    313,
                                    230, null);
                            return;
                        }
                        if (state == SQUAT) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    359,
                                    62,
                                    382,
                                    87, null);
                            return;
                        }
                        if (state == DEAD) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    280,
                                    253,
                                    320,
                                    267, null);
                            return;
                        }
                    case LEFT:
                        if (state == WALK) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    235 - 28 - 28 * state.arr[count],
                                    206,
                                    235 - 28 * state.arr[count],
                                    230, null);
                            return;
                        }
                        if (state == JUMP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    188,
                                    230,
                                    213,
                                    265, null);
                            return;
                        }
                        if (state == STOP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    134,
                                    198,
                                    155,
                                    230, null);
                            return;
                        }
                        if (state == SQUAT) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    359,
                                    62,
                                    382,
                                    87, null);
                            return;
                        }
                        if (state == DEAD) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    150,
                                    253,
                                    190,
                                    267, null);
                            return;
                        }
                    case JUMP:

                }
            case SASUKE:
                switch (dir) {
                    case RIGHT:
                        if (state == WALK) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    299 + 33 * state.arr[count],
                                    48,
                                    299 + 33 + 33 * state.arr[count],
                                    76, null);
                            return;
                        }
                        if (state == JUMP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    344,
                                    4,
                                    368,
                                    40, null);
                            return;
                        }
                        if (state == STOP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    483,
                                    42,
                                    507,
                                    76, null);
                            return;
                        }
                        if (state == SQUAT) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    368,
                                    51,
                                    395,
                                    77, null);
                            return;
                        }
                        if (state == DEAD) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    370,
                                    22,
                                    410,
                                    40, null);
                            return;
                        }
                    case LEFT:
                        if (state == WALK) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    299 - 33 - 33 * state.arr[count],
                                    48,
                                    299 - 33 * state.arr[count],
                                    76, null);
                            return;
                        }
                        if (state == JUMP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    228,
                                    4,
                                    252,
                                    40, null);
                            return;
                        }
                        if (state == STOP) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    92,
                                    42,
                                    116,
                                    76, null);
                            return;
                        }
                        if (state == SQUAT) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    368,
                                    51,
                                    395,
                                    77, null);
                            return;
                        }
                        if (state == DEAD) {
                            g.drawImage(img,
                                    left, top,
                                    right, bottom,
                                    185,
                                    22,
                                    225,
                                    40, null);
                            return;
                        }
                }
            case NARUTOHD:
                switch (dir) {
                    case RIGHT:
                        switch (state) {
                            case WALK:
                                if (state.arr[count] % 3 != 2) {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            1 + 68 * state.arr[count] - 11 * (state.arr[count] / 3),
                                            0,
                                            1 + 68 + 68 * state.arr[count] - 11 * (state.arr[count] / 3),
                                            52, null);
                                } else {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            1 + 68 * (state.arr[count]) - 68 * (state.arr[count] / 3) + 57 * (state.arr[count] / 3),
                                            0,
                                            1 + 68 * (state.arr[count]) - 68 * (state.arr[count] / 3) + 57 * ((state.arr[count] + 1) / 3),
                                            52, null);
                                }
                                return;
                            case JUMP:
                                switch (state.arr[count]) {
                                    case 0:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                1,
                                                0,
                                                47,
                                                75, null);
                                        break;
                                    case 1:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                47,
                                                0,
                                                100,
                                                75, null);
                                        break;
                                    case 2:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                102,
                                                0,
                                                162,
                                                75, null);
                                        break;
                                }
                                return;
                            case STOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        0 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 + 64 * (state.arr[count]),
                                        62, null);
                                return;
                            case SQUAT:
                                g.drawImage(imgSquat,
                                        left, top,
                                        right, bottom,
                                        0,
                                        0,
                                        62,
                                        53, null);
                                return;
                            case DEAD:
                                switch (state.arr[count]) {
                                    case 0:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                0,
                                                0,
                                                60,
                                                47, null);
                                        break;
                                    case 1:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                60,
                                                0,
                                                130,
                                                35, null);
                                        break;
                                    case 2:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                130,
                                                0,
                                                205,
                                                35, null);
                                        break;
                                }
                                return;
                        }
                    case LEFT:
                        switch (state) {
                            case WALK:
                                if (state.arr[count] % 3 != 2) {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            1 + 68 + 68 * state.arr[count] - 11 * (state.arr[count] / 3),
                                            0,
                                            1 + 68 * state.arr[count] - 11 * (state.arr[count] / 3),
                                            52, null);
                                } else {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            1 + 68 * (state.arr[count]) - 68 * (state.arr[count] / 3) + 57 * ((state.arr[count] + 1) / 3),
                                            0,
                                            1 + 68 * (state.arr[count]) - 68 * (state.arr[count] / 3) + 57 * (state.arr[count] / 3),
                                            52, null);
                                }
                                return;
                            case JUMP:
                                switch (state.arr[count]) {
                                    case 0:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                47,
                                                0,
                                                1,
                                                75, null);
                                        break;
                                    case 1:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                102,
                                                0,
                                                47,
                                                75, null);
                                        break;
                                    case 2:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                162,
                                                0,
                                                102,
                                                75, null);
                                        break;
                                }
                                return;
                            case STOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        0 + 64 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 * (state.arr[count]),
                                        62, null);
                                return;
                            case SQUAT:
                                g.drawImage(imgSquat,
                                        left, top,
                                        right, bottom,
                                        0,
                                        0,
                                        62,
                                        53, null);
                                return;
                            case DEAD:
                                switch (state.arr[count]) {
                                    case 0:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                60,
                                                0,
                                                0,
                                                47, null);
                                        break;
                                    case 1:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                130,
                                                0,
                                                60,
                                                35, null);
                                        break;
                                    case 2:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                205,
                                                0,
                                                130,
                                                35, null);
                                        break;
                                }
                                return;
                        }
                }
            case SASUKEHD:
                switch (dir) {
                    case RIGHT:
                        switch (state) {
                            case WALK:
                                if (state.arr[count] % 3 != 2) {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            1 + 65 * state.arr[count] - 5 * (state.arr[count] / 3),
                                            0,
                                            1 + 65 + 65 * state.arr[count] - 5 * (state.arr[count] / 3),
                                            54, null);
                                } else {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            1 + 65 * (state.arr[count]) - 65 * (state.arr[count] / 3) + 60 * (state.arr[count] / 3),
                                            0,
                                            1 + 65 * (state.arr[count]) - 65 * (state.arr[count] / 3) + 60 * ((state.arr[count] + 1) / 3),
                                            54, null);
                                }
                                return;
                            case JUMP:
                                g.drawImage(imgJump,
                                        left, top,
                                        right, bottom,
                                        1 + 56 * (state.arr[count]),
                                        0,
                                        1 + 56 + 56 * (state.arr[count]),
                                        71, null);
                                return;
                            case STOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        1 + 51 * (state.arr[count]),
                                        0,
                                        1 + 51 + 51 * (state.arr[count]),
                                        66, null);
                                return;
                            case SQUAT:
                                g.drawImage(imgSquat,
                                        left, top,
                                        right, bottom,
                                        0,
                                        0,
                                        42,
                                        44, null);
                                return;
                            case DEAD:
                                g.drawImage(imgDead,
                                        left, top,
                                        right, bottom,
                                        0 + 76 * (state.arr[count]),
                                        0,
                                        0 + 76 + 76 * (state.arr[count]),
                                        47, null);
                                return;
                        }
                    case LEFT:
                        switch (state) {
                            case WALK:
                                if (state.arr[count] % 3 != 2) {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            1 + 65 + 65 * state.arr[count] - 5 * (state.arr[count] / 3),
                                            0,
                                            1 + 65 * state.arr[count] - 5 * (state.arr[count] / 3),
                                            54, null);
                                } else {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            1 + 65 * (state.arr[count]) - 65 * (state.arr[count] / 3) + 60 * ((state.arr[count] + 1) / 3),
                                            0,
                                            1 + 65 * (state.arr[count]) - 65 * (state.arr[count] / 3) + 60 * (state.arr[count] / 3),
                                            54, null);
                                }
                                return;
                            case JUMP:
                                g.drawImage(imgJump,
                                        left, top,
                                        right, bottom,
                                        1 + 56 + 56 * (state.arr[count]),
                                        0,
                                        1 + 56 * (state.arr[count]),
                                        71, null);
                                return;
                            case STOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        1 + 51 + 51 * (state.arr[count]),
                                        0,
                                        1 + 51 * (state.arr[count]),
                                        66, null);
                                return;
                            case SQUAT:
                                g.drawImage(imgSquat,
                                        left, top,
                                        right, bottom,
                                        42,
                                        0,
                                        0,
                                        44, null);
                                return;
                            case DEAD:
                                g.drawImage(imgDead,
                                        left, top,
                                        right, bottom,
                                        0 + 76 + 76 * (state.arr[count]),
                                        0,
                                        0 + 76 * (state.arr[count]),
                                        47, null);
                                return;
                        }
                }
            case SAKURAHD:
                switch (dir) {
                    case RIGHT:
//                        switch (state) {
//                        }
                    case LEFT:
                        switch (state) {
                            case SAKURASTOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        0 + 43 + 43 * (state.arr[count]),
                                        0,
                                        0 + 43 * (state.arr[count]),
                                        66, null);
                                return;
                        }
                }
        }
    }

    public void update() {
        if (delay.count()) {
            count = ++count % state.arr.length;
        }

    }

}
