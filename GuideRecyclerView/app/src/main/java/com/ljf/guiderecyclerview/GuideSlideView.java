package com.ljf.guiderecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.lin on 2018/1/25.
 */

public class GuideSlideView extends RelativeLayout {

    private List<String> datas = new ArrayList<>();

    private LinearLayout linearLayout;
    private HorizontalScrollView horizontalScrollView;
    private View lineView;

    public GuideSlideView(@NonNull Context context) {
        this(context, null);
    }

    public GuideSlideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideSlideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_guideslide, this, true);
        linearLayout = view.findViewById(R.id.linearlayout);
        horizontalScrollView = view.findViewById(R.id.horizontalscrollview);
        lineView = view.findViewById(R.id.lineView);

        horizontalScrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (whichOne != 0) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = event.getX();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            setScrollViewPager(event.getX() - lastX);
                            lastX = event.getX();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            lastX = 0;
                            break;
                    }

                    return true;
                }

                return false;
            }
        });
    }

    private float lastX;

    public void setDatas(List<String> strings) {
        datas.clear();
        datas.addAll(strings);
        notifiDataSetChange();
    }

    private void notifiDataSetChange() {
        linearLayout.removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.viewholder_guide, linearLayout, false));
            viewHolder.textView.setText(datas.get(i));
            linearLayout.addView(viewHolder.itemView);
        }
    }

    class ViewHolder {

        TextView textView;
        View lineView;
        View itemView;

        ViewHolder(View itemView) {
            this.itemView = itemView;
            textView = itemView.findViewById(R.id.nameTv);
            lineView = itemView.findViewById(R.id.lineView);
        }
    }

    private ViewPager viewPager;

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(getOnPageChangeListener());
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

    //ViewPager 滑动联动GuideSlideView
    private int lineViewMarginLeft;
    private int currentPosition;

    private int lastx;
    private int whichOne;
    private boolean positiveDirection = true;

    private int screenWidth;

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
            positiveDirection = true;
            directionPrepare(position);
            return false;
        }
        if (position < currentPosition) {//第一次反向滑动触发
            positiveDirection = false;
            directionPrepare(position);
            return false;
        }
        if (position == 0 && offset == 0) {//滑到第一个 初始状态
            positiveDirection = true;
            directionPrepare(position);
            return false;
        }

        lineView.setVisibility(VISIBLE);
        ViewHolder viewHolder;
        if (positiveDirection) {
            viewHolder = new ViewHolder(linearLayout.getChildAt(currentPosition));
        } else {
            viewHolder = new ViewHolder(linearLayout.getChildAt(currentPosition));
            viewHolder.lineView.setVisibility(INVISIBLE);
            viewHolder = new ViewHolder(linearLayout.getChildAt(currentPosition + 1));
        }
        viewHolder.lineView.setVisibility(INVISIBLE);

        if (offset == 0) {
            viewHolder = new ViewHolder(linearLayout.getChildAt(currentPosition));
            viewHolder.lineView.setVisibility(VISIBLE);
            lineView.setVisibility(INVISIBLE);
        }
        return true;
    }

    private void directionPrepare(int position) {
        resetState(position);
        if (whichOne != 1) {
            if (positiveDirection) {
                layoutLineView(position);
            } else {
                layoutLineView(position + 1);
            }
        }

        ViewHolder viewHolder;
        if (positiveDirection) {
            viewHolder = new ViewHolder(linearLayout.getChildAt(currentPosition));
        } else {
            viewHolder = new ViewHolder(linearLayout.getChildAt(currentPosition + 1));
        }
        viewHolder.lineView.setVisibility(VISIBLE);
        lineView.setVisibility(INVISIBLE);
    }

    private void resetState(int position) {//重置状态
        currentPosition = position;
        whichOne = 0;
        lastx = 0;
    }

    private void layoutLineView(int currentPosition) {//将游标布局到指定位置
        View item = linearLayout.getChildAt(currentPosition);
        lineViewMarginLeft = item.getLeft() - horizontalScrollView.getScrollX() + (item.getWidth() - lineView.getWidth()) / 2;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lineView.getLayoutParams();
        params.leftMargin = lineViewMarginLeft;
        lineView.setLayoutParams(params);

    }

    private int calculateMoveLength(int position, float offset) {
        int temp = position;
        if (temp >= linearLayout.getChildCount()) {
            temp = linearLayout.getChildCount() - 1;
        }
        View currentView = linearLayout.getChildAt(temp);//当前所指view

        int lastPosition = position + 1;
        if (lastPosition >= linearLayout.getChildCount()) {
            lastPosition = linearLayout.getChildCount() - 1;
        }
        View nextView = linearLayout.getChildAt(lastPosition);//将要滑动到的view

        Log.i("viewnull", temp + "   currentView-->" + currentView + "----------" + lastPosition + "     nextView-->" + nextView);

        int width = (currentView.getWidth() + nextView.getWidth()) / 2;//计算两view之间的总的滑动距离
        return Math.round(width * offset);//此次滑动的距离
    }

    private void move(int x) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lineView.getLayoutParams();
        if (whichOne == 0) {//判断   一次完整的滑动   是否结束      一次滑动结束后 whichOne=0
            if (positiveDirection) {
                if (params.leftMargin % screenWidth > screenWidth / 2 && horizontalScrollView.canScrollHorizontally(1)) {//游标在右半边且recyclerView可以右滑 则滑动recyclerView
                    whichOne = whichOne | 1;
                } else {
                    whichOne = whichOne | 2;
                }
            } else {
                if (params.leftMargin % screenWidth < screenWidth / 2 && horizontalScrollView.canScrollHorizontally(-1)) {//游标在左半边且recyclerView可以左滑 则滑动recyclerView
                    whichOne = whichOne | 1;
                } else {
                    whichOne = whichOne | 2;
                }
            }
        }

        if (whichOne == 1) {
            horizontalScrollView.scrollBy(x - lastx, 0);//因为scrollBy的滑动是累加的，所以必须减去上次滑动的距离，实际移动的距离为差值  exp:第一次滑动1 第二次滑动2，第二次滑动3，scrollBy会滑动6，但实际我只想滑动3
            lastx = x;
        } else {
            params.leftMargin = lineViewMarginLeft + x;
            lineView.setLayoutParams(params);
        }
    }

    //GuideSlideView 滑动联动ViewPager
    private void setScrollViewPager(float length) {
        viewPager.scrollBy(-Math.round(length), 0);
    }

}
