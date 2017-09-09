package com.lubook.os.utils;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Vibrator;

/*
 使用手册

 1:初始化
 player = new PlayerUtil(this);

 2:播放设置，Assets 目录下的音频文件 是否重复
 player.playAssetsFile("outgoing.ogg", true);

 */
public class PlayerUtil {

	private MediaPlayer mPlayer;
	Context context;

	public PlayerUtil(Context context) {
		this.context = context;
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
	}
	private static int touchSoundId, speechId;
	/*参考: http://blog.csdn.net/yy1300326388/article/details/47044869*/
	private static SoundPool notificationMediaplayer;
	private static Vibrator notificationVibrator;
	/**
	 * 点击按键声效
	 */
	public void playNotification() {
		notificationMediaplayer.play(touchSoundId, 1, 1, 0, 0, 1);
		notificationVibrator.vibrate(50);
	}

	/**
	 * 点击按键声效
	 */
	public void playSpeechSound() {
		notificationMediaplayer.play(speechId, 1, 1, 0, 0, 1);
		notificationVibrator.vibrate(50);
	}

	private AssetManager mAssetManager;

	/**
	 * @param file Assets 目录下的音频文件
	 * @param repeat 是否重复
	 */
	public void playAssetsFile(String file, boolean repeat) {
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
		try {
			mAssetManager = context.getAssets();
			AssetFileDescriptor fd = mAssetManager.openFd(file);
			if (mPlayer != null && mPlayer.isPlaying()) {
				mPlayer.pause();
			}
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.reset();
			mPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
			fd.close();
			mPlayer.prepare();
			mPlayer.setLooping(repeat);
			mPlayer.start();
			if (!repeat) {/*不重复时释放播放器*/
				mPlayer.setOnCompletionListener(myComPlistener);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		try {
			if (mPlayer == null) {
				return;
			}
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
			}
			mPlayer.release();
			mPlayer = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mPlayer = null;
		}
	}

	OnCompletionListener myComPlistener = new OnCompletionListener() {

		public void onCompletion(MediaPlayer mp) {
			listener.playOver();
			mPlayer.pause();
			mPlayer.stop();
			mPlayer.release();
		}
	};

	/**
	 * 可以在外部监听是否播放完成
	 * @param listener
	 */
	public void setPlayerListener(PlayerListener listener) {
		this.listener = listener;
	}

	PlayerListener listener;

	public interface PlayerListener {
		/** 播放完毕回调 */
		void playOver();
	}

}
