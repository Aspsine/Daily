package com.aspsine.zhihu.daily.respository.interfaces;

import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.model.Themes;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Aspsine on 2015/4/10.
 */
public interface CacheRepository {
    void getStartImage(Callback<StartImage> callback);

    void saveStartImage(int width, int height, DisplayImageOptions options, StartImage startImage);

    void getLatestDailyStories(String url, Callback<DailyStories> callback);

    void saveLatestDailyStories(DailyStories dailyStories, String url);

    void getBeforeDailyStories(String url, Callback<DailyStories> callback);

    void saveBeforeDailyStories(DailyStories dailyStories, String url);

    void getStoryDetail(String url, Callback<Story> callback);

    void saveStoryDetail(Story story, String url);

    void getThemes(String url, Callback<Themes> callback);

    void saveThemes(Themes themes, String url);

    void getTheme(String url, Callback<Theme> callback);

    void saveTheme(Theme theme, String url);

    void getThemeBeforeStory(String url, Callback<Theme> callback);

    void saveThemeBeforeStory(Theme theme, String url);


    public interface Callback<T> {

        /**
         * Successful
         *
         * @param t
         */
        public void success(T t);


        /**
         * Unsuccessful
         *
         * @param e unexpected exception.
         */
        public void failure(Exception e);
    }
}
