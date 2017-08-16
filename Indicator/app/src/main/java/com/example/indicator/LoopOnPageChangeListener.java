package com.example.indicator;

import android.support.v4.view.ViewPager;

/**
 * Created by ljf on 2017/8/15.
 * viewpager 无限循环且不卡顿监听
 * viewpager添加此方法后 将无限滑动
 */

public class LoopOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    //all of adapter items even the duplicates
    private int size;

    private int currentPosition = 1;
    private int targetPosition = 1;

    public LoopOnPageChangeListener(ViewPager viewPager, int size) {
        this.viewPager = viewPager;
        this.size = size;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        targetPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 不在onPageSelected 里处理
        // 是因为onPageSelected回调时界面可能还没有停止滑动
        // 因此会产生闪烁
        if (state == ViewPager.SCROLL_STATE_IDLE && targetPosition != currentPosition) {
            int index = targetPosition;
            if (targetPosition == size - 1) {
                index = 1;
            } else if (targetPosition == 0) {
                index = size - 2;
            }
            viewPager.setCurrentItem(index, false);
            currentPosition = targetPosition;
        }
    }

}