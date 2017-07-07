package com.lubook.os.meida;

import java.io.Serializable;

public class BaseBean implements Serializable {
    /**
     * author : <em>张学友</em>
     * album_title : 吻别
     * audiopath: 歌曲url
     * songName: 歌曲名
     */

    private String author;
    private String album_title;
    private String audiopath;
    private String songName;

    public BaseBean(String author, String audiopath, String album_title, String songName) {
        this.author = author;
        this.audiopath = audiopath;
        this.album_title = album_title;
        this.songName = songName;
    }

    public BaseBean() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getAudiopath() {
        return audiopath;
    }

    public void setAudiopath(String audiopath) {
        this.audiopath = audiopath;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}