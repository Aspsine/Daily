package com.aspsine.zhihu.daily.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.util.UIUtils;

/**
 * Created by Aspsine on 2015/3/24.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    public Toolbar mActionBarToolbar;

    protected abstract int getContentViewLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutId());
        mActionBarToolbar = (Toolbar) findViewById(R.id.actionbarToolbar);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = UIUtils.getStatusBarHeight(this);
            mActionBarToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        setupActionBar();
    }

    private void setupActionBar() {
        setSupportActionBar(mActionBarToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && !(this instanceof NavigationDrawerActivity)) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
