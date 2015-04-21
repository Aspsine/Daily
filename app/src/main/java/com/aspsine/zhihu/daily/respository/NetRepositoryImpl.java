package com.aspsine.zhihu.daily.respository;

import com.aspsine.zhihu.daily.api.DailyApi;
import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.model.Themes;
import com.aspsine.zhihu.daily.respository.interfaces.NetRepository;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Aspsine on 2015/4/21.
 */
public class NetRepositoryImpl implements NetRepository {

    @Override
    public void getStartImage(int width, int height, final Callback<StartImage> callback) {
        DailyApi.createApi().getStartImage(width, height, new retrofit.Callback<StartImage>() {
            @Override
            public void success(StartImage startImage, Response response) {
                callback.success(startImage);

            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(new Exception(error.getCause()));
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
