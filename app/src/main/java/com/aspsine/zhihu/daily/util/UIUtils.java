package com.aspsine.zhihu.daily.util;

import android.os.Build;
import android.view.View;

/**
 * Created by Aspsine on 2015/2/25.
 */
public class UIUtils {
    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }
}
