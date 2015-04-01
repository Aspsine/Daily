package com.aspsine.zhihu.daily.api;

/**
 * http://blog.robinchutaux.com/blog/using-retrofit-with-activeandroid/
 * https://gist.github.com/polbins/1c7f9303d2b7d169a3b1
 * http://mattlogan.me/creating-a-retrofitlike-database-client
 * https://github.com/square/okhttp/wiki/Interceptors
 * https://github.com/square/retrofit/issues/693
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
