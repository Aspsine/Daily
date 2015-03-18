package com.aspsine.zhihu.daily;

/**
 * Created by Aspsine on 2015/2/28.
 */
public class Constants {

    public static class Url {

        /**
         * add width*height in pixel;
         */
        public static final String ZHIHU_DAILY_SPLASH = "http://news-at.zhihu.com/api/4/start-image/";

        public static final String ZHIHU_DAILY_LATEST = "http://daily.zhihu.com/api/4/news/latest";

        /**
         * add yyyymmdd;
         */
        public static final String ZHIHU_DAILY_BEFORE = "http://daily.zhihu.com/api/4/news/before/";

        /**
         * add ga_prefix
         */
        public static final String ZHIHU_DAILY_DETAIL = "http://daily.zhihu.com/api/3/news/";
    }

    public static class Config {
        public static final boolean DEVELOPER_MODE = true;
    }
}
