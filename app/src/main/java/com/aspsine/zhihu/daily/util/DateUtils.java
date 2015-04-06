package com.aspsine.zhihu.daily.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CancellationException;

/**
 * Created by Aspsine on 2015/2/28.
 */
public class DateUtils {

    public static String getDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return String.valueOf(simpleDateFormat.format(date));
    }

    public static boolean isToday(String date) {
        String today = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        if (date.equals(today)) {
            return true;
        }
        return false;
    }

    public static String getMainPageDate(String date) {
        String mData = "";
        try {
            Date tmpDate = new SimpleDateFormat("yyyyMMdd").parse(date);
            mData = DateFormat.getDateInstance(DateFormat.FULL).format(tmpDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String thisYear = Calendar.getInstance().get(Calendar.YEAR) + "年";
        if (mData.startsWith(thisYear)) {
            return mData.replace(thisYear, "");
        }
        return mData;
    }


}
