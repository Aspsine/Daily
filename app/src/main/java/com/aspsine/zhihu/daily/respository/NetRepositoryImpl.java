package com.aspsine.zhihu.daily.respository;

import com.aspsine.zhihu.daily.api.DailyApi;
import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.model.Themes;
import com.aspsine.zhihu.daily.respository.interfaces.NetRepository;
import com.aspsine.zhihu.daily.util.L;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Aspsine on 2015/4/21.
 */
public class NetRepositoryImpl implements NetRepository {
    private static final String TAG = NetRepositoryImpl.class.getSimpleName();

    @Override
    public void getStartImage(int width, int height, final Callback<StartImage> callback) {
        DailyApi.createApi().getStartImage(width, height, new retrofit.Callback<StartImage>() {
            @Override
            public void success(StartImage startImage, Response response) {
                callback.success(startImage, response.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error, error.getUrl());
            }
        });
    }

    @Override
    public void getLatestDailyStories(final Callback<DailyStories> callback) {
        DailyApi.createApi().getLatestDailyStories(new retrofit.Callback<DailyStories>() {
            @Override
            public void success(DailyStories dailyStories, Response response) {
                callback.success(dailyStories, response.getUrl());
                L.i(TAG, "getLatestDailyStories net");
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error, error.getUrl());
            }
        });
    }

    @Override
    public void getBeforeDailyStories(String date, final Callback<DailyStories> callback) {
        DailyApi.createApi().getBeforeDailyStories(date, new retrofit.Callback<DailyStories>() {
            @Override
            public void success(DailyStories dailyStories, Response response) {
                callback.success(dailyStories, response.getUrl());
                L.i(TAG, "getBeforeDailyStories net");
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error, error.getUrl());
            }
        });
    }

    @Override
    public void getStoryDetail(String storyId, final Callback<Story> callback) {
        DailyApi.createApi().getStoryDetail(storyId, new retrofit.Callback<Story>() {
            @Override
            public void success(Story story, Response response) {
                callback.success(story, response.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error, error.getUrl());
            }
        });
    }

    @Override
    public void getThemes(final Callback<Themes> callback) {
        DailyApi.createApi().getThemes(new retrofit.Callback<Themes>() {
            @Override
            public void success(Themes themes, Response response) {
                callback.success(themes, response.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error, error.getUrl());
            }
        });
    }

    @Override
    public void getTheme(String themeId, final Callback<Theme> callback) {
        DailyApi.createApi().getTheme(themeId, new retrofit.Callback<Theme>() {
            @Override
            public void success(Theme theme, Response response) {
                callback.success(theme, response.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error, error.getUrl());
            }
        });
    }

    @Override
    public void getThemeBeforeStory(String themeId, String storyId, final Callback<Theme> callback) {
        DailyApi.createApi().getThemeBeforeStory(themeId, storyId, new retrofit.Callback<Theme>() {
            @Override
            public void success(Theme theme, Response response) {
                callback.success(theme, response.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error, error.getUrl());
            }
        });
    }
}
