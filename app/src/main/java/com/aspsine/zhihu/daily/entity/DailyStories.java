package com.aspsine.zhihu.daily.entity;

import java.util.List;

/**
 * Created by Aspsine on 2015/3/10.
 */
public class DailyStories {
    private String date;
    private List<Story> stories;
    private List<Story> topStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<Story> topStories) {
        this.topStories = topStories;
    }
}
