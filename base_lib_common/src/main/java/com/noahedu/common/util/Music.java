/**
 * © 2019 www.youxuepai.com
 * @file name：Music.java.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2019-11-22上午1:23:14
 * @version 1.0
 */
package com.noahedu.common.util;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：Music
 *  描述：简单描述该类的作用
 * @class name：Music
 * @anthor : daisg
 * @time 2019-11-22上午1:23:14
 * @version V1.0
 */
public class Music {
    private int id;//音乐编号
    private String name;//音乐名
    private String singer;//歌手名
    private String album;//专辑名
    private int duration;//时长,以秒为单位
    private String path;//音乐文件存放路径
 
    /*
       无参构造器
     */
    public Music(){
        
    }
 
    public Music(String name, String singer, String album, int duration, String path) {
        this.name = name;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
    }
 
    public Music(int id, String name, String singer, String album, int duration, 
        String path) {
 
        this.id = id;
        this.name = name;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getSinger() {
        return singer;
    }
 
    public void setSinger(String singer) {
        this.singer = singer;
    }
 
    public int getDuration() {
        return duration;
    }
 
    public void setDuration(int duration) {
        this.duration = duration;
    }
 
    public String getPath() {
        return path;
    }
 
    public void setPath(String path) {
        this.path = path;
    }
 
    public String getAlbum() {
        return album;
    }
 
    public void setAlbum(String album) {
        this.album = album;
    }
 
    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                '}';
    }

}
