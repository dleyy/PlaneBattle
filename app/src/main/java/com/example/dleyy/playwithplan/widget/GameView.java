package com.example.dleyy.playwithplan.widget;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.example.dleyy.playwithplan.R;
import com.example.dleyy.playwithplan.bean.EnemyPlane;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dleyy on 2017/11/29.
 */
public class GameView extends View {

    private static String TAG = "GameView";

    private List<EnemyPlane> enemyPlanes;
    private List<Integer> needCleanPlanes;
    private List<View> enemyViews;

    private Paint mpaint;

    private Paint scorePaint;

    private final Timer timer = new Timer();

    private TimerTask produceEnemyPlane;

    private Handler changeEnemyPlanePositionHandler = new Handler();

    private boolean producePlane = true;

    private int width;
    private int height;

    private int planeSpeed = 0;

    private int score;

    public interface onTouchListener {
        void onTouched();
    }

    public onTouchListener listener;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        getScreenWidthAndHeight(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (0 != width && null != mpaint) {
            canvas.drawText("当前分数: " + score, 80, 80, scorePaint);
        }
        canvas.translate(0, 80);
        drawEnemyPlane(canvas);
        if (producePlane) {
            produceEnemyPlane(canvas);
        }
        drawMyPlane(canvas);
    }

    private void drawEnemyPlane(Canvas canvas) {
        canvas.save();
        if (getNeedCleanPlaneList().size() > 0) {
            int num = 0;
            for (int i = 0; i < needCleanPlanes.size(); i++) {
                getEnemyPlaneList().remove(needCleanPlanes.get(i) - num);
                num++;
            }
            getNeedCleanPlaneList().clear();
        }
        if (getEnemyPlaneList().size() > 0) {
            Bitmap enemyPlane = ((BitmapDrawable) getResources().
                    getDrawable(R.drawable.enemy)).getBitmap();
            for (EnemyPlane plane : enemyPlanes) {
                canvas.drawBitmap(enemyPlane, plane.getPlaneX(),
                        plane.getPlaneY() + planeSpeed, mpaint);
            }
        }
        canvas.restore();
    }

    private void produceEnemyPlane(Canvas canvas) {
        canvas.save();
        EnemyPlane enemyPlane1 = EnemyPlane.creatEnemyPlane1();
        enemyPlane1.setXAndY(width / 8, 40);
        EnemyPlane enemyPlane2 = EnemyPlane.creatEnemyPlane2();
        enemyPlane2.setXAndY(width / 4, 40);
        EnemyPlane enemyPlane3 = EnemyPlane.creatEnemyPlane3();
        enemyPlane3.setXAndY(width / 2, 40);
        Bitmap enemyPlane = ((BitmapDrawable) getResources().
                getDrawable(R.drawable.enemy)).getBitmap();
        canvas.drawBitmap(enemyPlane, enemyPlane1.getPlaneX(),
                enemyPlane1.getPlaneY(), mpaint);
        canvas.drawBitmap(enemyPlane, enemyPlane2.getPlaneX(),
                enemyPlane2.getPlaneY(), mpaint);
        canvas.drawBitmap(enemyPlane, enemyPlane3.getPlaneX(),
                enemyPlane3.getPlaneY(), mpaint);
        canvas.restore();
        getEnemyPlaneList().add(enemyPlane1);
        getEnemyPlaneList().add(enemyPlane2);
        getEnemyPlaneList().add(enemyPlane3);
        producePlane = false;
    }

    /**
     * 绘制 我的飞机
     *
     * @param canvas
     */
    private void drawMyPlane(Canvas canvas) {
        canvas.save();
        Bitmap myPlane = ((BitmapDrawable) getResources().
                getDrawable(R.drawable.plane)).getBitmap();
        canvas.drawBitmap(myPlane, width / 2, height - 380, mpaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void initPaint() {
        if (null == mpaint) {
            mpaint = new Paint();
        }
        mpaint.setAntiAlias(true);
        mpaint.setColor(Color.GRAY);

        if (null == scorePaint) {
            scorePaint = new Paint();
        }
        scorePaint.setTextSize(50);
        scorePaint.setColor(Color.GRAY);

        initTimerTask();
    }

    /**
     * 初始化时间任务
     */
    private void initTimerTask() {
        produceEnemyPlane = new TimerTask() {
            @Override
            public void run() {
                producePlane = true;
                postInvalidate();
            }
        };

    }

    /**
     * 判断敌机是否在屏幕内
     *
     * @param plane
     * @return
     */
    private boolean isPlaneWillMoveOut(EnemyPlane plane) {
        return plane.getPlaneY() + plane.getPlaneSpeed() > height - 80;
    }

    private void getScreenWidthAndHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.
                getSystemService(Context.WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();
        height = windowManager.getDefaultDisplay().getHeight();
    }

    public onTouchListener getListener() {
        return listener;
    }

    public void setListener(onTouchListener listener) {
        this.listener = listener;
    }

    private List<Integer> getNeedCleanPlaneList() {
        if (null == needCleanPlanes) {
            needCleanPlanes = new ArrayList<>();
        }
        return needCleanPlanes;
    }

    private List<EnemyPlane> getEnemyPlaneList() {
        if (null == enemyPlanes) {
            enemyPlanes = new ArrayList<>();
        }
        return enemyPlanes;
    }

    private List<View> getEnemyViews() {
        if (null == enemyViews) {
            enemyViews = new ArrayList<>();
        }
        return enemyViews;
    }

    public void startGame() {
        timer.schedule(produceEnemyPlane, 1000, 3000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
              changeEnemyPlanePositionHandler.post(new Runnable() {
                  @Override
                  public void run() {
                      if (null == enemyPlanes) {
                          return;
                      } else {
                          for (int i = 0; i < getEnemyPlaneList().size(); i++) {
                              if (isPlaneWillMoveOut(enemyPlanes.get(i))) {
                                  getNeedCleanPlaneList().add(i);
                              }
                          }
                          for (final EnemyPlane plane : getEnemyPlaneList()) {
                              final ValueAnimator animator = ValueAnimator.ofInt(plane.getPlaneY(),
                                      plane.getPlaneY() + plane.getPlaneSpeed());
                              animator.setDuration(1000);
                              animator.addUpdateListener(new ValueAnimator.
                                      AnimatorUpdateListener() {
                                  @Override
                                  public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                      plane.setPlaneY((int) animator.getCurrentPlayTime());
                                      postInvalidate();
                                  }
                              });
                              animator.start();
                          }
                      }
                  }
              });
            }
        },500,1000);

    }


}
