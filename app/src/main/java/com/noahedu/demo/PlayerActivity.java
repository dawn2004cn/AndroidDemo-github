package com.noahedu.demo;
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.res.Configuration;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.noahedu.demo.player.Data2Source;

import chuangyuan.ycj.videolibrary.listener.OnCoverMapImageListener;
import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.listener.VideoWindowListener;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.video.MediaSourceBuilder;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
import chuangyuan.ycj.videolibrary.whole.WholeMediaSource;


/** An activity that plays media using {@link SimpleExoPlayer}. */
public class PlayerActivity extends AppCompatActivity {

    private ExoUserPlayer exoPlayerManager;
    private Movie mSelectedMovie;
    private Uri uri;
    private static final String TAG = "MainDetailedActivity";
    public static  final String MEDIA_NAME="media_name";
    private String mediaName = "";
    private WholeMediaSource wholeMediaSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //mSelectedMovie = (Movie) getIntent().getSerializableExtra(DetailsActivity.MOVIE);
        uri = getIntent().getData();
        mediaName = getIntent().getStringExtra(MEDIA_NAME);

        wholeMediaSource = new WholeMediaSource(this, new Data2Source(getApplication()));
        MediaSource videoSource = wholeMediaSource.initMediaSource(uri);
        wholeMediaSource.setMediaSource(videoSource);
        //实例化
        exoPlayerManager = new VideoPlayerManager.Builder(this, VideoPlayerManager.TYPE_PLAY_GESTURE, R.id.player_view)
                .setDataSource(wholeMediaSource)
                //设置视频标题
                .setTitle(mediaName)
                .setPlayerGestureOnTouch(true)
                // .setPlayUri("/storage/sdcard0/DCIM/Camera/VID_20180829_100348.mp4")
                //加载rtmp 协议视频
                //加载m3u8
                .setPlayUri(uri)
                .addOnWindowListener(new VideoWindowListener() {
                    @Override
                    public void onCurrentIndex(int currentIndex, int windowCount) {
                        Toast.makeText(getApplication(), currentIndex + "windowCount:" + windowCount, Toast.LENGTH_SHORT).show();
                    }
                })
                /* .addUpdateProgressListener(new AnimUtils.UpdateProgressListener() {
                     @Override
                     public void updateProgress(long position, long bufferedPosition, long duration) {
 //                     //   Log.d(TAG,"bufferedPosition:"+position);
                     //    Log.d(TAG,"duration:"+duration);
                     }
                 })*/
                .addVideoInfoListener(new VideoInfoListener() {

                    @Override
                    public void onPlayStart(long currPosition) {

                    }

                    @Override
                    public void onLoadingChanged() {

                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException e) {

                    }

                    @Override
                    public void onPlayEnd() {
                        // Toast.makeText(getApplication(), "asd", Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void isPlaying(boolean playWhenReady) {

                    }
                })
                .setOnCoverMapImage(new OnCoverMapImageListener() {
                    @Override
                    public void onCoverMap(ImageView v) {
                        Glide.with(v.getContext())
                                .load(R.drawable.trinea)
                                .into(v);
                    }
                })
                .create();
        exoPlayerManager.startPlayer();
        //播放视频
        //d隐藏控制布局
        // exoPlayerManager.hideControllerView();
        //隐藏进度条
        // exoPlayerManager.hideSeekBar();
        //显示进度条
        //exoPlayerManager.showSeekBar();
        //是否播放
        // exoPlayerManager.isPlaying();
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.d(TAG, "onResume");
        exoPlayerManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        exoPlayerManager.onPause();
    }


    @Override
    protected void onDestroy() {
        exoPlayerManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        exoPlayerManager.onConfigurationChanged(newConfig);//横竖屏切换
    }

    @Override
    public void onBackPressed() {
        if (exoPlayerManager.onBackPressed()) {
            ActivityCompat.finishAfterTransition(this);
        }
    }


}
