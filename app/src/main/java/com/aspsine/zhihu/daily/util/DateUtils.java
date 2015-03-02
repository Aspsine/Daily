package com.aspsine.zhihu.daily.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aspsine on 2015/2/28.
 */
public class DateUtils {

    public static String getDate(Date date, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return String.valueOf(simpleDateFormat.format(date));
    }

}
