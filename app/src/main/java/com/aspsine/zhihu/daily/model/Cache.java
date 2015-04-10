package com.aspsine.zhihu.daily.model;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by Aspsine on 2015/4/10.
 */
public class Cache {
    private long id;
    @Expose
    private String request;
    @Expose
    private String response;
    @Expose
    private Date time;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
