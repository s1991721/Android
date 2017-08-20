package com.lin.gesturedetector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljf on 2016-04-11.
 */
public class SecondActivity extends AppCompatActivity {

    private ViewGroup viewGroup;
    private View topView;
    private View bottomView;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        viewGroup = (ViewGroup) findViewById(R.id.view_group);
        topView = findViewById(R.id.group_top);
        bottomView = findViewById(R.id.group_bottom);

        initView();
    }

    private void initView() {
        BottomFragment bottomFragment1 = new BottomFragment();
        BottomFragment bottomFragment2 = new BottomFragment();
        fragments = new ArrayList<>();
        fragments.add(bottomFragment1);
        fragments.add(bottomFragment2);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager = (ViewPager) bottomView.findViewById(R.id.bottom_viewpager);
        viewPager.setAdapter(pagerAdapter);
    }

}
