package com.aspsine.zhihu.daily.network;

import android.os.Build;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.util.L;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Aspsine on 2015/2/28.
 */
public class Http {
    protected static final String CHARSET = Constants.Config.CHARSET;

    public static String get(String baseUrl) throws IOException {
        L.i("url", baseUrl);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            return BaseHttpClient.get(baseUrl);
        } else {
            return BaseHttp.get(baseUrl);
        }
    }

    public static String get(String baseUrl, Map<String, String> params) throws IOException {
        return get(makeUrl(baseUrl, params));
    }

    public static String get(String baseUrl, String key, String value) throws IOException {
        return get(baseUrl + "?" + concatKeyValue(key, value));
    }

    public static String get(String baseUrl, String suffix) throws IOException {
        return get(baseUrl + encodeString(suffix));
    }

    public static String get(String baseUrl, String suffix, boolean replaceSpace) throws IOException {
        if (replaceSpace) {
            return get(baseUrl + encodeString(suffix).replace("+", "%20"));
        } else {
            return get(baseUrl, suffix);
        }
    }


    protected static String makeUrl(String baseUrl, Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return baseUrl;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append("?");

        for (Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator(); iterator.hasNext(); sb.append("&")) {
            Map.Entry<String, String> item = iterator.next();
            sb.append(concatKeyValue(item.getKey(), item.getValue()));
        }

        return sb.toString();
    }

    protected static String concatKeyValue(String key, String value) {
        return encodeString(key) + "=" + encodeString(value);
    }

    protected static String encodeString(String str) {
        try {
            return URLEncoder.encode(str, CHARSET);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
