package com.example.indicator;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljf on 2017/8/15.
 * 引导条自定义
 */

public class Indicator extends LinearLayout {

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i("ljf", "position:" + position + "---------positionoffset:" + positionOffset + "---------positionOffsetPixels:" + positionOffsetPixels);
            if (positionOffset == 0) {
                return;
            }
            if (currentPosition == position) {
                forward(position, positionOffset);
            } else {
                backup(position, positionOffset);
            }
        }

        @Override
        public void onPageSelected(int position) {
            targetPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE && targetPosition != currentPosition) {
                currentPosition = targetPosition;
            }
        }
    };

    private void forward(int position, float offset) {
        ImageView currentImageView = getImageView(position - 1);
        ImageView nextImageView = getImageView(position);
        action(currentImageView, nextImageView, offset);
    }

    private void backup(int position, float offset) {
        ImageView nextImageView = getImageView(position - 1);
        ImageView currentImageView = getImageView(position);
        action(currentImageView, nextImageView, 1 - offset);
    }

    private void action(ImageView currentImageView, ImageView nextImageView, float offset) {
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) currentImageView.getLayoutParams();
        layoutParams1.width = selectWidth - (int) (normalWidth * offset);
        layoutParams1.height = selectWidth - (int) (normalWidth * offset);
        currentImageView.setLayoutParams(layoutParams1);

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) nextImageView.getLayoutParams();
        layoutParams2.width = normalWidth + (int) (normalWidth * offset);
        layoutParams2.height = normalWidth + (int) (normalWidth * offset);
        nextImageView.setLayoutParams(layoutParams2);
    }

    private int currentPosition = 1;
    private int targetPosition = 1;
    private List<ImageView> imageViews;

    private int resource;

    int normalWidth;
    int selectWidth;

    public Indicator(Context context) {
        this(context, null);
    }

    public Indicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        init();
    }

    private void init() {
        imageViews = new ArrayList<>();
        resource = R.mipmap.ic_app_guide_white_point;
        normalWidth = dipToPx(10);
        selectWidth = normalWidth * 2;
    }

    public void setSize(int count) {
        ImageView imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(selectWidth, selectWidth);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setBackgroundResource(resource);
        addView(imageView);
        imageViews.add(imageView);
        for (int i = 1; i < count; i++) {
            ImageView imageView1 = new ImageView(getContext());
            LayoutParams layoutParams1 = new LayoutParams(normalWidth, normalWidth);
            layoutParams1.leftMargin = 20;
            imageView1.setLayoutParams(layoutParams1);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView1.setBackgroundResource(resource);
            addView(imageView1);
            imageViews.add(imageView1);
        }
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    private int dipToPx(float dip) {
        return (int) (this.getResources().getDisplayMetrics().density * dip + 0.5f);
    }

    private ImageView getImageView(int position) {
        if (position >= imageViews.size()) {
            return imageViews.get(0);
        } else if (position < 0) {
            return imageViews.get(imageViews.size() - 1);
        } else {
            return imageViews.get(position);
        }
    }
}
