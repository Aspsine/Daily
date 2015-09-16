package com.aspsine.zhihu.daily.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.fragment.DailyStoriesFragment;
import com.aspsine.zhihu.daily.ui.fragment.StoryFragment;
import com.aspsine.zhihu.daily.util.L;

public class StoryActivity extends BaseAppCompatActivity {

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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(StoryFragment.TAG);
        if (fm != null && fragment != null) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        L.i("StoryActivity", "onDestroy");
        super.onDestroy();
    }
}
