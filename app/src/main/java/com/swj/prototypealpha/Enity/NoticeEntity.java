package com.swj.prototypealpha.Enity;

import java.io.Serializable;

public class NoticeEntity implements Serializable {
    private  String title;
    private String time;
    private String text;
    private String sender;
    private String fromer;

    public NoticeEntity(String title, String time,String sender, String fromer,String text) {
        this.title = title;
        this.time = time;
        this.text = text;
        this.sender =sender;
        this.fromer =fromer;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getFromer() {
        return fromer;
    }

    public void setFromer(String fromer) {
        this.fromer = fromer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
