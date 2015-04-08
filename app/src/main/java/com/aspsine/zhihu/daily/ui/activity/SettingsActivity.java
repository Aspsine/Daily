package com.aspsine.zhihu.daily.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.fragment.SettingsFragment;
import com.aspsine.zhihu.daily.util.L;

public class SettingsActivity extends BaseActionBarActivity {

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i("Life_circle", "SettingsActivity onCreate");
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    protected void onRestart() {
        L.i("Life_circle", "SettingsActivity onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        L.i("Life_circle", "SettingsActivity onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        L.i("Life_circle", "SettingsActivity onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        L.i("Life_circle", "SettingsActivity onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        L.i("Life_circle", "SettingsActivity onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        L.i("Life_circle", "SettingsActivity onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        L.i("Life_circle", "SettingsActivity onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

}
