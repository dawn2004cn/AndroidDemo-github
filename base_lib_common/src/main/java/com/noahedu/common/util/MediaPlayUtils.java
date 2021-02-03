package com.noahedu.common.util;

import android.media.MediaPlayer;

public class MediaPlayUtils {
	private static MediaPlayUtils mMediaPlayUtils;
    private MediaPlayer mMediaPlayer;


    public void setPlayOnCompleteListener(MediaPlayer.OnCompletionListener playOnCompleteListener){
        if(mMediaPlayer != null){
            mMediaPlayer.setOnCompletionListener(playOnCompleteListener);
        }
    }


    public static MediaPlayUtils getInstance(){
        if(mMediaPlayUtils == null){
        	mMediaPlayUtils = new MediaPlayUtils();
        }
        return  mMediaPlayUtils;
    }


    private MediaPlayUtils(){
        mMediaPlayer = new MediaPlayer();
    }


    public void play(String soundFilePath){
        if(mMediaPlayer == null){
            return;
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(soundFilePath);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.start();
				}
			});
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.reset();
					mp.release();
					mp = null;
				}
			});
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					return false;
				}
			});
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void pause(){
        if(mMediaPlayer != null){
            mMediaPlayer.pause();
        }
    }


    public void stop(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }
    
    public void release(){
    	if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
    		mMediaPlayer.stop();
    		mMediaPlayer.release();
    		mMediaPlayer = null;
        }
    }


    public int getCurrentPosition(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            return mMediaPlayer.getCurrentPosition();
        }else{
            return 0;
        }
    }


    public int getDutation(){
        if(mMediaPlayer!= null && mMediaPlayer.isPlaying()){
            return mMediaPlayer.getDuration();
        }else{
            return 0;
        }
    }


    public boolean isPlaying(){
        if(mMediaPlayer != null){
            return mMediaPlayer.isPlaying();
        }else{
            return false;
        }
    }

}
