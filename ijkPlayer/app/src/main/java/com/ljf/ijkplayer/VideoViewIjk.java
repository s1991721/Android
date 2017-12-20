package com.ljf.ijkplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by mr.lin on 2017/12/20.
 * ijkPlayer控件
 */

public class VideoViewIjk extends FrameLayout implements IjkPlayerListener {

    private SurfaceView surfaceView;
    private IMediaPlayer mediaPlayer;
    private IjkPlayerListener ijkPlayerListener;

    private int mediacodec;
    private int mediacodec_auto_rotate;
    private int mediacodec_handle_resolution_change;
    private int opensles;
    private String overlay_format;
    private int framedrop;
    private int start_on_prepared;
    private int http_detect_range_support;
    private int skip_loop_filter;

    public VideoViewIjk(@NonNull Context context) {
        this(context, null);
    }

    public VideoViewIjk(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoViewIjk(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VideoViewIjk);
        mediacodec = typedArray.getInt(R.styleable.VideoViewIjk_mediacodec, 0);
        mediacodec_auto_rotate = typedArray.getInt(R.styleable.VideoViewIjk_mediacodec_auto_rotate, 0);
        mediacodec_handle_resolution_change = typedArray.getInt(R.styleable.VideoViewIjk_mediacodec_handle_resolution_change, 0);
        opensles = typedArray.getInt(R.styleable.VideoViewIjk_opensles, 0);
        overlay_format = typedArray.getString(R.styleable.VideoViewIjk_opensles);
        if (TextUtils.isEmpty(overlay_format)) {
            overlay_format = IjkMediaPlayer.SDL_FCC_RV32 + "";
        }
        framedrop = typedArray.getInt(R.styleable.VideoViewIjk_framedrop, 1);
        start_on_prepared = typedArray.getInt(R.styleable.VideoViewIjk_start_on_prepared, 0);
        http_detect_range_support = typedArray.getInt(R.styleable.VideoViewIjk_http_detect_range_support, 0);
        skip_loop_filter = typedArray.getInt(R.styleable.VideoViewIjk_skip_loop_filter, 48);
        typedArray.recycle();
        init();
    }

    private void init() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        initPlayer();
        initView();
    }

    private void initPlayer() {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", mediacodec);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", mediacodec_auto_rotate);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", mediacodec_handle_resolution_change);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", opensles);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", overlay_format);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", framedrop);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", start_on_prepared);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", http_detect_range_support);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "skip_loop_filter", skip_loop_filter);

        mediaPlayer = ijkMediaPlayer;
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
    }

    public void setVideoViewIjkListener(IjkPlayerListener playerListener) {
        ijkPlayerListener = playerListener;
    }

    private void initView() {
        surfaceView = new SurfaceView(getContext());
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setDisplay(surfaceView.getHolder());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(surfaceView, 0, layoutParams);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(null);
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDetachedFromWindow();
    }

    //------------------------播放器操作-------------------
    public void setDataSource(String source) {
        try {
            mediaPlayer.setDataSource(source);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        mediaPlayer.start();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void seekTo(long position) {
        mediaPlayer.seekTo(position);
    }

    public long getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public long getDuration() {
        return mediaPlayer.getDuration();
    }

    public void release() {
        mediaPlayer.release();
    }

    public void reset() {
        mediaPlayer.reset();
    }

    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public void setBrightness() {
        // TODO: 2017/12/20
    }

    //------------------------监听-------------------
    @Override
    public void onPrepared(IMediaPlayer mp) {
        if (ijkPlayerListener != null) {
            ijkPlayerListener.onPrepared(mp);
        }
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        if (ijkPlayerListener != null) {
            ijkPlayerListener.onCompletion(mp);
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        if (ijkPlayerListener != null) {
            ijkPlayerListener.onBufferingUpdate(mp, percent);
        }
    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {
        if (ijkPlayerListener != null) {
            ijkPlayerListener.onSeekComplete(mp);
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        if (ijkPlayerListener != null) {
            ijkPlayerListener.onVideoSizeChanged(mp, width, height, sar_num, sar_den);
        }
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        if (ijkPlayerListener != null) {
            return ijkPlayerListener.onError(mp, what, extra);
        }
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        if (ijkPlayerListener != null) {
            return ijkPlayerListener.onInfo(mp, what, extra);
        }
        return false;
    }
}
