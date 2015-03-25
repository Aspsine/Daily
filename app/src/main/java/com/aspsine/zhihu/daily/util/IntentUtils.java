package com.aspsine.zhihu.daily.util;

import android.app.Activity;
import android.content.Intent;

import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.ui.activity.StoryActivity;

/**
 * Created by Aspsine on 2015/3/20.
 */
public class IntentUtils {
    public static final String EXTRA_STORY_ID = "extra_story_id";

    public static final void IntentToStoryActivity(Activity activity, Story story) {
        Intent intent = new Intent(activity, StoryActivity.class);
        intent.putExtra(EXTRA_STORY_ID, String.valueOf(story.getId()));
        activity.startActivity(intent);
    }

}
