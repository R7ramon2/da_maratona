package com.dev.da.maratona;

/*
 * Created by Tiago Emerenciano on 10/10/2017.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class SwipeInterceptor extends FrameLayout {

    interface OnSwipeListener {
        void onSwipeLeft();

        void onSwipeRight();
    }

    private OnSwipeListener mSwipeListener;
    private int mTouchSlop;
    private boolean mIsDragged;
    private float mLastX;
    private float mLastY;
    private float mStartX;
    private float mDeltaXTotal = 0;

    public SwipeInterceptor(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTouchSlop(context);
    }

    //Api level 21 ou superior
    public SwipeInterceptor(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTouchSlop(context);
    }

    public SwipeInterceptor(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTouchSlop(context);
    }

    public SwipeInterceptor(Context context) {
        super(context);
        setTouchSlop(context);
    }

    public void setOnSwipeListenner(OnSwipeListener listener) {
        mSwipeListener = listener;
    }

    private void setTouchSlop(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mStartX = mLastX;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float xDelta = Math.abs(x - mLastX);
                float yDelta = Math.abs(y - mLastY);

                float xDeltaTotal = x - mStartX;
                if (xDelta > yDelta && Math.abs(xDeltaTotal) > mTouchSlop) {
                    mIsDragged = true;
                    mStartX = x;
                    return true;
                }
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsDragged && mSwipeListener != null) {
                    if (mDeltaXTotal > 0)
                        mSwipeListener.onSwipeRight();
                    else mSwipeListener.onSwipeLeft();
                }
                mIsDragged = false;
                mDeltaXTotal = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();

                float xDelta = Math.abs(x - mLastX);
                float yDelta = Math.abs(y - mLastY);

                mDeltaXTotal = x - mStartX;
                if (!mIsDragged && xDelta > yDelta && Math.abs(mDeltaXTotal) > mTouchSlop) {
                    mIsDragged = true;
                    mStartX = x;
                    mDeltaXTotal = 0;
                }
                mLastX = x;
                mLastY = y;
                break;
        }

        return true;
    }
}