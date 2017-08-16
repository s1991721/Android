package com.example.indicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<View> imageViews;
    private ViewPager viewPager;
    private Indicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new LoopAdapter(imageViews));
        indicator = (Indicator) findViewById(R.id.indicator);
        indicator.setSize(imageViews.size() - 2);//头尾增加为了循环  但实际数量不要加
        viewPager.setCurrentItem(1);//设置当前位置
        viewPager.addOnPageChangeListener(indicator.getOnPageChangeListener());
        viewPager.addOnPageChangeListener(new LoopOnPageChangeListener(viewPager, imageViews.size()));
    }

    private void initData() {
        imageViews = new ArrayList<>();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView imageView1 = new ImageView(this);
        imageView1.setBackgroundColor(Color.RED);
        imageView1.setLayoutParams(layoutParams);

        ImageView imageView2 = new ImageView(this);
        imageView2.setBackgroundColor(Color.GREEN);
        imageView2.setLayoutParams(layoutParams);

        ImageView imageView3 = new ImageView(this);
        imageView3.setBackgroundColor(Color.BLUE);
        imageView3.setLayoutParams(layoutParams);

        ImageView imageView4 = new ImageView(this);
        imageView4.setBackgroundColor(Color.YELLOW);
        imageView4.setLayoutParams(layoutParams);

        imageViews.add(imageView4);
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);
        imageViews.add(imageView4);
        imageViews.add(imageView1);
    }

}
