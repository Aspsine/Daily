package com.aspsine.zhihu.daily.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aspsine on 2015/2/28.
 */
public class DateUtils {
    public static String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return String.valueOf(simpleDateFormat.format(new Date()));
    }
}
