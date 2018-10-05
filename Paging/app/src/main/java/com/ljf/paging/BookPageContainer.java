package com.ljf.paging;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mr.lin on 2018/10/3.
 * 包裹BookPage的ViewGroup
 */

public class BookPageContainer extends ViewGroup {//继承ViewGroup,代码布局比xml布局效率更高

    private ViewGroup topToolVg;
    private BookPageView bookPager;
    private ViewGroup bottomToolVg;
    private TextView pageNumTv;

    private int width, height;

    //工具栏显示状态
    private boolean isToolsVisiable;

    private ValueAnimator mAnimator;

    private float offset;

    public void setOffset(float offset) {
        this.offset = offset;
        requestLayout();
    }

    public BookPageContainer(Context context) {
        this(context, null);
    }

    public BookPageContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookPageContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_bookpage_container, this, true);
    }

    //onFinishInflate先于onMeasure
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bookPager = findViewById(R.id.pageView);
        pageNumTv = findViewById(R.id.pageNumTv);
        topToolVg = findViewById(R.id.topToolVg);
        bottomToolVg = findViewById(R.id.bottomToolVg);
        bookPager.setOnCenterClickListener(new BookPageView.OnCenterClickListener() {
            @Override
            public void onCenterClick(BookPageView bookPageView) {
                executeAnimation();
            }
        });
        bookPager.setOnPagingListener(new BookPageView.OnPagingListener() {
            @Override
            public void noMore(boolean next) {
                Toast.makeText(getContext(), "没有内容了！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageChange(int pageNum) {

                pageNumTv.setText(String.format("第%d页", pageNum));
            }

            @Override
            public void onContentErro(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        bookPager.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        pageNumTv.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
        topToolVg.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));//注意AT_MOST与xml间底对应
        bottomToolVg.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("ljf", offset + "");

        int topToolVgHeight = topToolVg.getMeasuredHeight();
        int bottomToolVgHeight = bottomToolVg.getMeasuredHeight();

        bookPager.layout(0, 0, width, height);

        int topToolVgY = (int) (topToolVgHeight * (offset - 1));
        int bottomToolVgY = height - (int) (bottomToolVgHeight * offset);

        topToolVg.layout(0, topToolVgY, width, topToolVgY + topToolVgHeight);//注意bottom的计算，不要只关注top变化，否则控件会被拉伸
        bottomToolVg.layout(0, bottomToolVgY, width, bottomToolVgY + bottomToolVgHeight);

        pageNumTv.layout(0, height - pageNumTv.getMeasuredHeight(), pageNumTv.getMeasuredWidth(), height);
    }

    //显示or隐藏工具栏
    private void executeAnimation() {
        float start;
        float end;
        if (isToolsVisiable) {
            start = 1;
            end = 0;
        } else {
            start = 0;
            end = 1;
        }
        mAnimator = ObjectAnimator.ofFloat(this, "offset", start, end);
        mAnimator.setDuration(400);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isToolsVisiable = !isToolsVisiable;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

}
