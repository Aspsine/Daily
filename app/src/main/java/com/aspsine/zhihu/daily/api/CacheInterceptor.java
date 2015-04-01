package com.aspsine.zhihu.daily.api;

import com.aspsine.zhihu.daily.App;
import com.aspsine.zhihu.daily.util.NetWorkUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Aspsine on 2015/4/1.
 */
public class CacheInterceptor implements Interceptor {
    private static final String CACHE_CONTROL = "Cache-Control";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        // Add Cache Control only for GET methods
        if (request.method().equals("GET")) {
            if (NetWorkUtils.isNetWorkAvailable(App.getContext())) {
                request.newBuilder().addHeader(CACHE_CONTROL, "only-if-cached").build();
            } else {
                request.newBuilder().addHeader(CACHE_CONTROL, "public, max-stale=2419200").build();
            }
        }
        Response response = chain.proceed(request);
        // Re-write response CC header to force use of cache
        return response.newBuilder()
                .header("Cache-Control", "public, max-age=" + 60 * 60 * 3) // 3h
                .build();
    }
}
