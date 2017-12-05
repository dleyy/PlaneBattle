package com.example.dleyy.playwithplan.bean;

/**
 * Created by dleyy on 2017/11/29.
 */
public class PlaneBullet {

    /**
     * 子弹速度
     */
    private int bulletSpeed;

    private int bulletX;

    /**
     *
     */
    private int bulletY;

    /**
     * 子弹方向
     */
    private String bulletDirection;

    public static PlaneBullet buildPlanBullet() {
        PlaneBullet planeBullet = new PlaneBullet();
        planeBullet.bulletSpeed = 40;
        return planeBullet;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public int getBulletX() {
        return bulletX;
    }

    public void setBulletX(int bulletX) {
        this.bulletX = bulletX;
    }

    public int getBulletY() {
        return bulletY;
    }

    public void setBulletY(int bulletY) {
        this.bulletY = bulletY;
    }

    public String getBulletDirection() {
        return bulletDirection;
    }

    public void setBulletDirection(String bulletDirection) {
        this.bulletDirection = bulletDirection;
    }

    public void setBulletXAndY(int x, int y) {
        setBulletX(x);
        setBulletY(y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (null == obj) {
            return false;
        } else {
            PlaneBullet that = (PlaneBullet) obj;
            return that.getBulletY() - this.getBulletY() >= -40
                    && that.getBulletY() - this.getBulletY() <= 40;
        }
    }
}
