package com.example.dleyy.playwithplan.bean;

/**
 * Created by dleyy on 2017/11/29.
 */
public class MyPlane extends Plane {

    /**
     * 横向移动距离
     */
    private int planeOrientationLength;

    private int planeHp;

    private String planeBulletDirection;

    public static MyPlane buildPlane(){
        MyPlane myPlane = new MyPlane();
        myPlane.planeOrientationLength = 40;
        myPlane.planeHp = 3;
        myPlane.planeBulletDirection = Plane.BULLET_UP;
        myPlane.planeBulletSpace = 3;
        return myPlane;
    }

    public int getPlaneOrientationLength() {
        return planeOrientationLength;
    }

    public void setPlaneOrientationLength(int planeOrientationLength) {
        this.planeOrientationLength = planeOrientationLength;
    }

    @Override
    public int getPlaneHp() {
        return planeHp;
    }

    @Override
    public void setPlaneHp(int planeHp) {
        this.planeHp = planeHp;
    }

    public String getPlaneBulletDirection() {
        return planeBulletDirection;
    }

    public void setPlaneBulletDirection(String planeBulletDirection) {
        this.planeBulletDirection = planeBulletDirection;
    }
}
