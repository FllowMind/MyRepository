package com.example.administrator.kok_music_player.Utils.musicutils;

/**
 * Created by Administrator on 2016/5/27.
 */
public class Lyric {
    private Long time;
    private String content;

    public Lyric(Long time, String content) {
        this.time = time;
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

