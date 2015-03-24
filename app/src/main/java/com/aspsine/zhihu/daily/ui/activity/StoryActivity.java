package com.aspsine.zhihu.daily.ui.activity;

import android.os.Bundle;
import android.view.Menu;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.fragment.StoriesFragment;
import com.aspsine.zhihu.daily.ui.fragment.StoryFragment;

public class StoryActivity extends BaseActionBarActivity {

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_story;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            String storyId = getIntent().getStringExtra(StoriesFragment.EXTRA_STORY_ID);
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
}
