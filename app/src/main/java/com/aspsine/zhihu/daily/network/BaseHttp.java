package com.aspsine.zhihu.daily.network;

import android.util.Log;

import com.aspsine.zhihu.daily.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Aspsine on 2015/2/28.
 */
public class BaseHttp {
    protected static final String CHARSET = "UTF-8";

    protected static String get(String urlAddress) throws IOException {
        if(Constants.Config.DEVELOPER_MODE){
            Log.i("url", urlAddress);
        }

        URL url = new URL(urlAddress);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return response.toString();
            } else {
                throw new IOException("Network Error - response code:" + con.getResponseCode());
            }
        } finally {
            con.disconnect();
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
