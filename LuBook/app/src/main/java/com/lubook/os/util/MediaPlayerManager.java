package com.lubook.os.util;

import android.media.MediaPlayer;

import com.blankj.utilcode.util.LogUtils;
import com.lubook.os.base.BaseBean;
import com.lubook.os.util.impl.IMediaPlayerStatusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理音乐状态的类
 */
public class MediaPlayerManager {


    private static final String TAG = "reeman::MediaPlayerManager";
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnErrorListener mOnErrorListner;
    private IMediaPlayerStatusListener mOnPlayerStatusListener;
    public MediaPlayer mMediaPlayer = null;
    public List<BaseBean> mList;
    public static int cunIndex = 0;

    public MediaPlayerManager() {
        mList = new ArrayList<>();
        this.mMediaPlayer = getMediaPlayerInstance();
    }

    public void setmOnPreparedListener(MediaPlayer.OnPreparedListener mOnPrepare) {
        this.mOnPreparedListener = mOnPrepare;
    }

    public void setmOnErrorListner(MediaPlayer.OnErrorListener mOnError) {
        this.mOnErrorListner = mOnError;
    }

    public void setmOnCompletionListener(MediaPlayer.OnCompletionListener mOnComplet) {
        this.mOnCompletionListener = mOnComplet;
    }

    public void setmOnPlayerStatusListener(IMediaPlayerStatusListener statusListener) {
        this.mOnPlayerStatusListener = statusListener;
    }

    public void setSongList(List<BaseBean> mList) {
        this.mList = mList;
    }

    public synchronized MediaPlayer getMediaPlayerInstance() {
        if (mMediaPlayer == null) {
            LogUtils.d(TAG, "实例化MediaPlayer对象");
            return new MediaPlayer();
        } else {
            return mMediaPlayer;
        }
    }

    public void playUrl(int index) {
        pause();
        try {
            String audiopath = mList.get(index).getAudiopath();
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(audiopath);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListner);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isLastMusic() {
        return cunIndex == mList.size() - 1;
    }

    public boolean isFirstMusic() {
        return cunIndex == 0;
    }

    public void playPre() {
        cunIndex = cunIndex - 1;
        requestSong();
    }

    public void playNext() {
        cunIndex = cunIndex + 1;
        requestSong();

    }

    /**
     * 根据音乐的audiopath来请求播放音乐
     */
    public void requestSong() {
        if (cunIndex == mList.size()) {
            cunIndex = 0;//如果是播放到了最后一首，自动调到第一首歌
        }
        playUrl(cunIndex);
    }


    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pause() {
        if (isPlaying()) {
            try {
                mMediaPlayer.pause();
            } catch (IllegalStateException e) {
                LogUtils.d(TAG, "pause() an Error has occur..." + e.getMessage());
            }
        }

        if (mOnPlayerStatusListener != null) {
            mOnPlayerStatusListener.onPause();
        }
    }

    public void start() {
        if (!isPlaying()) {
            try {
                mMediaPlayer.start();
            } catch (IllegalStateException e) {
                LogUtils.d(TAG, "start() an Error has occur..." + e.getMessage());
            }
        }

        if (mOnPlayerStatusListener != null) {
            mOnPlayerStatusListener.onPlay();
        }
    }

    public String getSongName() {
        return mList.get(cunIndex).getSongName();
    }

    public String getAuthor() {
        String name = null;
        //>成龙</em>超级精装大戏主题曲
        name = mList.get(cunIndex).getAuthor();
        int em1 = name.indexOf("em");
        if (em1 != -1) {
            String s1 = name.substring(em1 + 2);
            int em2 = s1.indexOf("em");
            String author = s1.substring(1, em2 - 2);
            return author;
        }
        return name;
    }

    public String getSongTime() {
        int time = 0;
        time = mMediaPlayer.getDuration() / 1000;
        int min = time / 60;
        int sec = time - 60 * min;
        return (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
    }

    public int getSongTotalTime() {
        int time = 0;
        time = mMediaPlayer.getDuration() / 1000;
        return time;
    }

    public void stop() {
        cunIndex = 0;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer.release();//释放资源
            mMediaPlayer = null;
        }
        LogUtils.d(TAG, "停止播放，释放音乐");
    }

    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            int currentPosition = 0;
            try {
                currentPosition = mMediaPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "a error has throw ...\n" + e.getMessage());
            }
            return currentPosition;
        } else {
            return 0;
        }
    }

    public void setProgress(int progress) {
        mMediaPlayer.seekTo(progress);
    }

    public String getAlbum() {
        return mList.get(cunIndex).getAlbum_title();
    }

}