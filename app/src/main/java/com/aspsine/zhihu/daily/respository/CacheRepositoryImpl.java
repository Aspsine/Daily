package com.aspsine.zhihu.daily.respository;

import android.content.Context;
import android.text.TextUtils;

import com.aspsine.zhihu.daily.db.CacheDao;
import com.aspsine.zhihu.daily.model.Cache;
import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.model.Themes;
import com.aspsine.zhihu.daily.respository.interfaces.CacheRepository;
import com.aspsine.zhihu.daily.util.L;
import com.aspsine.zhihu.daily.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Aspsine on 2015/4/10.
 */
public class CacheRepositoryImpl implements CacheRepository {
    private static final String TAG = CacheRepositoryImpl.class.getSimpleName();
    private static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private Context mContext;
    private CacheDao mCacheDao;
    private Gson mGson;

    public CacheRepositoryImpl(Context context) {
        this.mContext = context;
        this.mCacheDao = new CacheDao(context);
        this.mGson = new Gson();
    }

    @Override
    public void getStartImage(Callback<StartImage> callback) {
        String mOldJsonString = SharedPrefUtils.getSplashJson(mContext);
        if (!TextUtils.isEmpty(mOldJsonString)) {
            StartImage startImage = new Gson().fromJson(mOldJsonString, StartImage.class);
            callback.success(startImage);
        } else {
            callback.failure(getException(StartImage.class));
        }
    }

    @Override
    public void saveStartImage(int width, int height, DisplayImageOptions options, StartImage startImage) {
        String mOldJsonString = SharedPrefUtils.getSplashJson(mContext);
        StartImage old = new Gson().fromJson(mOldJsonString, StartImage.class);
        if (old == null || !startImage.getImg().equals(old.getImg())) {
            SharedPrefUtils.setSplashJson(mContext, new Gson().toJson(startImage));
            ImageLoader.getInstance().loadImage(startImage.getImg(), new ImageSize(width, height), options, null);
            L.i(TAG, "new image.");
        } else {
            L.i(TAG, "old image.");
        }
    }

    @Override
    public void getLatestDailyStories(String url, Callback<DailyStories> callback) {
        getDataObject(url, DailyStories.class, callback);
    }

    @Override
    public void saveLatestDailyStories(DailyStories dailyStories, String url) {
        saveCacheToDB(dailyStories, url);
    }

    @Override
    public void getBeforeDailyStories(String url, Callback<DailyStories> callback) {
        getDataObject(url, DailyStories.class, callback);
    }

    @Override
    public void saveBeforeDailyStories(DailyStories dailyStories, String url) {
        saveCacheToDB(dailyStories, url);
    }

    @Override
    public void getStoryDetail(String url, Callback<Story> callback) {
        getDataObject(url, Story.class, callback);
    }

    @Override
    public void saveStoryDetail(Story story, String url) {
        saveCacheToDB(story, url);
    }

    @Override
    public void getThemes(String url, Callback<Themes> callback) {
        getDataObject(url, Themes.class, callback);
    }

    @Override
    public void saveThemes(Themes themes, String url) {
        saveCacheToDB(themes, url);
    }

    @Override
    public void getTheme(String url, Callback<Theme> callback) {
        getDataObject(url, Theme.class, callback);
    }

    @Override
    public void saveTheme(Theme theme, String url) {
        saveCacheToDB(theme, url);
    }

    @Override
    public void getThemeBeforeStory(String url, Callback<Theme> callback) {
        getDataObject(url, Theme.class, callback);
    }

    @Override
    public void saveThemeBeforeStory(Theme theme, String url) {
        saveCacheToDB(theme, url);
    }

    private <T> void getDataObject(String url, Class<T> classOfT, CacheRepository.Callback callback) {
        String json = mCacheDao.getCache(url).getResponse();
        T t = mGson.fromJson(json, classOfT);
        if (t != null) {
            callback.success(t);
            L.i(TAG, "get" + classOfT.getSimpleName() + " cache");
        } else {
            callback.failure(getException(classOfT));
        }
    }

    private void saveCacheToDB(Object o, String url) {
        Cache cache = new Cache(url, mGson.toJson(o), Long.valueOf(df.format(Calendar.getInstance().getTimeInMillis())));
        mCacheDao.updateCache(cache);
    }

    private Exception getException(Class clazz) {
        return new Exception(clazz.getSimpleName() + " cache not found!");
    }
}
