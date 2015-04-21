package com.aspsine.zhihu.daily.respository;

import android.content.Context;

import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.model.Themes;
import com.aspsine.zhihu.daily.respository.interfaces.CacheRepository;
import com.aspsine.zhihu.daily.respository.interfaces.NetRepository;
import com.aspsine.zhihu.daily.respository.interfaces.Repository;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Aspsine on 2015/4/21.
 */
public class RepositoryImpl implements Repository {
    private static final String TAG = RepositoryImpl.class.getSimpleName();

    private Context mContext;

    private CacheRepository mCache;
    private NetRepository mNet;


    public RepositoryImpl(Context context) {
        mContext = context;
        mCache = new CacheRepositoryImpl(context);
        mNet = new NetRepositoryImpl();
    }

    /**
     * get start image
     *
     * @param width
     * @param height
     * @param callback
     */
    @Override
    public void getStartImage(final int width, final int height, final DisplayImageOptions options, final Callback<StartImage> callback) {

        //get data form db
        mCache.getStartImage(width, height, new CacheRepository.Callback<StartImage>() {
            @Override
            public void success(StartImage startImage) {
                callback.success(startImage, true);
            }

            @Override
            public void failure(Exception e) {
                callback.failure(e);
            }
        });

        //get data form network
        mNet.getStartImage(width, height, new NetRepository.Callback<StartImage>() {
            @Override
            public void success(StartImage startImage) {
                mCache.saveStartImage(width, height, options, startImage);
            }

            @Override
            public void failure(Exception e) {
                e.printStackTrace();
            }
        });
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
