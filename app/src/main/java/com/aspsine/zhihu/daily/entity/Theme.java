package com.aspsine.zhihu.daily.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Aspsine on 2015/3/19.
 */
public class Theme {
    @Expose
    private String color;
    @Expose
    private String thumbnail;
    @Expose
    private String description;
    @Expose
    private String id;
    @Expose
    private String name;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
