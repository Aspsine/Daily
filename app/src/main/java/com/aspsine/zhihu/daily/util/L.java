package com.aspsine.zhihu.daily.util;

import android.util.Log;

import com.aspsine.zhihu.daily.Constants;

import java.util.logging.Logger;

/**
 * Created by aspsine on 15-3-13.
 */
public class L{

    public static int i(String tag, String msg){
        if(Constants.Config.DEVELOPER_MODE){
            return Log.i(tag, msg);
        }
        return -1;
    }

    public static int e(String tag, String msg){
        if(Constants.Config.DEVELOPER_MODE){
            return Log.e(tag, msg);
        }
        return -1;
    }
}
