package com.ljf.guiderecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by mr.lin on 2018/1/22.
 * 滑动引导RecyclerView
 */

public class GuideRecyclerView extends FrameLayout {

    private RecyclerView recyclerView;
    private View lineView;

    private int lineViewMarginLeft;
    private int currentPosition;

    public GuideRecyclerView(Context context) {
        this(context, null);
    }

    public GuideRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_guiderecyclerview, this);
        initView(view);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        lineView = view.findViewById(R.id.lineView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);


    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("onPageScrolled", "position-->" + position + "     positionOffset-->" + positionOffset + "     positionOffsetPixels-->" + positionOffsetPixels);
                setScroll(position, positionOffset);
            }
        };
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (lineViewMarginLeft == 0) {//第一次布局游标
            layoutLineView(0);
        }
    }

    private int lastx;
    private int whichOne;
    private boolean positiveDirection = true;

    private void setScroll(int position, float offset) {

        Log.i("setScroll", "position-->" + position + "     offset-->" + offset);

        if (!whetherOrNotToMove(position, offset)) {
            return;
        }

        if (!positiveDirection) {//根据方向设置偏移
            offset = offset - 1;
        }

        int x = calculateMoveLength(position, offset);

        move(x);
    }

    private boolean whetherOrNotToMove(int position, float offset) {
        if (position > currentPosition) {//第一次正向滑动触发
            resetState(position);
            layoutLineView(currentPosition);
            positiveDirection = true;
            return false;
        }
        if (position < currentPosition) {//第一次反向滑动触发
            resetState(position);
            layoutLineView(currentPosition + 1);
            positiveDirection = false;
            return false;
        }

        if (position == 0 && offset == 0) {//滑到第一个 初始状态
            resetState(position);
            layoutLineView(0);
            positiveDirection = true;
            return false;
        }
        return true;
    }

    private void resetState(int position) {//重置状态
        currentPosition = position;
        whichOne = 0;
        lastx = 0;
    }

    private void layoutLineView(int currentPosition) {//将游标布局到指定位置
        View item = recyclerView.getLayoutManager().findViewByPosition(currentPosition);
        lineViewMarginLeft = item.getLeft() + (item.getWidth() - lineView.getWidth()) / 2;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lineView.getLayoutParams();
        params.leftMargin = lineViewMarginLeft;
        lineView.setLayoutParams(params);
    }

    private int calculateMoveLength(int position, float offset) {
//        recyclerView.getChildCount() 只有当前显示的数目
//        recyclerView.getChildAt() 只能获取当前显示的view
        int temp = position;
        if (temp >= recyclerView.getAdapter().getItemCount()) {
            temp = recyclerView.getAdapter().getItemCount() - 1;
        }
        View currentView = recyclerView.getLayoutManager().findViewByPosition(temp);//当前所指view

        int lastPosition = position + 1;
        if (lastPosition >= recyclerView.getAdapter().getItemCount()) {
            lastPosition = recyclerView.getAdapter().getItemCount() - 1;
        }
        View nextView = recyclerView.getLayoutManager().findViewByPosition(lastPosition);//将要滑动到的view

        Log.i("viewnull", temp + "   currentView-->" + currentView + "----------" + lastPosition + "     nextView-->" + nextView);

        int width = (currentView.getWidth() + nextView.getWidth()) / 2;//计算两view之间的总的滑动距离
        return Math.round(width * offset);//此次滑动的距离
    }

    private void move(int x) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lineView.getLayoutParams();
        if (whichOne == 0) {//判断   一次完整的滑动   是否结束      一次滑动结束后 whichOne=0
            if (positiveDirection) {
                if (params.leftMargin > 540 && recyclerView.canScrollHorizontally(1)) {//游标在右半边且recyclerView可以右滑 则滑动recyclerView
                    whichOne = whichOne | 1;
                } else {
                    whichOne = whichOne | 2;
                }
            } else {
                if (params.leftMargin < 540 && recyclerView.canScrollHorizontally(-1)) {//游标在左半边且recyclerView可以左滑 则滑动recyclerView
                    whichOne = whichOne | 1;
                } else {
                    whichOne = whichOne | 2;
                }
            }
        }

        if (whichOne == 1) {
            recyclerView.scrollBy(x - lastx, 0);//因为scrollBy的滑动是累加的，所以必须减去上次滑动的距离，实际移动的距离为差值  exp:第一次滑动1 第二次滑动2，第二次滑动3，scrollBy会滑动6，但实际我只想滑动3
            lastx = x;
        } else {
            params.leftMargin = lineViewMarginLeft + x;
            lineView.setLayoutParams(params);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//viewpager还在滑动的时候recyclerView不许滑动    算了直接不让滑动否则会造成空指针
        return super.dispatchTouchEvent(ev);
    }

}