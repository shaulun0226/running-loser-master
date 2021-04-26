
package game.utils;

public class Path {

    public static abstract class Flow {

        private String path;

        public Flow(String path) {
            this.path = path;
        }

        public Flow(Flow flow, String path) {
            this.path = flow.path + path;
        }

        @Override
        public String toString() {
            return path;
        }
    }

    private static class Resources extends Flow {

        private Resources() {
            super("/resources");
        }
    }

    public static class Imgs extends Flow {

        private Imgs() {
            super(new Resources(), "/imgs");
        }

        public static class Actors extends Flow {

            private Actors(Flow flow) {
                super(flow, "/actors");
            }
            public String mario(){
                return this+"/mario.png";
            }
            public String luigi(){
                return this+"/luigi.png";
            }
            public String naruto(){
                return this+"/naruto.png";
            }
            public String narutoHD(){
                return this+"/narutohd.png";
            }
            public String narutoHDRun(){
                return this+"/naruto-hd-run.png";
            }
            public String narutoHDJump(){
                return this+"/naruto-hd-jump.png";
            }
            public String narutoHDStop(){
                return this+"/naruto-hd-stop.png";
            }
            public String narutoHDSquat(){
                return this+"/naruto-hd-squat.png";
            }
            public String narutoHDDead(){
                return this+"/naruto-hd-dead.png";
            }
            public String narutoHDSmall(){
                return this+"/naruto-hd-small.png";
            }
            public String rasengan(){
                return this+"/rasengan.png";
            }
            
            public String sasuke(){
                return this+"/sasuke.png";
            }
            
            public String sasukeHD(){
                return this+"/sasukehd.png";
            }
            public String sasukeHDRun(){
                return this+"/sasuke-hd-run.png";
            }
            public String sasukeHDJump(){
                return this+"/sasuke-hd-jump.png";
            }
            public String sasukeHDStop(){
                return this+"/sasuke-hd-stop.png";
            }
            public String sasukeHDSquat(){
                return this+"/sasuke-hd-squat.png";
            }
            public String sasukeHDDead(){
                return this+"/sasuke-hd-dead.png";
            }
            public String sasukeHDSmall(){
                return this+"/sasuke-hd-small.png";
            }
            public String sakuraHDStop(){
                return this+"/sakura-hd-stop.png";
            }
            public String fireball(){
                return this+"/fireball.png";
            }
            public String marioDead(){
                return this+"/mariodead.png";
            }
            public String luigiDead(){
                return this+"/luigidead.png";
            }
            public String heart(){
                return this+"/heart.png";
            }

        }
        public static class Monsters extends Flow {

            private Monsters(Flow flow) {
                super(flow, "/monsters");
            }

            public String ghost() {
                return this + "/ghost.png";
            }
            public String piranha(){
                return this + "/piranha.png";
            }
            public String bullet(){
                return this + "/bullet.png";
            }
            public String fortseat1(){
                return this + "/fortseat1.png";
            }
            public String pig() {
                return this + "/pigNpc.png";
            }
            public String kapa() {
                return this + "/kapaNpc.png";
            }
            public String taco() {
                return this + "/tacoNpc.png";
            }
            public String green() {
                return this + "/greenNpc.png";
            }
            public String fire() {
                return this + "/fire.png";
            }
            public String thorn_UP(){
                return this + "/thorn_UP.png";
            }
            public String thorn_DOWN() {
                return this + "/thorn_DOWN.png";
            }
            public String thorn_RIGHT() {
                return this + "/thorn_RIGHT.png";
            }
            public String thorn_LEFT() {
                return this + "/thorn_LEFT.png";
            }
            public String firePillar() {
                return this + "/firePillar.png";
            }
            public String boom2() {
                return this + "/boom2.png";
            }
        }
        public static class Objs extends Flow {

            private Objs(Flow flow) {
                super(flow, "/objs");
            }

