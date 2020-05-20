package tank.util.frame;

import tank.util.entity.Bullet;
import tank.util.entity.Tank;
import tank.util.Dir;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankFrame extends Frame {

    Tank mainTank = new Tank(200,200,Dir.DOWN,this);

   public List<Bullet> bullets = new ArrayList<>();
    public static  final int WIDTH = 1200,HEIGHT=800;
    public TankFrame() {
        this.setLocation(360, 150);
        this.setSize(1200, 800);
        //禁止改变窗口的大小
        this.setResizable(false);
        this.setTitle("我的tank世界");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
        this.addKeyListener(new KeyListener());
    }

    @Override
    public void paint(Graphics g) {
        mainTank.paint(g);
        g.drawString("子弹的数量：" + bullets.size() , 10,60);
        bullets.removeIf(bullet -> !bullet.isLive());
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }
    }

    class KeyListener extends KeyAdapter {

        // 处理坦克斜方向移动的问题
        boolean bL = false;
        boolean bR = false;
        boolean bU = false;
        boolean bD = false;
        boolean cT = false;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    break;
                case KeyEvent.VK_UP:
                    bU = true;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    break;
                case KeyEvent.VK_CONTROL:
                    cT = true;
                    mainTank.fire();
                    break;
                default:
                    break;
            }
            setMainTankDir();

        }


        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    bL = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    break;
                case KeyEvent.VK_UP:
                    bU = false;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    break;
                case KeyEvent.VK_CONTROL:
                    cT = false;
                    break;
                default:
                    break;
            }
            setMainTankDir();
        }

        private void setMainTankDir() {
            if(!bL && !bR && !bD && !bU){
                mainTank.setMoving(false);
            }else {
                mainTank.setMoving(true);
            }
            if(bL)  mainTank.setDir(Dir.LEFT);
            if(bR) mainTank.setDir(Dir.RIGHT);
            if(bU) mainTank.setDir(Dir.UP);
            if(bD) mainTank.setDir(Dir.DOWN);

        }
    }

}
