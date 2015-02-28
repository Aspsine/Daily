package com.aspsine.zhihu.daily.network;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Aspsine on 2015/2/28.
 */
public class Http extends BaseHttp{

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
}
