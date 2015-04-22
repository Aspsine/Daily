package com.aspsine.zhihu.daily.network;

import android.content.Context;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.util.FileUtils;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by Aspsine on 2015/3/31.
 */
public class OkHttp {
    private static OkHttpClient okHttpClient;

    public static OkHttpClient createHttpClient(Context context) {
        if (okHttpClient == null) {
            synchronized (OkHttp.class) {
                okHttpClient = new OkHttpClient();
                okHttpClient.setCache(new Cache(FileUtils.getHttpCacheDir(context), Constants.Config.HTTP_CACHE_SIZE));
                okHttpClient.setConnectTimeout(Constants.Config.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                okHttpClient.setReadTimeout(Constants.Config.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
                okHttpClient.networkInterceptors().add(new CacheInterceptor());
            }
        }
        return okHttpClient;
    }
}
