package com.ljf.guiderecyclerview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private GuideRecyclerView guideRecyclerView;
    private GuideSlideView guideSlideView;
    private GuideSlideView2 guideSlideView2;

    private FragmentAdapter fragmentAdapter;
    private RecyclerViewAdapter recyclerViewAdapter;

    private List<Fragment> fragments;
    private List<String> guides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentAdapter);

//        recyclerViewAdapter = new RecyclerViewAdapter(this, guides);
//        guideRecyclerView.setAdapter(recyclerViewAdapter);

        guideSlideView.setDatas(guides);
        guideSlideView2.setDatas(guides);
        guideSlideView2.setOnGuideSlideItemClickListener(new GuideSlideView2.OnGuideSlideItemClickListener() {
            @Override
            public void onItemClick(int position) {
                viewPager.setCurrentItem(position,false);
            }
        });

        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(8);

    }

    private void initData() {
        guides = new ArrayList<>();
        fragments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String str = "导航" + i;

            guides.add(str);

            Fragment fragment = new MineFragment();

            Bundle bundle = new Bundle();
            bundle.putString("data", str);

            fragment.setArguments(bundle);

            fragments.add(fragment);
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
//        guideRecyclerView = findViewById(R.id.guiderecyclerview);
        guideSlideView = findViewById(R.id.guideslideview);
        guideSlideView2 = findViewById(R.id.guideslideview2);

//        viewPager.addOnPageChangeListener(guideRecyclerView.getOnPageChangeListener());
        guideSlideView.setViewPager(viewPager);
        guideSlideView2.setViewPager(viewPager);
    }
}
