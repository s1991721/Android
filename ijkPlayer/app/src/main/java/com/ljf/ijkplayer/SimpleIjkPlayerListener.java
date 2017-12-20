package com.ljf.ijkplayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by mr.lin on 2017/12/20.
 */

public class SimpleIjkPlayerListener implements IjkPlayerListener {
    @Override
    public void onPrepared(IMediaPlayer mp) {

    }

    @Override
    public void onCompletion(IMediaPlayer mp) {

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {

    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {

    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {

    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        return false;
    }
}
