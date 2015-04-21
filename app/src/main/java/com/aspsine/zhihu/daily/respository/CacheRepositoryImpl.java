package com.aspsine.zhihu.daily.respository;

import android.content.Context;
import android.text.TextUtils;

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

/**
 * Created by Aspsine on 2015/4/10.
 */
public class CacheRepositoryImpl implements CacheRepository {
    private static final String TAG = CacheRepositoryImpl.class.getSimpleName();
    private Context mContext;

    public CacheRepositoryImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void getStartImage(int width, int height, Callback<StartImage> callback) {
        String mOldJsonString = SharedPrefUtils.getSplashJson(mContext);
        if (!TextUtils.isEmpty(mOldJsonString)) {
            StartImage startImage = new Gson().fromJson(mOldJsonString, StartImage.class);
            callback.success(startImage);
        } else {
            callback.failure(new Exception("no cache found!"));
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
    public void getLatestDailyStories(Callback<DailyStories> callback) {
    }

    @Override
    public void getBeforeDailyStories(String date, Callback<DailyStories> callback) {

    }

    @Override
    public void getStoryDetail(String storyId, Callback<Story> callback) {

    }

    @Override
    public void getThemes(Callback<Themes> callback) {

    }

    @Override
    public void getTheme(String themeId, Callback<Theme> callback) {

    }

    @Override
    public void getThemeBeforeStory(String themeId, String storyId, Callback<Theme> callback) {

    }
}
