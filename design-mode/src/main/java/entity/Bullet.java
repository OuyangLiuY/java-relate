package entity;

import frame.TankFrame;
import util.Dir;

import java.awt.*;

public class Bullet {
    private static final int SPEED = 10;
    int x,y;
    int width = 10 ,height = 10;
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

       Color color = g.getColor();
       g.setColor(Color.RED);
       g.fillOval(x,y,width,height);
       g.setColor(color);
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
