package com.noahedu.demo.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

public class SimplePlayer {
    private MediaPlayer mPlayer = null;

    private static SimplePlayer mInstance = null;

    public static SimplePlayer getInstance() {
        if (mInstance == null) {
            mInstance = new SimplePlayer();
        }

        return mInstance;
    }

    private SimplePlayer() {
        mPlayer = new MediaPlayer();
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    private OnCompletionListener getMediaPlayerCompletionListener(MediaPlayer player) {
        try {
            Field field = player.getClass().getDeclaredField("mOnCompletionListener");
            field.setAccessible(true);
            return (OnCompletionListener) field.get(player);
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void preparePlay(OnCompletionListener listener) {
        if (mPlayer != null) {
            stopMediaPlayer();
        } else {
            mPlayer = new MediaPlayer();
        }
        mPlayer.setOnCompletionListener(listener);
    }

    private void startPlay() throws IllegalStateException, IOException {
        mPlayer.prepareAsync();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                          @Override
                                          public void onPrepared(MediaPlayer mp) {
                                              mp.start();
                                          }
                                      }
        );
        //mPlayer.start();
    }

    /**
     * 播放asset里面的音频
     *
     * @param assetFileName
     * @param listener
     */
    public void playAssetSound(Context ctx,String assetFileName, OnCompletionListener listener) {
        AssetFileDescriptor afd = null;
        try {
            afd = ctx.getAssets().openFd(assetFileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        play(afd, listener);
    }

    /**
     * 播放在线音频
     * @param url
     * @param listener
     */
    public void playOnlineMusic(String url, OnCompletionListener listener) {
        preparePlayNormal(listener);
        try {
            mPlayer.setDataSource(url);
            startPlay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(AssetFileDescriptor source, OnCompletionListener listener) {
        preparePlay(listener);
        try {
            //mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(source.getFileDescriptor(), source.getStartOffset(), source.getLength());
            source.close();
            startPlay();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch blockA
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

    public int getDuration() {
        return mPlayer != null ? mPlayer.getDuration() : 0;
    }

    public int getCurrentPosition() {
        return mPlayer != null ? mPlayer.getCurrentPosition() : 0;
    }

    public void stop() {
        if (mPlayer != null) {
            stopMediaPlayer();
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

    private void stopMediaPlayer() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
            OnCompletionListener listener = getMediaPlayerCompletionListener(mPlayer);
            if (listener != null) {
                listener.onCompletion(mPlayer);
            }
        }
        mPlayer.reset();
    }

    public void release() {
        if (mPlayer != null) {
            stopMediaPlayer();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void preparePlayNormal(OnCompletionListener listener) {
        if (mPlayer != null) {
            stopMediaPlayerNormal();
        } else {
            mPlayer = new MediaPlayer();
        }
        mPlayer.setOnCompletionListener(listener);
    }


    public void stopNormal() {
        if (mPlayer != null) {
            stopMediaPlayerNormal();
            mPlayer.setOnCompletionListener(null);
        }
    }

    /**
     * stop时，如果有OnCompletionListener回调，不再调用OnCompletionListener回调。
     */
    private void stopMediaPlayerNormal() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
            // OnCompletionListener listener =
            // getMediaPlayerCompletionListener(mPlayer);
            // if (listener != null){
            // listener.onCompletion(mPlayer);
            // }
        }
        mPlayer.reset();
    }

    public void releaseNormal() {
        if (mPlayer != null) {
            stopMediaPlayerNormal();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
