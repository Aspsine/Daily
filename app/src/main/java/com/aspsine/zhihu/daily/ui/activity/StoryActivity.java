package com.aspsine.zhihu.daily.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.fragment.ExploreFragment;
import com.aspsine.zhihu.daily.ui.fragment.StoriesFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryActivity extends ActionBarActivity {
    @InjectView(R.id.actionbarToolbar)
    android.support.v7.widget.Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        ButterKnife.inject(this);

        setupActionBar();
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText(getIntent().getStringExtra(StoriesFragment.EXTRA_STORY_ID));
    }

    private void setupActionBar(){
        setSupportActionBar(mActionBarToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