            public String finish(){
                return this+"/finish.png";
            }
            public String block1() {
                return this + "/block1.gif";
            }
            public String block2(){
                return this + "/block2.gif";
            }
            public String blockBoss() {
                return this + "/blockBoss.gif";
            }
            public String brick(){
                return this + "/brick.png";
            }
            public String brick0() {
                return this + "/brick0.gif";
            }
            public String brick1(){
                return this + "/brick1.gif";
            }
            public String brick2() {
                return this + "/brick2.png";
            }
            public String brickAsk(){
                return this + "/brickAsk.png";
            }
            public String coin() {
                return this + "/coin.png";
            }
            public String coinCount(){
                return this + "/coincount.png";
            }
            public String ending(){
                return this + "/ending.png";
            }
            public String fireDown() {
                return this + "/fireDown.gif";
            }
            public String fireUp(){
                return this + "/fireUp.gif";
            }
            public String fort1() {
                return this + "/fort1.png";
            }
            public String fort2() {
                return this + "/fort2.png";
            }
            public String moguL(){
                return this + "/moguL.png";
            }
            public String moguM() {
                return this + "/moguM.png";
            }
            public String moguR(){
                return this + "/moguR.png";
            }
            public String moguL1() {
                return this + "/moguL1.png";
            }
            public String moguM1(){
                return this + "/moguM1.png";
            }
            public String moguR1() {
                return this + "/moguR1.png";
            }
            public String moguL2(){
                return this + "/moguL2.png";
            }
            public String moguM2() {
                return this + "/moguM2.png";
            }
            public String moguR2(){
                return this + "/moguR2.png";
            }
            public String pipeBody() {
                return this + "/pipeBody.png";
            }
            public String pipeDown(){
                return this + "/pipeDown.png";
            }
            public String pipeUp() {
                return this + "/pipeUp.png";
            }
            public String spring(){
                return this + "/spring.png";
            }
            public String stair() {
                return this + "/stair.png";
            }
            public String rebornPoint1(){
                return this+"/repoint1.png";
            }
            public String rebornPoint2(){
                return this+"/repoint2.png";
            }
            public String arrow_LEFT() {
                return this + "/arrow_LEFT.png";
            }
            public String arrow_RIGHT() {
                return this + "/arrow_RIGHT.png";
            }
            public String fireBall(){
                return this+"/fireBall.png";
            }
            public String title() {
                return this + "/title.png";
            }
            public String win(){
                return this+"/win.png";
            }
        }

        public static class Backgrounds extends Flow {

            private Backgrounds(Flow flow) {
                super(flow, "/backgrounds");
            }
            public String background1() {
                return this + "/background1.png";
            }
            public String background2() {
                return this + "/background2.png";
            }
            public String controlP1() { // 1p操作圖
                return this + "/1p.png"; 
            }
            public String controlP2() { // 2p操作圖
                return this + "/2p.png";
            }
            public String tool() {
                return this + "/tool.png";
            }
            public String story() {
                return this + "/story.png";
            }
            public String backgroundMiddle(){
                return this+"/background-middle-large.png";
            }
            public String backgroundBottom(){
                return this+"/background-bottom-large.png";
            }
            public String backgroundMiddleW3390H960(){
                return this+"/background-middle-W3390H960.png";
            }
            public String backgroundBottomW2560H960(){
                return this+"/background-bottom-W2560H960.png";
            }
            public String pointCount(){
                return this+"/pointCount.png";
            }
            public String pointCountOrange(){
                return this+"/pointCount-orange.png";
            }
        }
        public static class Effects extends Flow {

            private Effects(Flow flow) {
                super(flow, "/effects");
            }
            public String invincible(){
                return this+"/invincible.png";
            }
            public String speedUp(){
                return this+"/speedUp.png";
            }
            public String speedDown(){
                return this+"/speedDown.png";
            }
            public String chaos(){
                return this+"/chaos.png";
            }
            public String ink(){
                return this+"/inkW1300H460.png";
            }
        }

        public Actors actors() {
            return new Actors(this);
        }

        public Objs objs() {
            return new Objs(this);
        }
        public Monsters monsters() {
            return new Monsters(this);
        }
        public Backgrounds backgrounds() {
            return new Backgrounds(this);
        }
        public Effects effects() {
            return new Effects(this);
        }
    }

    public static class Sounds extends Flow {

        private Sounds() {
            super(new Resources(), "/sounds");
        }
        public String background(){
                return this+"/background.wav";
            }
        public String hanamatsuri(){
            return this+"/hanamatsuri.wav";
        }
        public String rasengan(){
                return this+"/rasengan.wav";
            }
        public String fireball(){
                return this+"/fireball.wav";
            }
        public String menu(){
            return this+"/menu.wav";
        }
        public String button() {
            return this + "/button.wav";
        }
        public String coin() {
            return this + "/coin.wav";
        }
        public String spring() {
            return this + "/spring.wav";
        }
        public String boom() {
            return this + "/boom.wav";
        }
        public String magicBrick() {
            return this + "/magicBrick.wav";
        }
        public String breakBrick() {
            return this + "/breakBrick.wav";
        }
        public String dead() {
            return this + "/dead.wav";
        }
        public String pop() {
            return this + "/pop.wav";
        }
    }

    public Imgs img() {
        return new Imgs();
    }

    public Sounds sound() {
        return new Sounds();
    }
}
