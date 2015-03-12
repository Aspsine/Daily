package com.aspsine.zhihu.daily.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.adapter.StoryListAdapter;
import com.aspsine.zhihu.daily.entity.DailyStories;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.ui.widget.MyViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryListFragment extends PlaceholderFragment {
    private static final String TAG = StoryListFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private StoryListAdapter mAdapter;
    RecyclerView recyclerView;
    private DailyStories mDailyStories;

    public StoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDailyStories = new DailyStories();
        mDailyStories.setStories(new ArrayList<Story>());
        mDailyStories.setTopStories(new ArrayList<Story>());
        mAdapter = new StoryListAdapter(mDailyStories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(mAdapter);
        refresh();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy");
        if (recyclerView != null) {
            View view = recyclerView.findViewById(R.id.viewPager);
            if (view != null) {
                ((MyViewPager) view).destroyView();
            }
        }

        super.onDestroyView();
        recyclerView = null;
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Thread(new MyRunnable()).start();
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            DailyStories dailyStories = new DailyStories();
            List<Story> topStories = new ArrayList<Story>();
            List<Story> stories = new ArrayList<Story>();
            try {
                JSONObject jsonObject = new JSONObject(Http.get(Constants.Url.ZHIHU_DAILY_LATEST));
                dailyStories.setDate(String.valueOf(jsonObject.getString("date")));
                dailyStories.setDisplayDate(String.valueOf(jsonObject.getString("display_date")));

                JSONArray topStoryArray = jsonObject.getJSONArray("top_stories");

                for (int i = 0, size = topStoryArray.length(); i < size; i++) {
                    JSONObject storyObject = topStoryArray.getJSONObject(i);
                    Story story = new Story();
                    story.setId(String.valueOf(storyObject.getString("id")));
                    story.setTitle(String.valueOf(storyObject.getString("title")));
//                  story.setThumbnail(String.valueOf(storyObject.getString("thumbnail")));
                    story.setImage(String.valueOf(storyObject.getString("image")));
                    story.setGaPrefix(String.valueOf(storyObject.getString("ga_prefix")));
                    story.setShareUrl(String.valueOf(storyObject.getString("share_url")));
                    story.setUrl(String.valueOf(storyObject.getString("url")));
                    topStories.add(story);
                }
                dailyStories.setTopStories(topStories);

                JSONArray storyArray = jsonObject.getJSONArray("news");
                for (int i = 0, size = storyArray.length(); i < size; i++) {
                    JSONObject storyObject = storyArray.getJSONObject(i);
                    Story story = new Story();
                    story.setId(String.valueOf(storyObject.getString("id")));
                    story.setTitle(String.valueOf(storyObject.getString("title")));
                    try {
                        story.setThumbnail(String.valueOf(storyObject.getString("thumbnail")));
                    } catch (Exception e) {
                        Log.i("TAG", story.getTitle());
                    }
                    story.setImage(String.valueOf(storyObject.getString("image")));
                    story.setGaPrefix(String.valueOf(storyObject.getString("ga_prefix")));
                    story.setShareUrl(String.valueOf(storyObject.getString("share_url")));
                    story.setUrl(String.valueOf(storyObject.getString("url")));
                    stories.add(story);
                }

                dailyStories.setStories(stories);
                handler.obtainMessage(0, dailyStories).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
            if (msg.what == 0) {
                DailyStories tmpDailyStories = (DailyStories) msg.obj;
                mDailyStories.setDate(tmpDailyStories.getDate());
                mDailyStories.setDisplayDate(tmpDailyStories.getDisplayDate());
                mDailyStories.getTopStories().clear();
                mDailyStories.getTopStories().addAll(tmpDailyStories.getTopStories());
                mDailyStories.getStories().clear();
                mDailyStories.getStories().addAll(tmpDailyStories.getStories());
                mAdapter.notifyDataSetChanged();
            }
        }
    };

}
