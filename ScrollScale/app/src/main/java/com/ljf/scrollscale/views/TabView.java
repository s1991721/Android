package com.ljf.scrollscale.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ljf.scrollscale.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljf on 2017/8/22.
 * 自定义吸附tab
 */

public class TabView extends LinearLayout {

    private CharSequence[] datas;
    private List<TextView> textViews;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textViews = new ArrayList<>();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        datas = typedArray.getTextArray(R.styleable.TabView_datas);
        initView();
        typedArray.recycle();
    }

    private void initView() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        for (CharSequence item : datas) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(layoutParams);
            textView.setText(item);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_select);
            addView(textView);
            textViews.add(textView);
        }
    }

    public void select(int position) {
        for (TextView textView : textViews) {
            textView.setSelected(false);
        }
        textViews.get(position).setSelected(true);
    }

}
