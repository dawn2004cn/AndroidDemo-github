package com.noahedu.common.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Player {
	private MediaPlayer mPlayer = null;
	public static final String ROOTPATH = "/noahdata/Launcher";

	private static Player mInstance = null;
	public static Player getInstance() {
		if (mInstance == null) {
			mInstance = new Player();
		}

		return mInstance;
	}

	public Player() {
		mPlayer = new MediaPlayer();
	}

	public MediaPlayer getMediaPlayer() {
		return mPlayer;
	}

	public boolean isPlaying() {
		return mPlayer != null && mPlayer.isPlaying();
	}

	private void preparePlay(OnCompletionListener outCompletionListener) {
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
			}
			mPlayer.reset();
		} else {
			mPlayer = new MediaPlayer();
		}
		mPlayer.setOnCompletionListener(outCompletionListener);
	}

	private void startPlay() throws IllegalStateException, IOException {
		mPlayer.prepare();
		mPlayer.start();
	}

	public void play(String source, OnCompletionListener listener) {
		resetUrl();
		preparePlay(listener);
		Log.e("lugx", "play  source = " + source);
		try {
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			if (source.startsWith("/")) {
				mPlayer.setDataSource(new FileInputStream(source).getFD());
			} else {
				mPlayer.setDataSource(new FileInputStream(ROOTPATH + "/" + source).getFD());
			}

			startPlay();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play(String source, int offsetTime, OnCompletionListener outCompletionListener) {
		resetUrl();
		preparePlay(outCompletionListener);

		try {
			if (source.startsWith("/")) {
				mPlayer.setDataSource(new FileInputStream(source).getFD());
			} else {
				mPlayer.setDataSource(new FileInputStream(ROOTPATH + "/" + source).getFD());
			}

			startPlay();
			Log.e("lugx", "play offsetTime = " + offsetTime + ", getDuration() = " + getDuration());
			if (offsetTime < getDuration()) {
				mPlayer.seekTo(offsetTime);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void play(FileDescriptor fd, int offset, int length, int offsetTime, OnCompletionListener listener) {
		resetUrl();
		preparePlay(listener);

		try {
			mPlayer.setDataSource(fd, offset, length);

			startPlay();
			Log.e("lugx", "play offsetTime = " + offsetTime + ", getDuration() = " + getDuration());
			if (offsetTime < getDuration()) {
				mPlayer.seekTo(offsetTime);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setVicoe() {
		if (mPlayer != null) {
			mPlayer.setVolume(1f, 1f);
		}
	}

	public void play(AssetFileDescriptor source, OnCompletionListener listener) {
		resetUrl();
		preparePlay(listener);

		try {
			mPlayer.setDataSource(source.getFileDescriptor(), source.getStartOffset(), source.getLength());

			startPlay();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 播放asset里面的音频
	 *
	 * @param assetFileName
	 * @param listener
	 */
	public void playAssetSound(Context ctx, String assetFileName, OnCompletionListener listener) {
		AssetFileDescriptor afd = null;
		try {
			afd = ctx.getAssets().openFd(assetFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		play(afd, listener);
	}
	private boolean onlinePlayFlag = false;// 播放在线文件标志
	private boolean preparedFlag = false;// 在线文件准备完成标志
	private boolean startPlayUrlFlag = false;// 是否启动在线文件播放标志
	private OnPreparedListener outPreparedListener = null;
	private OnPreparedListener preparedListener = new OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			int duration = arg0.getDuration();
			Log.e("lugx", "onPrepared duration = " + duration);
			preparedFlag = true;
			if (startPlayUrlFlag) {
				arg0.start();
			}
			if (outPreparedListener != null) {
				outPreparedListener.onPrepared(arg0);
			}
		}
	};

	public void prepareUrl(String url, OnCompletionListener listener) {
		prepareUrl(url, null, listener);
	}

    public void prepareUrl(String url, OnPreparedListener outPreparedListener, OnCompletionListener OutCompletionListener) {

        preparePlay(OutCompletionListener);
        Log.e("lugx", "prepareUrl rul = " + url);
        onlinePlayFlag = true;
        preparedFlag = false;
        startPlayUrlFlag = false;
        this.outPreparedListener = outPreparedListener;
        try {
            mPlayer.setDataSource(url);
            mPlayer.setOnPreparedListener(preparedListener);
            mPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("lugx", "IllegalArgumentException");
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("lugx", "IllegalStateException");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("lugx", "FileNotFoundException");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("lugx", "IOException");
        }
    }

	public void playUrl() {
		if (!onlinePlayFlag) {
			return;
		}
		//startPlayUrlFlag = true;
		if (preparedFlag /* && !mPlayer.isPlaying() */) {
			mPlayer.start();
		}
	}

	public void playUrl(OnCompletionListener onCompletionListener) {
		if (!onlinePlayFlag) {
			return;
		}
		startPlayUrlFlag = true;
		if (preparedFlag /* && !mPlayer.isPlaying() */) {
			mPlayer.start();
			mPlayer.setOnCompletionListener(onCompletionListener);
		}
	}

    public void pauseUrl() {
        if (!onlinePlayFlag) {
            return;
        }
        startPlayUrlFlag = false;
        Log.e("PlaySoundState", "PlaySoundState:==>22222222222222"+preparedFlag+"    "+mPlayer.isPlaying());
        if (preparedFlag && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

	public void resumeUrl() {
		if (!onlinePlayFlag) {
			return;
		}
		playUrl();
	}

	private void resetUrl() {
		onlinePlayFlag = false;
		preparedFlag = false;
		startPlayUrlFlag = false;
		outPreparedListener = null;
	}

	public int getDuration(String source) {
		if(mPlayer==null){
			mPlayer = new MediaPlayer();
		}
		mPlayer.reset();
		try {
			if (source.startsWith("/")) {
				mPlayer.setDataSource(new FileInputStream(source).getFD());
			} else {
				mPlayer.setDataSource(new FileInputStream(ROOTPATH + "/" + source).getFD());
			}
			mPlayer.prepare();
			return mPlayer.getDuration();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getDuration() {
		return mPlayer != null ? mPlayer.getDuration() : 0;
	}

	public int getCurrentPosition() {
		return mPlayer != null ? mPlayer.getCurrentPosition() : 0;
	}

	public void stop() {
		if (mPlayer != null) {
			resetUrl();
			mPlayer.setOnPreparedListener(null);

			if (mPlayer.isPlaying()) {
				mPlayer.stop();
			}
			mPlayer.reset();
			mPlayer.setOnCompletionListener(null);
		}
	}

	public void pause() {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}

	public void resume() {
		if (mPlayer != null) {
			mPlayer.start();
		}
	}

	public void seekTo(int offsetTime) {
		if (mPlayer != null && offsetTime < getDuration()) {
			mPlayer.seekTo(offsetTime);
		}
	}

	public void release() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	public static void playSound(Player player, String file, OnCompletionListener listener) {
		// Log.d(TAG, "playsound: "+file);
		stopSound(player, false);
		if (player != null)
			player.play(file, listener);
	}

	public static void stopSound(Player player, boolean release) {
		if (player != null) {
			if(player.isPlaying()){
				player.stop();
			}
			if (release) {
				player.release();
				player = null;
			}
		}
	}
}
