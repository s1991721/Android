package com.ljf.exoplayer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ljf.exoplayer.video.PlayerActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    String mp4 = "https://healthstation.blob.core.chinacloudapi.cn/video/sample.mp4";
    String hls = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVideoView();
    }

    private void initVideoView() {
        imageView = (ImageView) findViewById(R.id.imageview);
        findViewById(R.id.mp4).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goActivity(mp4);
            }
        });
        findViewById(R.id.hls).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goActivity(hls);
            }
        });
    }

    private void goActivity(String url) {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.setData(Uri.parse(url));
        intent.setAction(PlayerActivity.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView, "shareImage").toBundle());
        } else {
            startActivity(intent);
        }
    }

}
