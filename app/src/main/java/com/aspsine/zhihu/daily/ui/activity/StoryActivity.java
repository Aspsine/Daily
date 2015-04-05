package com.aspsine.zhihu.daily.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.fragment.DailyStoriesFragment;
import com.aspsine.zhihu.daily.ui.fragment.StoryFragment;
import com.aspsine.zhihu.daily.util.L;

public class StoryActivity extends BaseActionBarActivity {

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_story;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(null);
        if (savedInstanceState == null) {
            String storyId = getIntent().getStringExtra(DailyStoriesFragment.EXTRA_STORY_ID);
            StoryFragment storyFragment = StoryFragment.newInstance(storyId);
            storyFragment.setToolBar(mActionBarToolbar);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, storyFragment, StoryFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        FragmentManager fm;
        Fragment fragment;
        if ((fm = getSupportFragmentManager()) != null && (fragment = fm.findFragmentByTag(StoryFragment.TAG)) != null) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
        if(mActionBarToolbar != null){
            mActionBarToolbar.getBackground().setAlpha(255);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        L.i("StoryActivity", "onDestroy");
        super.onDestroy();
    }
}
