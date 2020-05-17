package entity;

import frame.TankFrame;
import image.ImageManager;
import util.Dir;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {

    int x, y;
    Dir dir;
    static final int SPEED = 50;
    TankFrame tankFrame;
    boolean moving = false;
    public static int width = ImageManager.tankU.getWidth()
            ,height = ImageManager.tankU.getHeight() ;

    public Tank(int x, int y, Dir dir,TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tankFrame = tankFrame;
    }

    public void paint(Graphics g) {
        g.drawImage(ImageManager.tankU,x,y,null);
        switch (dir){
            case LEFT:
                g.drawImage(ImageManager.tankL,x,y,null);
                break;
            case RIGHT:
                g.drawImage(ImageManager.tankR,x,y,null);
                break;
            case UP:
                g.drawImage(ImageManager.tankU,x,y,null);
                break;
            case DOWN:
                g.drawImage(ImageManager.tankD,x,y,null);
                break;
            default:
                break;
        }
     //   g.fillRect(x, y, 50, 50);
        move();

    }

    private void move() {
        if(!moving) return;
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
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void fire() {
        int bulletX = this.x + width/2 - Bullet.width/2 ;
        int bulletY =  this.y + height/2 - Bullet.height/2;

        tankFrame.bullets.add(new Bullet(bulletX,bulletY,this.dir,tankFrame));

    }
}
