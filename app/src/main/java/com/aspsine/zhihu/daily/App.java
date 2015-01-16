package com.aspsine.zhihu.daily;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by sf on 2015/1/12.
 *
 * Application of the app
 */
public class App extends Application{
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init(){
        sContext = getApplicationContext();
    }

    public static Context getContext(){
        return sContext;
    }

    public static Resources getResource(){
        return sContext.getResources();
    }

}
