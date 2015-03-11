package com.aspsine.zhihu.daily.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.adapter.StoryListAdapter;
import com.aspsine.zhihu.daily.entity.DailyStories;
import com.aspsine.zhihu.daily.entity.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryListFragment extends PlaceholderFragment {


    public StoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DailyStories dailyStories = new DailyStories();
        List<Story> stopStories = new ArrayList<Story>();
        List<Story> stories = new ArrayList<Story>();
        for (int i = 0; i < 10; i++) {
            Story story = new Story();
            story.setTitle(i + "");
            story.setThumbnail("");
            stopStories.add(story);
            stories.add(story);
        }
        dailyStories.setStories(stories);
        dailyStories.setTopStories(stopStories);

        recyclerView.setAdapter(new StoryListAdapter(dailyStories));
    }
}
