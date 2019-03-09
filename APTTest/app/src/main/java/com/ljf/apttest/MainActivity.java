package com.ljf.apttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ljf.apt_annotation.BindView;
import com.ljf.apt_library.BindViewTools;

public class MainActivity extends Activity {

    @BindView(R.id.textView)
    TextView textview;
    @BindView(R.id.button)
    TextView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindViewTools.bind(this);

        textview.setText("ok");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview.setText("onclick");
            }
        });
    }
}
