package com.aspsine.zhihu.daily.api;

import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.model.Themes;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Aspsine on 2015/3/30.
 */
public interface DailyService {

    @GET("/start-image/{width}*{height}")
    void getStartImage(@Path("width") int width, @Path("height") int height, Callback<StartImage> callback);

    @GET("/news/latest")
    void getLatestDailyStories(Callback<DailyStories> callback);

    @GET("/news/before/{date}")
    void getBeforeDailyStories(@Path("date") String date, Callback<DailyStories> callback);

    @GET("/news/{storyId}")
    void getStoryDetail(@Path("storyId") String storyId, Callback<Story> callback);

    @GET("/themes")
    void getThemes(Callback<Themes> callback);

    @GET("/theme/{themeId}")
    void getTheme(@Path("themeId") String themeId, Callback<Theme> callback);

    @GET("/theme/{themeId}/before/{storyId}")
    void getThemeBeforeStory(@Path("themeId") String themeId, @Path("storyId") String storyId, Callback<Theme> callback);
}
