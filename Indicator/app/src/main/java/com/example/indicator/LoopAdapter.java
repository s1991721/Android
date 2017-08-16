package com.example.indicator;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ljf on 2017/8/15.
 * viewpager 无限循环adapter
 */

public class LoopAdapter extends PagerAdapter {

    private List<View> views;

    public LoopAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // index  0 1 2 3 4 5
    // view   3 0 1 2 3 0
    // 如果debug你会发现PagerAdapter加载视图的顺序   例如首次显示view 0
    // instantiateItem 加载顺序为  view0 view3 view1
    // 手势左滑会触发  destroyItem view3  instantiateItem view2    以此类推
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {//由于无限循环采用首位添加冗余的方式
            // 例如本例一共4个view，但是列表里有6个。当滑动index5完全显示时会控制viewPager跳到index1
            // 如果此处不判断view.getParent()的话，会发生java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
            // 因为list里index5和index1的对象是同一个因此不能够重复addview();
            viewGroup.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //在instantiateItem里面处理
    }
}
