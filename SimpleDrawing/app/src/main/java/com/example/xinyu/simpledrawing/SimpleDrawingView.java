package com.example.xinyu.simpledrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by xinyu on 2/25/17.
 */

public class SimpleDrawingView extends View implements View.OnTouchListener {
    //0 draw, 1 delete, 2 move
    public int mode;
    public int color = Color.BLACK;
    static Paint thick;
    static {
        thick = new Paint();
        thick.setColor(Color.BLACK);
        thick.setStyle(Paint.Style.STROKE);
    }
    private float startX;
    private float startY;
    private float currentX;
    private float currentY;
    private float tempX;
    private float tempY;
    private int tempIndex;
    private float speedX;
    private float speedY;
    private boolean keepMoving;
    private long tempTime;
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Float> radiusList = new ArrayList<>();
    private ArrayList<Integer> colorList = new ArrayList<>();
    private VelocityTracker mVelocityTracker = null;
    public SimpleDrawingView(Context context, AttributeSet xmlAttributes) {
        super(context, xmlAttributes);
        setOnTouchListener(this);
        mode = 0;
    }
    public boolean onTouch(View arg0, MotionEvent event) {
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        switch (actionCode) {
            case MotionEvent.ACTION_DOWN:
                return handleActionDown(event);
            case MotionEvent.ACTION_MOVE:
                return handleActionMove(event);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return handleActionUp(event);
        }
        return false;
    }
    private boolean handleActionDown(MotionEvent event) {
        startX = event.getX();
        startY = event.getY();
        switch (mode) {
            case 0:
                float x = currentX - startX;
                float y = currentY - startY;
                float r = 0;
                colorList.add(color);
                radiusList.add(r);
                Point p = new Point();
                p.x = (int)startX;
                p.y = (int)startY;
                points.add(p);
                tempIndex = points.indexOf(p);
                break;
            case 2:
                mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(event);
                ListIterator<Point> listIterator = points.listIterator();
                while(listIterator.hasNext()) {
                    Point point = listIterator.next();
                    float x2 = startX - point.x;
                    float y2 = startY - point.y;
                    int index = points.indexOf(point);
                    float r2 = (float) Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
                    if (r2 <= radiusList.get(index)) {
                        tempIndex = index;
                    }
                }
                break;
        }
        return true;
    }
    private boolean handleActionUp(MotionEvent event) {
        currentX = event.getX();
        currentY = event.getY();
        switch (mode) {
            case 1:
                ListIterator<Point> listIterator = points.listIterator();
                while(listIterator.hasNext()) {
                    Point point = listIterator.next();
                    float x1 = currentX - point.x;
                    float y1 = currentY - point.y;
                    int index = points.indexOf(point);
                    float r1 = (float)Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
                    if (r1 <= radiusList.get(index)) {
                        listIterator.remove();
                        radiusList.remove(index);
                        colorList.remove(index);
                    }
                    invalidate();
                }
                break;
            case 2:
                keepMoving = true;
                break;
        }
        return true;
    }
    private boolean handleActionMove(MotionEvent event) {
        tempX = event.getX();
        tempY = event.getY();
        switch (mode) {
            case 0:
                float x = tempX - startX;
                float y = tempY - startY;
                float r = (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                radiusList.set(tempIndex, r);
                break;
            case 2:
                points.get(tempIndex).x = (int)tempX;
                points.get(tempIndex).y = (int)tempY;
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1);
                speedX = mVelocityTracker.getXVelocity();
                speedY = mVelocityTracker.getYVelocity();
                tempTime = System.currentTimeMillis();
                invalidate();
                break;
        }
        return true;
    }
    protected void onDraw(Canvas canvas) {
        switch(mode) {
            case 2:
                if(tempIndex < points.size()) {
                    long time = System.currentTimeMillis() - tempTime;
                    Point point = points.get(tempIndex);
                    point.x += (int) (time * speedX);
                    point.y += (int) (time * speedY);
                    tempTime = System.currentTimeMillis();
                    changeOnCollision(point, time);
                }
        }
        int i = 0;
        for(Point point:points){
            thick.setColor(colorList.get(i));
            canvas.drawCircle(point.x, point.y, radiusList.get(i), thick);
            i++;
        }
        invalidate();
    }
    private void changeOnCollision(Point point, long time) {
        if (xIsOutOfBounds(point)) {
            speedX = (float)(-speedX * 0.8);
            point.x += (int)(time * speedX);
        }

        if (yIsOutOfBounds(point)) {
            speedY = (float)(-speedY * 0.8);
            point.y += (int)(time * speedY);
        }
    }
    private boolean xIsOutOfBounds(Point point) {
        float x = point.x;
        if (x - radiusList.get(tempIndex) < 0) return true;
        if (x + radiusList.get(tempIndex) > this.getWidth()) return true;
        return false;
    }
    private boolean yIsOutOfBounds(Point point) {
        float y = point.y;
        if (y - radiusList.get(tempIndex) < 0) return true;
        if (y + radiusList.get(tempIndex) > this.getHeight()) return true;
        return false;
    }
}
