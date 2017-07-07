package com.lubook.os.meida;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lubook.os.R;
import com.lubook.os.meida.util.impl.IMediaPlayerStatusListener;
import com.lubook.os.meida.util.MediaPlayerManager;
import com.lubook.os.meida.util.BaiLocHelper;
import com.lubook.os.meida.util.GlideCircleTransform;
import java.io.File;
import java.util.List;
import jp.wasabeef.glide.transformations.BlurTransformation;
import static android.media.MediaPlayer.MEDIA_ERROR_IO;
import static android.media.MediaPlayer.MEDIA_ERROR_MALFORMED;
import static android.media.MediaPlayer.MEDIA_ERROR_SERVER_DIED;
import static android.media.MediaPlayer.MEDIA_ERROR_TIMED_OUT;
import static android.media.MediaPlayer.MEDIA_ERROR_UNKNOWN;
import static android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED;

/**
 * Created by ZJcan on 2017-06-12.
 */

public class MusicViewController implements View.OnClickListener {

    private final static String TAG = "reeman::MusicViewController";
    public  MediaPlayerManager playerManager = null;

    private MusicActivity mContext;
    private ImageView mLastSong;
    private ImageView mPlayState;
    private ImageView mNextSong;
    private TextView mSongName;
    private TextView mAuthor;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private String mTime;
    private ImageView mMusicPic;
    private RelativeLayout mRotate;
    private ImageView mBackground;
    private TextView mAlbum;
    private SeekBar mSeekBar;
    public static boolean isStoping = true;
    public static Handler mUpdateHandler = null;
    Animation rotateAnimation;
    private  MyLocationListener myLocationListener;
    private TextView lcoation_title;

    public MusicViewController(Context c) {
        this.mContext = (MusicActivity) c;
        mUpdateHandler = new Handler();
        playerManager = new MediaPlayerManager();

        playerManager.setmOnCompletionListener(mOnCompletionListener);
        playerManager.setmOnPreparedListener(mOnPreparedListener);
        playerManager.setmOnErrorListner(mOnErrorListner);
        playerManager.setmOnPlayerStatusListener(mStatusListener);
        initView();
        BaiDuLocInit();
    }

    private void BaiDuLocInit() {
        myLocationListener = new MyLocationListener();
        BaiLocHelper.createInstance(mContext.getApplication());
        BaiLocHelper.getInstance().registerListener(myLocationListener);
        BaiLocHelper.getInstance().start();
    }

    /**
     * 资源更新
     *
     * @param mSongList
     */
    public void setSongList(List<BaseBean> mSongList) {
        if (playerManager != null) {
            playerManager.setSongList(mSongList);
        }
    }

    /**
     * 播放音乐
     */
    public void playMusic() {
        playerManager.playUrl(0);
    }

    /**
     * 暂停音乐
     */
    public void pauseMusic() {
        playerManager.pause();
    }

