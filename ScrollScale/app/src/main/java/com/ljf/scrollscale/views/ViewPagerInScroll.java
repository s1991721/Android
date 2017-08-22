package com.ljf.scrollscale.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ljf on 2017/8/17.
 * 在ScrollView中的ViewPager
 */

public class ViewPagerInScroll extends ViewPager {

    private GestureDetector gestureDetector;

    public ViewPagerInScroll(Context context) {
        this(context, null);
    }

    public ViewPagerInScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;

        /**
         * 这里是取viewpager里高度最大的一个作为父控件的高度
         */
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));//这里如果是ListViewFragment，会初始化全部ListView里全部的item 非常消耗性能
//            int h = child.getMeasuredHeight();
//            if (h > height)
//                height = h;
//        }

        /**
         * 本例是取第一的高度
         */
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev)
                && gestureDetector.onTouchEvent(ev);
    }
}
