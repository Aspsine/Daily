package com.aspsine.zhihu.daily.entity;

import java.util.List;

/**
 * Created by Aspsine on 2015/2/27.
 */
public class Story {
    private List<String> images;
    private String image;
    private String thumbnail;
    private String type;
    private String id;
    private String gaPrefix;
    private String title;
    private String shareUrl;
    private String url;
    private String body;
    private String imageSource;
    private String sectionThumbnail;
    private String sectionId;
    private List<String> jsList;
    private String sectionName;
    private List<String> cssList;


    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGaPrefix() {
        return gaPrefix;
    }

    public void setGaPrefix(String gaPrefix) {
        this.gaPrefix = gaPrefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getSectionThumbnail() {
        return sectionThumbnail;
    }

    public void setSectionThumbnail(String sectionThumbnail) {
        this.sectionThumbnail = sectionThumbnail;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public List<String> getJsList() {
        return jsList;
    }

    public void setJsList(List<String> jsList) {
        this.jsList = jsList;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<String> getCssList() {
        return cssList;
    }

    public void setCssList(List<String> cssList) {
        this.cssList = cssList;
    }
}
