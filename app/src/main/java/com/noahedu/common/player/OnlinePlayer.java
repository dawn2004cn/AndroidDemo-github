package com.noahedu.common.player;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class OnlinePlayer extends BasePlayer{
	private HandlerThread thread;
	private Handler handler;
	
	static class PrepareData{
		String source;
		OnPreparedListener outPreparedListener;
		OnCompletionListener outCompletionListener;
	}
	
	
	private final int PREPAREURL = 0;
	private final int PLAY = 1;
	private final int STOP = 2;
	private final int QUIT = 3;
	private final int PAUSE = 4;
	private final int RESUME = 5;
	
	public OnlinePlayer() {
		// TODO Auto-generated constructor stub
		player = new Player();
		isPreParedFlag = true;
		init();
	}
	
	private void init(){
		thread = new HandlerThread("OnlinePlayer");
		thread.start();
		handler = new Handler(thread.getLooper()){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what){
					case PREPAREURL:
					{
						PrepareData data = (PrepareData)msg.obj;
						player.prepareUrl(data.source, data.outPreparedListener, data.outCompletionListener);
						break;
					}
					case PLAY:
					{
						player.playUrl();
						break;
					}
					case STOP:
					{
//						Player.stopSound(player, false);
						player.stop();
						break;
					}
					case QUIT:
					{
						player.release();
						thread.quit();
						break;
					}
					case PAUSE:
					{
						player.pauseUrl();
						break;
					}
					case RESUME:
					{
						player.resumeUrl();
						break;
					}
				}
			}
			
		};	
	}
	
	@Override
	public void play(String source, OnPreparedListener outPreparedListener, OnCompletionListener outCompletionListener) {
		// TODO Auto-generated method stub
//		player.prepareUrl(source, outPreparedListener, listener);
		Message msg = Message.obtain();
		msg.what = PREPAREURL;
		PrepareData data = new PrepareData();
		data.source = source;
		data.outPreparedListener = outPreparedListener;
		data.outCompletionListener = outCompletionListener;
		msg.obj = data;
		handler.sendMessage(msg);
		handler.sendEmptyMessage(PLAY);
		isPlayingFlag = true;
		isPreParedFlag = false;
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		if(isPlayingFlag){
			handler.sendEmptyMessage(PAUSE);
			isPlayingFlag = false;
		}
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
//		player.resumeUrl();

		handler.sendEmptyMessage(PLAY);
		isPlayingFlag = true;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
//		player.stop();
		handler.sendEmptyMessage(STOP);
		isPlayingFlag = false;
	}

	@Override
	public boolean isPrePared() {
		// TODO Auto-generated method stub
		return isPreParedFlag;
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return isPlayingFlag;
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		int duration = 0;
		if(isPreParedFlag){
			duration = player.getDuration();
		}
		return duration;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		int curDuration = 0;
		if(isPreParedFlag){
			curDuration = player.getCurrentPosition();
		}
		return curDuration;
	}

	@Override
	public void seekTo(int offsetTime) {
		// TODO Auto-generated method stub
		if(isPreParedFlag){
			player.seekTo(offsetTime);
		}
	}

	@Override
	public void release() {
		handler.sendEmptyMessage(QUIT);
	}

}
