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
import com.aspsine.zhihu.daily.ui.fragment.BaseSectionFragment;
import com.aspsine.zhihu.daily.ui.fragment.ExploreFragment;
import com.aspsine.zhihu.daily.ui.fragment.NavigationFragment;
import com.aspsine.zhihu.daily.ui.fragment.StoryListFragment;
import com.aspsine.zhihu.daily.util.L;

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

    int lastPosition = 0;

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mTitle = mNavigationFragment.getTitle(position);
        // update the main content by replacing fragments
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment fragment = null;
        Fragment lastFragment = null;

        lastFragment = fm.findFragmentByTag(getTag(lastPosition));

        if (lastFragment != null) {
            L.i(TAG, "last fragment " + lastPosition + " is not null, detach it.");
            ft.detach(lastFragment);
        }

        fragment = fm.findFragmentByTag(getTag(position));

        if (fragment == null) {

            if (mFragments.size() > position) {
                fragment = mFragments.get(position);
                L.i(TAG, "get fragment from cache list " + position);
            }

            if (fragment == null) {
                L.i(TAG, "renew fragment " + position);
                fragment = getFragmentItem(position);
                int j = 0;
                while (mFragments.size() <= position) {
                    mFragments.add(null);
                    j++;
                    L.i(TAG, "fill cache list" + j);
                }
                mFragments.set(position, fragment);
                L.i(TAG, "set fragment to cache list" + position);
            }
            ft.add(R.id.container, fragment, getTag(position));
            L.i(TAG, "ft add fragment" + position);
        } else {
            ft.attach(fragment);
            L.i(TAG, "ft attach fragment" + position);
        }
        ft.commit();
        lastPosition = position;
    }

    private int getId(Fragment fragment) {
        return ((BaseSectionFragment) fragment).getSectionNumber();
    }

    private String getTag(int position) {
        switch (position) {
            case 0:
                return StoryListFragment.TAG;
            default:
                return ExploreFragment.TAG + position;
        }
    }

    private Fragment getFragmentItem(int position) {
        return BaseSectionFragment.newInstance(position, mNavigationFragment.getSectionId(position));
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
