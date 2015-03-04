package com.aspsine.zhihu.daily.network;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Aspsine on 2015/3/3.
 */
public class BaseHttpClient {

    protected static String get(String urlAddress) throws IOException {
        HttpGet httpGet = new HttpGet(urlAddress);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpGet);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream inputStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Http.CHARSET));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            inputStream.close();
            reader.close();
            return response.toString();
        }
        throw new IOException("");
    }
}
