package com.example.dleyy.playwithplan.bean;

/**
 * Created by dleyy on 2017/11/29.
 */
public class Plane {

    public static final String BULLET_DOWN = "down";
    public static final String BULLET_UP = "up";

    /**
     * 血量
     */
    protected int planeHp;

    protected int planeX;

    protected int planeY;

    /**
     * 发射子弹的间隔
     */
    protected int planeBulletSpace;

    /**
     * 飞机大小
     */
    protected int planeSize;

    public int getPlaneHp() {
        return planeHp;
    }

    public void setPlaneHp(int planeHp) {
        this.planeHp = planeHp;
    }

    public int getPlaneX() {
        return planeX;
    }

    public void setPlaneX(int planeX) {
        this.planeX = planeX;
    }

    public int getPlaneY() {
        return planeY;
    }

    public void setPlaneY(int planeY) {
        this.planeY = planeY;
    }

    public int getPlaneBulletSpace() {
        return planeBulletSpace;
    }

    public void setPlaneBulletSpace(int planeBulletSpace) {
        this.planeBulletSpace = planeBulletSpace;
    }

    public int getPlaneSize() {
        return planeSize;
    }

    public void setPlaneSize(int planeSize) {
        this.planeSize = planeSize;
    }

    public void setXAndY(int pointX, int pointY) {
        setPlaneX(pointX);
        setPlaneY(pointY);
    }
}
