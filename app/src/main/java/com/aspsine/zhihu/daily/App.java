package com.aspsine.zhihu.daily;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by sf on 2015/1/12.
 * <p/>
 * Application of the app
 */
public class App extends Application {
    private static Context applicationContext;

    @Override
    public void onCreate() {

        setStrictMode();

        super.onCreate();

        applicationContext = getApplicationContext();

        CrashHandler.getInstance(getApplicationContext());

        initImageLoader(getApplicationContext());
    }

    private void setStrictMode(){
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.enableDefaults();
        }
    }

    private void initImageLoader(Context context){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(Constants.Config.IMAGE_CACHE_SIZE) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    };

    public static Context getContext() {
        return applicationContext;
    }
}
