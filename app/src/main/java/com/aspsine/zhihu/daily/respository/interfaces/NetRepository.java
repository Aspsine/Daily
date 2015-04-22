package com.aspsine.zhihu.daily.respository.interfaces;

import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.model.Themes;

/**
 * Created by Aspsine on 2015/4/21.
 */
public interface NetRepository {
    void getStartImage(int width, int height, Callback<StartImage> callback);

    void getLatestDailyStories(Callback<DailyStories> callback);

    void getBeforeDailyStories(String date, Callback<DailyStories> callback);

    void getStoryDetail(String storyId, Callback<Story> callback);

    void getThemes(Callback<Themes> callback);

    void getTheme(String themeId, Callback<Theme> callback);

    void getThemeBeforeStory(String themeId, String storyId, Callback<Theme> callback);

    public interface Callback<T> {

        /**
         * Successful
         *
         * @param t
         * @param url
         */
        public void success(T t, String url);


        /**
         * Unsuccessful
         *
         * @param e unexpected exception.
         */
        public void failure(Exception e, String url);
    }
}
