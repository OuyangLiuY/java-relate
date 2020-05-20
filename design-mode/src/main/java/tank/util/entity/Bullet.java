package tank.util.entity;

import tank.util.frame.TankFrame;
import tank.util.image.ImageManager;
import tank.util.Dir;

import java.awt.*;

public class Bullet {
    private static final int SPEED = 10;
    int x,y;
    public static int width = ImageManager.bulletU.getWidth()
            ,height = ImageManager.bulletU.getHeight() ;
    Dir dir;
    TankFrame tf;
    boolean live = true;
    public Bullet(int x, int y, Dir dir,TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tf =tf;
    }

   public void paint(Graphics g){

       switch (dir){
           case LEFT:
              g.drawImage(ImageManager.bulletL,x,y,null);
               break;
           case RIGHT:
               g.drawImage(ImageManager.bulletR,x,y,null);
               break;
           case UP:
               g.drawImage(ImageManager.bulletU,x,y,null);
               break;
           case DOWN:
               g.drawImage(ImageManager.bulletD,x,y,null);
               break;
           default:
               break;
       }

      /* Color color = g.getColor();
       g.setColor(Color.RED);
       g.fillOval(x,y,width,height);
       g.setColor(color);*/
       move();
   }

    private void move() {
        switch (dir){
            case LEFT:
                x -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
            default:
                break;
        }
        if(x < 0 || y<0|| x > TankFrame.WIDTH || y>TankFrame.HEIGHT) live = false;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }
}
