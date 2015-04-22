package com.aspsine.zhihu.daily.model;

import com.google.gson.annotations.Expose;

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
    private long time;

    public Cache() {
    }

    public Cache(String request, String response, long time) {
        this.request = request;
        this.response = response;
        this.time = time;
    }

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