    /**
     * 初始化资源文件
     */
    private void initView() {
        mLastSong = (ImageView) mContext.findViewById(R.id.last_song);
        mPlayState = (ImageView) mContext.findViewById(R.id.play_state);
        mNextSong = (ImageView) mContext.findViewById(R.id.next_song);
        mSongName = (TextView) mContext.findViewById(R.id.song_name);
        mAuthor = (TextView) mContext.findViewById(R.id.author);
        mCurrentTime = (TextView) mContext.findViewById(R.id.current_time);
        mTotalTime = (TextView) mContext.findViewById(R.id.total_time);
        mMusicPic = (ImageView) mContext.findViewById(R.id.music_picture);
        mSeekBar = (SeekBar) mContext.findViewById(R.id.seekbar);
        mBackground = (ImageView) mContext.findViewById(R.id.background);
        mRotate = (RelativeLayout) mContext.findViewById(R.id.rotate);
        lcoation_title = (TextView)mContext.findViewById(R.id.location_title);
        mLastSong.setOnClickListener(this);
        mPlayState.setOnClickListener(this);
        mNextSong.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(10000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(mContext, android.R.anim.linear_interpolator);//设置动画插入器
        mRotate.setAnimation(rotateAnimation);

    }


    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                playerManager.setProgress(progress * 1000);
                LogUtils.d(TAG, "initView onProgressChanged 66666");
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (!playerManager.isPlaying()) {
                playerManager.pause();
                LogUtils.d(TAG, "initView onStartTrackingTouch 44444");
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (playerManager.isPlaying()) {
                playerManager.start();
                LogUtils.d(TAG, "initView onStopTrackingTouch 55555");
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.last_song:
                removeCallbackAndMessages(null);/*播放上一首时停止当前的更新进度状态*/
                if (playerManager.isFirstMusic()) {
                    Toast.makeText(mContext, "这是第一首歌", Toast.LENGTH_SHORT).show();
                } else {
                    mPlayState.setBackgroundResource(R.drawable.music_start_selector);
                    playerManager.playPre();
                }
                break;
            case R.id.play_state:

                if (playerManager.isPlaying()) {
                    mPlayState.setBackgroundResource(R.drawable.music_pause_selector);
                    playerManager.pause();
                } else {
                    isStoping = false;//这块设置为true后导致 暂停和播放切换时, 歌词状态进度无法更新。
                    mPlayState.setBackgroundResource(R.drawable.music_start_selector);
                    playerManager.start();
                }
                break;
            case R.id.next_song:
                removeCallbackAndMessages(null);/*播放下一首时停止上一首的更新进度状态*/
                if (playerManager.isLastMusic()) {
                    Toast.makeText(mContext, "这是最后一首歌", Toast.LENGTH_SHORT).show();
                } else {
                    mPlayState.setBackgroundResource(R.drawable.music_start_selector);
                    playerManager.playNext();
                }
                break;
            default:
                break;
        }
    }

    private MediaPlayer.OnErrorListener mOnErrorListner = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (what == MEDIA_ERROR_SERVER_DIED) {
                //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
                playerManager.getMediaPlayerInstance();
                Toast.makeText(mContext, "网络服务错误", Toast.LENGTH_LONG).show();
            } else if (what == MEDIA_ERROR_UNKNOWN) {
                switch (extra) {
                    case MEDIA_ERROR_IO:
                        LogUtils.d(TAG, "文件不存在或错误，或网络不可访问错误:MEDIA_ERROR_IO：" + MEDIA_ERROR_IO);
                        break;
                    case MEDIA_ERROR_MALFORMED:
                        LogUtils.d(TAG, "流不符合有关标准或文件的编码规范:MEDIA_ERROR_MALFORMED：" + MEDIA_ERROR_MALFORMED);
                        break;
                    case MEDIA_ERROR_UNSUPPORTED:
                        LogUtils.d(TAG, "比特流符合相关编码标准或文件的规格，但媒体框架不支持此功能:MEDIA_ERROR_UNSUPPORTED：" + MEDIA_ERROR_UNSUPPORTED);
                        break;
                    case MEDIA_ERROR_TIMED_OUT:
                        Toast.makeText(mContext, "网络超时错误", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        LogUtils.d(TAG, "未知错误:" + extra);
                        break;
                }

            }
            return false;
        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {/*当播放到达数据流的末尾，播放就完成了*/
        @Override
        public void onCompletion(MediaPlayer mp) {/*获取的时间值不准确*/
            //根据播放模式播放下一首歌
            isStoping = true;
            removeCallbackAndMessages(null);
            playerManager.playNext();
            LogUtils.d(TAG, "mPosition 根据播放模式播放下一首歌");
        }
    };


    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {/*播放器初始化完成后回调*/
        @Override
        public void onPrepared(MediaPlayer mp) {
            isStoping = false;
            playerManager.start();
            LogUtils.d(TAG, "onStartCommand: " + "听音乐");
            mSongName.setText(playerManager.getSongName());
            mAuthor.setText("歌手:" + playerManager.getAuthor());
            mTime = playerManager.getSongTime();
            mTotalTime.setText(mTime);
            File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "downloadMusic/" + playerManager.getAuthor() + ".jpg");
            Glide.with(mContext).load(file).transform(new GlideCircleTransform(mContext)).into(mMusicPic);
            Glide.with(mContext).load(file).bitmapTransform(new BlurTransformation(mContext, 30), new CenterCrop(mContext)).into(mBackground);
            updateTime();
        }
    };

    private IMediaPlayerStatusListener mStatusListener = new IMediaPlayerStatusListener() {
        @Override
        public void onPlay() {
            rotateAnimation.startNow();
            mPlayState.setBackgroundResource(R.drawable.music_start_selector);
        }

        @Override
        public void onPause() {
            rotateAnimation.cancel();
            mPlayState.setBackgroundResource(R.drawable.music_pause_selector);
        }
    };

    private void updateTime() {
        mUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isStoping) {
                            LogUtils.d(TAG, "间隔1s 更新一次时间进度 updateSeekBar  updateTime");
                            updateSeekBar();
                            updateTime();

                        }
                    }
                });
            }
        }, 1000);
    }

    /**
     * 更新进度条
     */
    private void updateSeekBar() {
        int position = playerManager.getCurrentPosition();

        int time = position / 1000;
        int min = time / 60;
        if (time > 0 && time < 3600) {
            int sec = time - min * 60;
            mCurrentTime.setText((min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec));
            mSeekBar.setProgress(time);
            mSeekBar.setMax(playerManager.getSongTotalTime());
        } else {
            mCurrentTime.setText("00:00");
        }

    }

    /**
     * 统一释放资源 release
     */
    public void release() {
        playerManager.stop();
        removeCallbackAndMessages(null);
        BaiLocHelper.getInstance().unregisterListener(myLocationListener);
        BaiLocHelper.getInstance().stop();
    }

    /**
     * 释放回调
     *
     * @param obj
     */
    public void removeCallbackAndMessages(Object obj) {
        if (mUpdateHandler == null) return;
        LogUtils.d(TAG, "释放mUpdateHandler持有的线程回调");
        mUpdateHandler.removeCallbacksAndMessages(obj);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lcoation_title.setText(bdLocation.getCity());
                    LogUtils.d(TAG, "地址："+bdLocation.getCity());
                }
            });
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.i("MainActivity", "地址 connect hot spot message: " + s);
        }
    }

}
