package com.aspsine.zhihu.daily.api;

import com.aspsine.zhihu.daily.App;
import com.aspsine.zhihu.daily.BuildConfig;
import com.aspsine.zhihu.daily.network.OkHttp;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Aspsine on 2015/4/1.
 */
public class RestApi {

    private static RestAdapter restAdapter;

    public static <T> T createApi(Class<T> clazz, String api) {
        if (restAdapter == null) {
            synchronized (RestApi.class) {
                if (restAdapter == null) {
                    restAdapter = new RestAdapter.Builder()
                            .setEndpoint(api)
                            .setClient(new OkClient(OkHttp.createHttpClient(App.getContext())))
                            .setConverter(new GsonConverter(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                            .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                            .build();
                }
            }
        }
        return restAdapter.create(clazz);
    }
}
