package com.ljf.guiderecyclerview;

import android.content.Context;
import android.graphics.Color;
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

import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;

/**
 * Created by mr.lin on 2018/1/25.
 */

public class GuideSlideView2 extends HorizontalScrollView {

    private List<String> datas = new ArrayList<>();

    private LinearLayout linearLayout;
    private View lineView;

    private int minMarginLeft;//光标左margin
    private int minMarginBottom = 7;//光标下margin

    private int screenWidth;//屏宽

    private float lastX;//多触点时上次的滑动坐标，用以计算滑动距离

    private ViewPager viewPager;
    private int viewPagerState;

    private int currentPosition;//当前位置

    private OnGuideSlideItemClickListener onGuideSlideItemClickListener;
    private String selectedCol = "#444444";
    private String unselectedCol = "#888888";

    private boolean isFirstSelect = true;

    public GuideSlideView2(@NonNull Context context) {
        this(context, null);
    }

    public GuideSlideView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideSlideView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
    }

    private void initView() {
        RelativeLayout rootLinearLayout = new RelativeLayout(getContext());
        addView(rootLinearLayout);

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        lineView = LayoutInflater.from(getContext()).inflate(R.layout.view_lineview, rootLinearLayout, false);

        rootLinearLayout.addView(linearLayout);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lineView.getLayoutParams();
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.bottomMargin = minMarginBottom;

        rootLinearLayout.addView(lineView);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (viewPagerState == ViewPager.SCROLL_STATE_DRAGGING) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://由于click的原因,导致ACTION_DOWN不会到达
                            lastX = event.getX();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (lastX == 0) {
                                lastX = event.getX();
                            } else {
                                viewPager.scrollBy(-Math.round(event.getX() - lastX), 0);
                                //viewpager默认缓存了当前项、前一项、后一项，所以滑动仅限于这三者之间。
                                //原作是因为缓存的较多，所以给人以可以一直滑的假象,setOffscreenPageLimit
                                lastX = event.getX();
                            }
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

    public void setDatas(List<String> strings) {
        datas.clear();
        datas.addAll(strings);
        notifyDataSetChange();
    }

    private void notifyDataSetChange() {
        linearLayout.removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.viewholder_guide2, linearLayout, false));
            viewHolder.textView.setText(datas.get(i));
            viewHolder.itemView.setTag(i);
            linearLayout.addView(viewHolder.itemView);
        }
        minMarginLeft = 0;
        isFirstSelect = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (minMarginLeft == 0) {
            View firstView = linearLayout.getChildAt(0);
            minMarginLeft = (firstView.getWidth() - lineView.getWidth()) / 2;
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(getOnPageChangeListener());
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("GuideSlideView2", "onPageScrolled    position-->" + position + "     positionOffset-->" + positionOffset + "     positionOffsetPixels-->" + positionOffsetPixels);
                setPosition(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("GuideSlideView2", "onPageSelected");
                if (isFirstSelect) {
                    setSelect(position);
                    isFirstSelect = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("GuideSlideView2", "onPageScrollStateChanged" + state);
                viewPagerState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    setSelect(viewPager.getCurrentItem());
                }

            }
        };
    }

    private void setSelect(int position) {
        if (onGuideSlideItemClickListener != null && !isFirstSelect) {
            onGuideSlideItemClickListener.onItemClick(position);
        }
        ViewHolder viewHolder = new ViewHolder(linearLayout.getChildAt(currentPosition));
        viewHolder.setChecked(false);

        viewHolder = new ViewHolder(linearLayout.getChildAt(position));
        viewHolder.setChecked(true);

        currentPosition = position;
    }

    private void setPosition(int position, float positionOffset) {
        View currentView = linearLayout.getChildAt(position);
        int targetPosition = (position + 1 > linearLayout.getChildCount() - 1) ? linearLayout.getChildCount() - 1 : position + 1;
        View targetView = linearLayout.getChildAt(targetPosition);
        int totalLength = (currentView.getWidth() + targetView.getWidth()) / 2;

        int originalX = currentView.getLeft();
        int offset = Math.round(totalLength * positionOffset);

        int marginLeft = originalX + offset;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lineView.getLayoutParams();
        params.leftMargin = minMarginLeft + marginLeft;
        lineView.setLayoutParams(params);

        setScrollX(originalX - screenWidth / 2 + offset);
    }

    public void setOnGuideSlideItemClickListener(OnGuideSlideItemClickListener onGuideSlideItemClickListener) {
        this.onGuideSlideItemClickListener = onGuideSlideItemClickListener;
    }

    class ViewHolder {

        TextView textView;
        View itemView;

        ViewHolder(View view) {
            this.itemView = view;
            textView = itemView.findViewById(R.id.nameTv);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelect((int) itemView.getTag());
                }
            });
        }

        void setChecked(boolean checked) {
            if (checked) {
                textView.setTextColor(Color.parseColor(selectedCol));
            } else {
                textView.setTextColor(Color.parseColor(unselectedCol));
            }
        }
    }

    interface OnGuideSlideItemClickListener {
        void onItemClick(int position);
    }

}