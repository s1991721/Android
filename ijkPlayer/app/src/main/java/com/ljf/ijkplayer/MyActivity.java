package com.ljf.ijkplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by mr.lin on 2017/12/20.
 */

public class MyActivity extends AppCompatActivity {

    VideoViewIjk videoViewIjk;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        videoViewIjk = findViewById(R.id.surfaceView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go();
            }
        });
        videoViewIjk.setVideoViewIjkListener(new SimpleIjkPlayerListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mp.start();
            }
        });
    }

    private void go() {
        videoViewIjk.setDataSource(editText.getText().toString().trim());
    }

}
