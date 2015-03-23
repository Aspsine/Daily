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
import com.aspsine.zhihu.daily.ui.fragment.ExploreFragment;
import com.aspsine.zhihu.daily.ui.fragment.NavigationDrawerFragment;
import com.aspsine.zhihu.daily.ui.fragment.NavigationFragment;
import com.aspsine.zhihu.daily.ui.fragment.StoryListFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NavigationDrawerActivity extends ActionBarActivity implements NavigationDrawerCallbacks {
    @InjectView(R.id.actionbarToolbar)
    Toolbar mActionBarToolbar;

    private NavigationFragment mNavigationDrawerFragment;

    private CharSequence mTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        ButterKnife.inject(this);
        setUpDrawer();
        if (savedInstanceState == null) {
            mNavigationDrawerFragment.selectItem(NavigationDrawerFragment.getDefaultNavDrawerItem());
        }
    }

    private void setUpDrawer() {
        setSupportActionBar(mActionBarToolbar);
        mNavigationDrawerFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setup(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mActionBarToolbar);
    }

    Fragment lastFragment = null;

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mTitle = mNavigationDrawerFragment.getTitle(position);
        // update the main content by replacing fragments
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(getTag(position));

        if (lastFragment != null) {
            ft.detach(lastFragment);
        }

        if (fragment != null) {
            ft.attach(fragment);
        } else {
            fragment = getFragmentItem(position);
            ft.add(R.id.container, fragment, getTag(position));
        }
        ft.commit();
        lastFragment = fragment;
    }

    private String getTag(int position) {
        switch (position) {
            case 0:
                return StoryListFragment.TAG;
            default:
                return ExploreFragment.TAG;
        }
    }

    private Fragment getFragmentItem(int position) {
        switch (position) {
            case 0:
                return StoryListFragment.newInstance(position);
            default:
                return ExploreFragment.newInstance(position);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_navigation_drawer, menu);
            restoreActionBar();
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
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else {
            if (mNavigationDrawerFragment.getCurrentSelectedPosition() != NavigationDrawerFragment.getDefaultNavDrawerItem()) {
                mNavigationDrawerFragment.onNavigationDrawerItemSelected(NavigationDrawerFragment.getDefaultNavDrawerItem());
            } else {
                super.onBackPressed();
            }
        }
    }
}
