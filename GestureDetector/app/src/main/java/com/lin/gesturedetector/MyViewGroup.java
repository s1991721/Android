package com.lin.gesturedetector;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lin.gesturedetector.R;

/**
 * Created by ljf on 2016-04-11.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyViewGroup extends ViewGroup {

    private String tag = "ViewGroup";

    private View viewTop;
    private View viewBottom;

    private GestureDetector mGestureDetector;
    private GestureDetector.OnGestureListener onGestureListener;

    private ValueAnimator animator;

    private float layoutOffset = 1f;

    public MyViewGroup(Context context) {
        this(context, null);
    }

    public void setLayoutOffset(float layoutOffset) {
        this.layoutOffset = layoutOffset;
        requestLayout();
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i(tag, "onScroll -> distanceY" + distanceY);
                if (distanceY < 0) {// 手势向下滑动是负值
                    animatorLayoutOffset(1);
                }

                if (distanceY > 0) {
                    animatorLayoutOffset(0f);
                }
                return true;
            }
        };
        mGestureDetector = new GestureDetector(getContext(), onGestureListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viewTop = findViewById(R.id.group_top);
        viewBottom = findViewById(R.id.group_bottom);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int topHeight = viewTop.getMeasuredHeight();

        float offset = layoutOffset * topHeight;
        int width = r - l;

        float topViewYTop = offset - topHeight;
        float topViewYBottom = topViewYTop + topHeight;
        viewTop.layout(0, (int) topViewYTop, width, (int) topViewYBottom);

        viewBottom.layout(0, (int) topViewYBottom, width, (int) topViewYBottom + viewBottom.getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        viewTop.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
        viewBottom.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void animatorLayoutOffset(float offset) {
        if (animator != null && animator.isRunning()) {
            return;
        }

        animator = ObjectAnimator.ofFloat(this, "layoutOffset", layoutOffset, offset);
        animator.setDuration(500);
        animator.start();
    }

}
