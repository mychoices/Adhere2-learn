package com.jonas.yun_library.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.jonas.yun_library.listener.OnSlidingListener;

/**
 * @author yun.
 * @date 2016/12/21
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
public class SlideEffectLayout extends RelativeLayout {

    private ViewDragHelper mDragHelper;
    private View mDragView;
    private Point mAutoBackPoint;
    private int mHeight;
    private OnSlidingListener mL;

    public SlideEffectLayout(Context context) {
        super(context);
        init(context);
    }

    public SlideEffectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideEffectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
//        mDragView = child;
                return true;
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                mAutoBackPoint = new Point(mDragView.getLeft(), mDragView.getTop());
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return mDragView.getLeft();
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (top > 0) {
                    return top;
                } else {
                    return mDragView.getTop();
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (Math.abs(mDragView.getTop() - mAutoBackPoint.y) < mHeight / 3) {
                    mDragHelper.settleCapturedViewAt(mAutoBackPoint.x, mAutoBackPoint.y);
                } else {
                    mDragHelper.settleCapturedViewAt(mAutoBackPoint.x, mHeight);
                }
                postInvalidate();
            }

        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
//        mAutoBackPoint = new Point(mDragView.getLeft(), mDragView.getTop());
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            //刷新界面就好
            postInvalidate();

            if (mL != null) {
                mL.onSliding(mDragView.getTop());
                if (mDragView.getTop() == mHeight) {
                    mL.onSlidingFinish();
                }
            }
        }
    }

    public void setOnSlidingListener(OnSlidingListener l) {
        mL = l;
    }
}
