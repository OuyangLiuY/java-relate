package tank.util.window;

import tank.util.frame.TankFrame;

public class TankMain {
    public static void main(String[] args) throws InterruptedException {
        TankFrame tankFrame = new TankFrame();
        while (true){
            Thread.sleep(500);
            tankFrame.repaint();
        }
    }
}
