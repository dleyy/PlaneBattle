package com.example.dleyy.playwithplan.bean;

/**
 * Created by dleyy on 2017/11/29.
 */
public class EnemyPlane extends Plane {

    /**
     * 飞机分数 1、3、5
     */
    private int scort;

    private int planeSpeed;

    private int nowAdded = 0;

    private String bulletDirection = BULLET_DOWN;

    public static EnemyPlane creatEnemyPlane1() {
        EnemyPlane enemyPlane = new EnemyPlane();
        enemyPlane.setPlaneHp(1);
        enemyPlane.setScort(1);
        enemyPlane.setPlaneSpeed(1200);
        enemyPlane.setPlaneBulletSpace(3);
        return enemyPlane;
    }

    public static EnemyPlane creatEnemyPlane2() {
        EnemyPlane enemyPlane = new EnemyPlane();
        enemyPlane.setPlaneHp(3);
        enemyPlane.setScort(3);
        enemyPlane.setPlaneSpeed(800);
        enemyPlane.setPlaneBulletSpace(2);
        return enemyPlane;
    }

    public static EnemyPlane creatEnemyPlane3() {
        EnemyPlane enemyPlane = new EnemyPlane();
        enemyPlane.setPlaneHp(5);
        enemyPlane.setScort(5);
        enemyPlane.setPlaneSpeed(400);
        enemyPlane.setPlaneBulletSpace(1);
        return enemyPlane;
    }

    public int getScort() {
        return scort;
    }

    public void setScort(int scort) {
        this.scort = scort;
    }

    public int getPlaneBulletSpace() {
        return planeBulletSpace;
    }

    public void setPlaneBulletSpace(int planeBulletSpace) {
        this.planeBulletSpace = planeBulletSpace;
    }

    public int getPlaneSpeed() {
        return planeSpeed;
    }

    public void setPlaneSpeed(int planeSpeed) {
        this.planeSpeed = planeSpeed;
    }

    public String getBulletDirection() {
        return bulletDirection;
    }

    public void setBulletDirection(String bulletDirection) {
        this.bulletDirection = bulletDirection;
    }

    public int getNowAdded() {
        return nowAdded;
    }

    public void setNowAdded(int nowAdded) {
        this.nowAdded = nowAdded;
    }
}
