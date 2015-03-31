package com.aspsine.zhihu.daily.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Aspsine on 2015/3/31.
 */
public class FileUtils {

    private static final String HTTP_CACHE_DIR = "http";

    public static final File getHttpCacheDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir(), HTTP_CACHE_DIR);
        }
        return new File(context.getCacheDir(), HTTP_CACHE_DIR);
    }
}
