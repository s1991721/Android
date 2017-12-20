package com.ljf.ijkplayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by mr.lin on 2017/12/20.
 * ijkPlayer监听
 */

public interface IjkPlayerListener extends IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener {
}