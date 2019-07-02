package com.swj.prototypealpha.swj.util;

import android.graphics.Bitmap;

public class ItemBean {
    public String title;

    private String content;

    private Bitmap left_image;

    private Bitmap right_image;

    public ItemBean(String title,String content, Bitmap left_image, Bitmap right_image)
    {
        this.content = content;
        this.left_image = left_image;
        this.right_image = right_image;
        this.title = title;
    }

    public Bitmap getLeft_image() {
        return left_image;
    }

    public Bitmap getRight_image() {
        return right_image;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLeft_image(Bitmap left_image) {
        this.left_image = left_image;
    }

    public void setRight_image(Bitmap right_image) {
        this.right_image = right_image;
    }
}
