package entity;

import frame.TankFrame;
import util.Dir;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {

    int x, y;
    Dir dir;
    static final int SPEED = 50;
    TankFrame tankFrame;
    boolean moving = false;

    public Tank(int x, int y, Dir dir,TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tankFrame = tankFrame;
    }

    public void paint(Graphics g) {

        g.fillRect(x, y, 50, 50);
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

        tankFrame.bullets.add(new Bullet(this.x,this.y,this.dir,tankFrame));

    }
}
