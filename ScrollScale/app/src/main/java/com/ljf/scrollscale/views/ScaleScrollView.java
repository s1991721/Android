package com.ljf.scrollscale.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by ljf on 2017/8/17.
 * 顶部可缩放的ScrollView
 */

public class ScaleScrollView extends ScrollView {

    private View firstView;
    private int firstViewWidth;
    private int firstViewHeight;
    private boolean isLayout;
    private OnScrollListener onScrollListener;

    public ScaleScrollView(Context context) {
        this(context, null);
    }

    public ScaleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //scrollview滑动的时候会多次调用onLayout 所以在此获取宽高会变
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isLayout) {
            firstView = ((ViewGroup) getChildAt(0)).getChildAt(0);
            firstViewWidth = firstView.getMeasuredWidth();
            firstViewHeight = firstView.getMeasuredHeight();
            isLayout = true;
        }
    }

    float lastY;
    float nowY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                lastY = event.getRawY();
//                break;

            case MotionEvent.ACTION_MOVE:
                if (lastY == 0) {//为了防止首次单击  然后下拉产生多余距离 见注释56-58行
                    lastY = event.getRawY();
                    break;
                }
                nowY = event.getRawY();
                if (getScrollY() == 0 //只有在最顶部的时候下拉有效
                        && (nowY - lastY > 0) //手势要向下
                        && !animatorRunning()) { //动画停止才有效
                    ViewGroup.LayoutParams layoutParams = firstView.getLayoutParams();
                    //防止match_parent = -1 wrap_content=-2
                    if (layoutParams.width < 0 || layoutParams.height < 0) {
                        layoutParams.width = firstViewWidth;
                        layoutParams.height = firstViewHeight;
                    }
                    //下拉的最大限度
                    if (layoutParams.height > firstViewHeight * 2) {
                        break;
                    }
                    int offset = (int) ((nowY - lastY) * 0.3);
                    layoutParams.height = layoutParams.height + offset;
                    layoutParams.width = layoutParams.width + offset;
                    firstView.setLayoutParams(layoutParams);
                }
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                recovery();
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean animatorRunning() {
        return animatorSet != null && animatorSet.isRunning();
    }

    private AnimatorSet animatorSet;

    //恢复原形
    private void recovery() {
        ViewGroup.LayoutParams layoutParams = firstView.getLayoutParams();

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(this, "height", layoutParams.height, firstViewHeight);
        ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(this, "width", layoutParams.width, firstViewWidth);

        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
        }
        animatorSet.playTogether(heightAnimator, widthAnimator);
        animatorSet.setDuration(200);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lastY = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void setHeight(float height) {
        ViewGroup.LayoutParams layoutParams = firstView.getLayoutParams();
        layoutParams.height = (int) height;
        firstView.setLayoutParams(layoutParams);
    }

    private void setWidth(float width) {
        ViewGroup.LayoutParams layoutParams = firstView.getLayoutParams();
        layoutParams.width = (int) width;
        firstView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(getScrollY());
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    //滑动距离回调
    public interface OnScrollListener {
        void onScroll(int y);
    }

}
