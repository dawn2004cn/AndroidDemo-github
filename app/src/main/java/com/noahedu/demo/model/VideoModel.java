package com.noahedu.demo.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


import com.noahedu.demo.PlayerActivity;

import java.util.List;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：VideoModel$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/3/27$ 12:16$
 */
public class VideoModel {
    String msg;
    String status;
    public ValueEntity value;


    public static class ValueEntity{
        public List<UrlEntity> videos;

        public List<UrlEntity> getVideos() {
            return videos;
        }

        public void setVideos(List<UrlEntity> videos) {
            this.videos = videos;
        }

        public Intent buildIntent(
                Context context) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.setData(Uri.parse(videos.get(0).getPlayurl()));
            intent.putExtra(PlayerActivity.MEDIA_NAME,videos.get(0).videoName);
            return intent;
        }

    }

    public static class UrlEntity{
        public String playurl;
        public String videoName;

        public String getPlayurl() {
            return playurl;
        }

        public void setPlayurl(String playurl) {
            this.playurl = playurl;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ValueEntity getValue() {
        return value;
    }

    public void setValue(ValueEntity value) {
        this.value = value;
    }
}
