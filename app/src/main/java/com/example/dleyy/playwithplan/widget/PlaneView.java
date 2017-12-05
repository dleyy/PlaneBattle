package com.example.dleyy.playwithplan.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.dleyy.playwithplan.R;
import com.example.dleyy.playwithplan.bean.EnemyPlane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dleyy on 2017/11/30.
 */
public class PlaneView extends View {

    private Paint mPaint;

    private boolean needProducePlane = Boolean.TRUE;

    private int width;

    private int height;

    private List<EnemyPlane> enemyPlanes;

    public PlaneView(Context context) {
        this(context, null);
    }

    public PlaneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getScreenWidthAndHeight(context);
    }

    public PlaneView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawEnemyPlane(canvas);

        if (needProducePlane) {
            produceEnemyPlane();
        }
    }

    private void drawEnemyPlane(Canvas canvas) {
        if (getEnemyPlanes().size() > 0) {
            for (EnemyPlane enemyPlane : enemyPlanes) {
                Bitmap enemyBitmap = ((BitmapDrawable)
                        getResources().getDrawable(R.drawable.enemy)).getBitmap();
                canvas.drawBitmap(enemyBitmap, enemyPlane.getPlaneX(),
                        enemyPlane.getPlaneY(), getPaint());
            }
        }
    }

    private void produceEnemyPlane() {
        EnemyPlane enemyPlane1 = EnemyPlane.creatEnemyPlane1();
        enemyPlane1.setXAndY(getPlaneX(), 80);
        getEnemyPlanes().add(enemyPlane1);

        EnemyPlane enemyPlane2 = EnemyPlane.creatEnemyPlane2();
        enemyPlane2.setXAndY(getPlaneX(), 80);
        getEnemyPlanes().add(enemyPlane2);

        EnemyPlane enemyPlane3 = EnemyPlane.creatEnemyPlane3();
        enemyPlane3.setXAndY(getPlaneX(), 80);
        getEnemyPlanes().add(enemyPlane3);
    }

    private void getScreenWidthAndHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.
                getSystemService(Context.WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();
        height = windowManager.getDefaultDisplay().getHeight();
    }

    public Paint getPaint() {
        if (null == mPaint) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.GRAY);
        }
        return mPaint;
    }

    private List<EnemyPlane> getEnemyPlanes() {
        if (null == enemyPlanes) {
            enemyPlanes = new ArrayList<>();
        }
        return enemyPlanes;
    }

    private int getPlaneX() {
        Random random = new Random();
        int enemyX = random.nextInt(width - 40) % (width - 39);
        return enemyX;
    }
}
