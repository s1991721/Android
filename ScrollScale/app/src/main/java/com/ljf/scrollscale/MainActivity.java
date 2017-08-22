package com.ljf.scrollscale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.ljf.scrollscale.fragment.RainbowFragment;
import com.ljf.scrollscale.views.ScaleScrollView;
import com.ljf.scrollscale.views.TabView;
import com.ljf.scrollscale.views.ViewPagerInScroll;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPagerInScroll viewPager;
    private TabView tabView;
    private List<Fragment> fragmentList;
    private ScaleScrollView scrollView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        regListener();
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new RainbowFragment());
        fragmentList.add(new RainbowFragment());
    }

    private void initView() {
        scrollView = (ScaleScrollView) findViewById(R.id.scrollView);
        imageView = (ImageView) findViewById(R.id.firstView);
        viewPager = (ViewPagerInScroll) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        tabView = (TabView) findViewById(R.id.tabView);
        tabView.select(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabView.select(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void regListener() {
        scrollView.setOnScrollListener(new ScaleScrollView.OnScrollListener() {
            @Override
            public void onScroll(int y) {
                Log.i("ljf-scrollY", "onScroll: " + y);
                if (y > imageView.getHeight()) {
                    tabView.setY(y);
                } else {
                    tabView.setY(imageView.getHeight());
                }
            }
        });
    }

}
