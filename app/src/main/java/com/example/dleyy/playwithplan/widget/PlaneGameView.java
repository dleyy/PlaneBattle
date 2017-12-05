package com.example.dleyy.playwithplan.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.dleyy.playwithplan.R;
import com.example.dleyy.playwithplan.bean.EnemyPlane;
import com.example.dleyy.playwithplan.bean.MyPlane;
import com.example.dleyy.playwithplan.bean.PlaneBullet;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dleyy on 2017/11/30.
 */
public class PlaneGameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static String TAG = "PlaneGameView";

    private int screenWidth;
    private int screenHeight;

    private SurfaceHolder surfaceHolder;

    /**
     * 敌机
     */
    private CopyOnWriteArrayList<EnemyPlane> enemyPlanes;

    /**
     * 即将飞出屏幕的敌机
     */
    private CopyOnWriteArrayList<Integer> needCleanPlanes;

    private CopyOnWriteArrayList<PlaneBullet> planeBullets;
    private CopyOnWriteArrayList<PlaneBullet> needCleanPlaneBullets;

    /**
     * 敌机、本机、子弹 bitmap
     */
    private Bitmap enemyPlaneBitmap;
    private Bitmap myPlaneBitmap;
    private Bitmap planeBulletBitmap;

    private MyPlane myPlane;

    /**
     * 飞机坐标偏差
     */
    private static final int OFFSET = 40;

    private Canvas canvas;
    private Paint mPaint;
    private Paint scorePaint;

    /**
     * 分数
     */
    private int score;

    /**
     * 控制是否继续绘制
     */
    private boolean isRunning = false;

    public interface onGameFinished {
        void onGameFinished(int score);
    }

    private onGameFinished finishedListener;

    public PlaneGameView(Context context) {
        this(context, null);
    }

    public PlaneGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlaneGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                moveMyPlane(x);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 绘制玩家飞机
     *
     * @param canvas
     * @param mpaint
     */
    private void drawMyPlane(Canvas canvas, Paint mpaint) {
        canvas.drawBitmap(getMyPlaneBitmap(),
                myPlane.getPlaneX(), myPlane.getPlaneY(),
                mpaint);
    }

    private void drawScore(Canvas canvas) {
        canvas.drawText("当前分数: " + score, 80, 80, getScorePaint());
    }


    private void drawEnemyPlane() {
        try {
            synchronized (surfaceHolder) {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                Paint mpaint = getCanvasPaint();

                //清除即将飞出屏幕的飞机
                if (getNeedCleanPlaneList().size() > 0) {
                    int num = 0;
                    for (int i = 0; i < needCleanPlanes.size(); i++) {
                        getEnemyPlaneList().remove(needCleanPlanes.get(i) - num);
                        num++;
                    }
                    getNeedCleanPlaneList().clear();
                }

                drawScore(canvas);

                //清除上一绘制阶段即将飞出屏幕的子弹
                if (getNeedCleanPlaneBulletList().size() > 0) {
                    for (int i = 0; i < needCleanPlaneBullets.size(); i++) {
                        getPlaneBulletList().remove(needCleanPlaneBullets.get(i));
                    }
                    getNeedCleanPlaneList().clear();
                }

                if (getEnemyPlaneList().size() > 0) {
                    Bitmap enemyPlane = getEnemyPlaneBitmap();

                    for (int i = 0; i < enemyPlanes.size(); i++) {
                        EnemyPlane enemyPlan = enemyPlanes.get(i);
                        if (isPlaneWillMoveOut(enemyPlan)) {
                            if (isMyPlaneWillDestroy(enemyPlan, myPlane)) {
                                finishGame();
                            } else {
                                getNeedCleanPlaneList().add(i);
                            }
                        } else {
                            if (getPlaneBulletList().size() > 0) {          //如果子弹存在
                                for (int j = 0; j < planeBullets.size(); j++) {
                                    PlaneBullet bullet = planeBullets.get(j);
                                    if (bullet.getBulletY() -
                                            bullet.getBulletSpeed() / 20 < 10) {
                                        getNeedCleanPlaneBulletList().add(bullet);
                                    } else {
                                        bullet.setBulletY(bullet.getBulletY() -
                                                bullet.getBulletSpeed() / 20);
                                        canvas.drawBitmap(getPlaneBulletBitmap(), bullet.getBulletX()
                                                , bullet.getBulletY(), getBulletPaint());
                                    }
                                }

                                //构造子弹对象，用于判断当前敌机位置是否存在子弹。
                                PlaneBullet planePlaneBullet = new PlaneBullet();
                                planePlaneBullet.setBulletY(enemyPlan.getPlaneY());

                                if (planeBullets.contains(planePlaneBullet)) {    //并且该横线上有敌机

                                    if (isBulletShutPlane(enemyPlan.getPlaneX(),
                                            planeBullets, planePlaneBullet)) { //相撞击
                                        enemyPlan.setPlaneHp(enemyPlan.getPlaneHp() - 1);
                                        if (enemyPlan.getPlaneHp() == 0) {              //飞机血量为0
                                            score += enemyPlan.getScort();
                                            enemyPlanes.remove(i);
                                        }
                                        planeBullets.remove(planePlaneBullet);
                                    }
                                }
                                canvas.drawBitmap(enemyPlane, enemyPlan.getPlaneX(),
                                        enemyPlan.getPlaneY(), mpaint);

                            }
                            canvas.drawBitmap(enemyPlane, enemyPlan.getPlaneX(),
                                    enemyPlan.getPlaneY(), mpaint);
                        }
                    }

                }
                drawMyPlane(canvas, mpaint);

            }
        } catch (Exception e) {

        } finally {
            if (null != canvas) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
        screenWidth = surfaceFrame.width();   //屏幕宽度
        screenHeight = surfaceFrame.height(); //屏幕高度

        myPlane.setXAndY(screenWidth / 2, screenHeight - 180);

        startCanvasThread();          //启动绘图线程

        startProduceThread();         //启动生产线程

        startChangePlanePositionThread();  //启动修改位置的线程

        startLaunchBulletThread();    //启动发射子弹线程
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    /**
     * 初始化Holder并添加回调事件
     */
    public void init() {
        myPlane = MyPlane.buildPlane();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        //setZOrderOnTop(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

    }

    private void startCanvasThread() {
        isRunning = true;
        new Thread(this).start();
    }

    private void startChangePlanePositionThread() {
        TimerTask changePlanePosition = new TimerTask() {
            @Override
            public void run() {

                if (null != getEnemyPlaneList()) {

                    for (EnemyPlane enemyPlane : enemyPlanes) {
                        enemyPlane.setNowAdded(enemyPlane.getPlaneSpeed() / 300);
                        enemyPlane.setPlaneY(enemyPlane.getPlaneY()
                                + enemyPlane.getNowAdded());

                    }
                }
            }
        };

        new Timer().schedule(changePlanePosition, 0, 10);
    }

    private void startProduceThread() {
        TimerTask producePlane = new TimerTask() {
            @Override
            public void run() {
                EnemyPlane enemyPlane1 = EnemyPlane.creatEnemyPlane1();
                enemyPlane1.setXAndY(screenWidth / 3, 0);

                EnemyPlane enemyPlane2 = EnemyPlane.creatEnemyPlane2();
                enemyPlane2.setXAndY(screenWidth / 2, 0);

                EnemyPlane enemyPlane3 = EnemyPlane.creatEnemyPlane3();
                enemyPlane3.setXAndY((int) (screenWidth / 1.3), 0);
                getEnemyPlaneList().add(enemyPlane1);
                getEnemyPlaneList().add(enemyPlane2);
                getEnemyPlaneList().add(enemyPlane3);
            }
        };

        new Timer().schedule(producePlane, 500, 3000);
    }

    private void startLaunchBulletThread() {
        TimerTask launchBullet = new TimerTask() {
            @Override
            public void run() {
                PlaneBullet planeBullet = PlaneBullet.buildPlanBullet();
                planeBullet.setBulletXAndY(myPlane.getPlaneX(), myPlane.getPlaneY() - 10);
                //根据其Y轴坐标来检查是否能装击到飞机
                getPlaneBulletList().add(planeBullet);
            }
        };

        new Timer().schedule(launchBullet, 100, 1000);
    }

    @Override
    public void run() {
        while (isRunning) {
            drawEnemyPlane();
        }
        try {
            Thread.sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveMyPlane(int x) {
        if (null == myPlane) {
            return;
        } else {
            int planeWillPositionRight = myPlane.getPlaneOrientationLength() + myPlane.getPlaneX();
            int planeWillPositionLeft = myPlane.getPlaneX() - myPlane.getPlaneOrientationLength();
            if (x > myPlane.getPlaneX() && planeWillPositionRight < screenWidth - 10) {
                myPlane.setPlaneX(planeWillPositionRight);
            } else if (x < myPlane.getPlaneX() && planeWillPositionLeft > 10) {
                myPlane.setPlaneX(planeWillPositionLeft);
            }

        }
    }

    private CopyOnWriteArrayList<Integer> getNeedCleanPlaneList() {
        if (null == needCleanPlanes) {
            needCleanPlanes = new CopyOnWriteArrayList<>();
        }
        return needCleanPlanes;
    }

    private CopyOnWriteArrayList<EnemyPlane> getEnemyPlaneList() {
        if (null == enemyPlanes) {
            enemyPlanes = new CopyOnWriteArrayList<>();
        }
        return enemyPlanes;
    }

    private CopyOnWriteArrayList<PlaneBullet> getPlaneBulletList() {
        if (null == planeBullets) {
            planeBullets = new CopyOnWriteArrayList<>();
        }
        return planeBullets;
    }

    private CopyOnWriteArrayList<PlaneBullet> getNeedCleanPlaneBulletList() {
        if (null == needCleanPlaneBullets) {
            needCleanPlaneBullets = new CopyOnWriteArrayList<>();
        }
        return needCleanPlaneBullets;
    }

    private Paint getCanvasPaint() {
        if (null == mPaint) {
            mPaint = new Paint();
        }
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        return mPaint;
    }

    private Paint getBulletPaint() {
        if (null == mPaint) {
            mPaint = new Paint();
        }
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        return mPaint;
    }

    private Paint getScorePaint() {
        if (null == scorePaint) {
            scorePaint = new Paint();
        }
        scorePaint.setTextSize(50);
        scorePaint.setColor(Color.GRAY);
        return scorePaint;
    }

    public onGameFinished getFinishedListener() {
        return finishedListener;
    }

    public void setFinishedListener(onGameFinished finishedListener) {
        this.finishedListener = finishedListener;
    }

    /**
     * 子弹是否击中敌机
     *
     * @param enemyPosition 敌机X坐标
     * @param buttles       子弹list
     * @param nePlaneButtle 子包含Y坐标的子弹对象
     * @return
     */
    private boolean isBulletShutPlane(int enemyPosition, CopyOnWriteArrayList<PlaneBullet> buttles,
                                      PlaneBullet nePlaneButtle) {
        for (int i = 0; i < buttles.size(); i++) {
            if (buttles.get(i).equals(nePlaneButtle)) {
                return enemyPosition - buttles.get(i).getBulletX() > -OFFSET
                        && enemyPosition - buttles.get(i).getBulletX() < OFFSET;
            }
        }
        return false;
    }

    private Bitmap getMyPlaneBitmap() {
        if (null == myPlaneBitmap) {
            myPlaneBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.plane);
        }
        return myPlaneBitmap;
    }

    private Bitmap getEnemyPlaneBitmap() {
        if (null == enemyPlaneBitmap) {
            enemyPlaneBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.enemy);
        }
        return enemyPlaneBitmap;
    }


    private Bitmap getPlaneBulletBitmap() {
        if (null == planeBulletBitmap) {
            planeBulletBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.dot);
        }
        return planeBulletBitmap;
    }

    /**
     * 判断敌机是否在屏幕内
     *
     * @param plane
     * @return
     */
    private boolean isPlaneWillMoveOut(EnemyPlane plane) {
        return plane.getPlaneY() + plane.getNowAdded() > screenHeight - 80;
    }

    /**
     * 判断敌机是否与本机相撞击
     *
     * @param enemyPlane
     * @param myPlane
     * @return
     */
    private boolean isMyPlaneWillDestroy(EnemyPlane enemyPlane, MyPlane myPlane) {
        return enemyPlane.getPlaneX() - myPlane.getPlaneX() > -OFFSET &&
                enemyPlane.getPlaneX() - myPlane.getPlaneX() < OFFSET;
    }

    /**
     * Game Over
     */
    private void finishGame() {
        isRunning = false;

//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("游戏结束");
//        builder.setMessage("当前分数： " + score);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
    }

}
