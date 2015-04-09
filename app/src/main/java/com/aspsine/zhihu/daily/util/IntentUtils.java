package com.aspsine.zhihu.daily.util;

import android.app.Activity;
import android.content.Intent;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.ui.activity.NavigationDrawerActivity;
import com.aspsine.zhihu.daily.ui.activity.StoryActivity;

/**
 * Created by Aspsine on 2015/3/20.
 */
public class IntentUtils {
    public static final String EXTRA_STORY_ID = "extra_story_id";

    public static final void intentToMainActivity(Activity activity) {
        Intent intent = new Intent(activity, NavigationDrawerActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
    }

    public static final void intentToStoryActivity(Activity activity, Story story) {
        Intent intent = new Intent(activity, StoryActivity.class);
        intent.putExtra(EXTRA_STORY_ID, String.valueOf(story.getId()));
        activity.startActivity(intent);
    }

    public static final void share(Activity activity, Story story) {
        StringBuilder sb = new StringBuilder();
        sb.append(story.getTitle()).append(" ")
                .append(activity.getString(R.string.share_link))
                .append(story.getShareUrl())
                .append(" ")
                .append(activity.getString(R.string.share_from));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, story.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        activity.startActivity(intent);
    }

}
