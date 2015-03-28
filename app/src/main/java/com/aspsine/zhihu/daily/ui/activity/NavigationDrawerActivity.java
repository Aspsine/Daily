package com.aspsine.zhihu.daily.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.interfaces.NavigationDrawerCallbacks;
import com.aspsine.zhihu.daily.ui.fragment.BaseFragment;
import com.aspsine.zhihu.daily.ui.fragment.DailyStoriesFragment;
import com.aspsine.zhihu.daily.ui.fragment.NavigationFragment;
import com.aspsine.zhihu.daily.ui.fragment.ThemeStoriesFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NavigationDrawerActivity extends ActionBarActivity implements NavigationDrawerCallbacks {
    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();
    @InjectView(R.id.actionbarToolbar)
    Toolbar mActionBarToolbar;

    private NavigationFragment mNavigationFragment;

    private List<Fragment> mFragments = new ArrayList<>();

    private CharSequence mTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        ButterKnife.inject(this);
        setUpDrawer();
        if (savedInstanceState == null) {
            mNavigationFragment.selectItem(NavigationFragment.getDefaultNavDrawerItem());
        }
    }

    private void setUpDrawer() {
        setSupportActionBar(mActionBarToolbar);
        mNavigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationFragment.setup(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mActionBarToolbar);
    }

    private int lastPosition = 0;

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment lastFragment = fm.findFragmentByTag(getTag(lastPosition));

        if (lastFragment != null) {
            ft.detach(lastFragment);
        }

        Fragment fragment = fm.findFragmentByTag(getTag(position));

        if (fragment == null) {

            if (mFragments.size() > position) {
                fragment = mFragments.get(position);
            }

            if (fragment == null) {
                fragment = getFragmentItem(position);
                while (mFragments.size() <= position) {
                    mFragments.add(null);
                }
                mFragments.set(position, fragment);
            }
            ft.add(R.id.container, fragment, getTag(position));

        } else {
            ft.attach(fragment);
        }

        ft.commit();
        lastPosition = position;

        mTitle = mNavigationFragment.getTitle(position);
        restoreActionBar();
    }

    private int getId(Fragment fragment) {
        return ((BaseFragment) fragment).getThemeNumber();
    }

    private String getTag(int position) {
        switch (position) {
            case 0:
                return DailyStoriesFragment.TAG;
            default:
                return ThemeStoriesFragment.TAG + position;
        }
    }

    private Fragment getFragmentItem(int position) {
        return BaseFragment.newInstance(position, mNavigationFragment.getSectionId(position));
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_navigation_drawer, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationFragment.isDrawerOpen()) {
            mNavigationFragment.closeDrawer();
        } else {
            if (mNavigationFragment.getCurrentSelectedPosition() != NavigationFragment.getDefaultNavDrawerItem()) {
                mNavigationFragment.onNavigationDrawerItemSelected(NavigationFragment.getDefaultNavDrawerItem());
            } else {
                super.onBackPressed();
            }
        }
    }
}
