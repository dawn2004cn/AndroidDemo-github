package com.noahedu.common.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public abstract class BasePlayer {
	protected Player player;
	protected boolean isPreParedFlag;
	protected boolean isPlayingFlag;
	
	public void setPrepared(boolean isPreParedFlag){
		this.isPreParedFlag = isPreParedFlag;
	}
	
	public MediaPlayer  getMediaPlayer(){
		return player.getMediaPlayer();
	}
	
	public abstract void play(String source, OnPreparedListener  outPreparedListener, 
			OnCompletionListener outCompletionListener);
	public abstract void pause();
	public abstract void resume();
	public abstract void stop();
	
	public abstract boolean isPrePared();
	public abstract boolean isPlaying();
	public abstract int getDuration();
	public abstract int getCurrentPosition();
	public abstract void seekTo(int offsetTime);
	public abstract void release();	
}
