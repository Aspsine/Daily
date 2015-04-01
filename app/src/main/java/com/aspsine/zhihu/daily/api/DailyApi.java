package com.aspsine.zhihu.daily.api;

/**
 * http://blog.robinchutaux.com/blog/using-retrofit-with-activeandroid/
 * http://mattlogan.me/creating-a-retrofitlike-database-client
 * Created by Aspsine on 2015/3/30.
 */
public class DailyApi {

    private static final String API = "http://news.at.zhihu.com/api/4";

    private static DailyApiService dailyApiService;

    public static DailyApiService createApi() {
        if (dailyApiService == null) {
            synchronized (DailyApi.class) {
                if (dailyApiService == null) {
                    dailyApiService = RestApi.createApi(DailyApiService.class, API);
                }
            }
        }
        return dailyApiService;
    }

}
