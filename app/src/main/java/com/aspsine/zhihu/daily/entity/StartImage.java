package com.aspsine.zhihu.daily.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Aspsine on 2015/3/30.
 */
public class StartImage {
    @Expose
    private String text;
    @Expose
    private String img;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isValueEquals(StartImage startImage) {
        return this.text.equals(startImage.getText()) && this.img.equals(startImage.getImg());
    }
}
