package com.aspsine.zhihu.daily.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aspsine on 2015/2/28.
 */
public class BaseHttp {

    protected static String get(String urlAddress) throws IOException {
        URL url = new URL(urlAddress);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Http.CHARSET));
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
}
