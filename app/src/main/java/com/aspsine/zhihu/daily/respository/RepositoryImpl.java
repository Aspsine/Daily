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

        //get data form cache
        mCache.getStartImage(new CacheRepository.Callback<StartImage>() {
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
            public void success(StartImage startImage, String url) {
                mCache.saveStartImage(width, height, options, startImage);
            }

            @Override
            public void failure(Exception e, String url) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void getLatestDailyStories(final Callback<DailyStories> callback) {
        //get data form network
        mNet.getLatestDailyStories(new NetRepository.Callback<DailyStories>() {
            @Override
            public void success(DailyStories dailyStories, String url) {
                callback.success(dailyStories, false);
                mCache.saveLatestDailyStories(dailyStories, url);
            }

            @Override
            public void failure(final Exception error, String url) {
                //network failed, get data form cache
                mCache.getLatestDailyStories(url, new CacheRepository.Callback<DailyStories>() {
                    @Override
                    public void success(DailyStories dailyStories) {
                        callback.success(dailyStories, true);
                    }

                    @Override
                    public void failure(Exception e) {
                        callback.failure(error);
                    }
                });
            }
        });
    }


    @Override
    public void getBeforeDailyStories(String date, final Callback<DailyStories> callback) {
        mNet.getBeforeDailyStories(date, new NetRepository.Callback<DailyStories>() {
            @Override
            public void success(DailyStories dailyStories, String url) {
                callback.success(dailyStories, false);
                mCache.saveBeforeDailyStories(dailyStories, url);
            }

            @Override
            public void failure(final Exception error, String url) {
                mCache.getBeforeDailyStories(url, new CacheRepository.Callback<DailyStories>() {
                    @Override
                    public void success(DailyStories dailyStories) {
                        callback.success(dailyStories, false);
                    }

                    @Override
                    public void failure(Exception e) {
                        callback.failure(error);
                    }
                });
            }
        });
    }

    @Override
    public void getStoryDetail(String storyId, final Callback<Story> callback) {
        mNet.getStoryDetail(storyId, new NetRepository.Callback<Story>() {
            @Override
            public void success(Story story, String url) {
                callback.success(story, false);
                mCache.saveStoryDetail(story, url);
            }

            @Override
            public void failure(final Exception error, String url) {
                mCache.getStoryDetail(url, new CacheRepository.Callback<Story>() {
                    @Override
                    public void success(Story story) {
                        callback.success(story, true);
                    }

                    @Override
                    public void failure(Exception e) {
                        callback.failure(error);
                    }
                });
            }
        });
    }

    @Override
    public void getThemes(final Callback<Themes> callback) {
        mNet.getThemes(new NetRepository.Callback<Themes>() {
            @Override
            public void success(Themes themes, String url) {
                callback.success(themes, false);
                mCache.saveThemes(themes, url);
            }

            @Override
            public void failure(final Exception error, String url) {
                mCache.getThemes(url, new CacheRepository.Callback<Themes>() {
                    @Override
                    public void success(Themes themes) {
                        callback.success(themes, false);
                    }

                    @Override
                    public void failure(Exception e) {
                        callback.failure(error);
                    }
                });
            }
        });
    }

    @Override
    public void getTheme(String themeId, final Callback<Theme> callback) {
        mNet.getTheme(themeId, new NetRepository.Callback<Theme>() {
            @Override
            public void success(Theme theme, String url) {
                callback.success(theme, false);
                mCache.saveTheme(theme, url);
            }

            @Override
            public void failure(final Exception error, String url) {
                mCache.getTheme(url, new CacheRepository.Callback<Theme>() {
                    @Override
                    public void success(Theme theme) {
                        callback.success(theme, true);
                    }

                    @Override
                    public void failure(Exception e) {
                        callback.failure(error);
                    }
                });
            }
        });
    }

    @Override
    public void getThemeBeforeStory(String themeId, String storyId, final Callback<Theme> callback) {
        mNet.getThemeBeforeStory(themeId, storyId, new NetRepository.Callback<Theme>() {
            @Override
            public void success(Theme theme, String url) {
                callback.success(theme, false);
                mCache.saveThemeBeforeStory(theme, url);
            }

            @Override
            public void failure(final Exception error, String url) {
                mCache.getThemeBeforeStory(url, new CacheRepository.Callback<Theme>() {
                    @Override
                    public void success(Theme theme) {
                        callback.success(theme, true);
                    }

                    @Override
                    public void failure(Exception e) {
                        callback.failure(error);
                    }
                });
            }
        });
    }
}
