package com.aspsine.zhihu.daily.ui.activity;

import android.os.Bundle;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.fragment.SettingsFragment;

public class SettingsActivity extends BaseActionBarActivity {

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment())
                    .commit();
        }
    }
}
