package com.lin.gesturedetector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lin.gesturedetector.R;

import java.util.ArrayList;

/**
 * Created by ljf on 2016-04-11.
 */
public class BottomFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment, null);
        listView = (ListView) view.findViewById(R.id.fragment_listview);
        list = new ArrayList();
        for (int i = 0; i < 15; i++) {
            list.add(i);
        }
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        return view;
    }

}
