package com.ljf.scrollscale.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljf.scrollscale.R;

/**
 * Created by ljf on 2017/8/17.
 * 彩虹fragment
 */

public class RainbowFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rainbow, container, false);
        return view;
    }
}
