package com.aspsine.zhihu.daily.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    RecyclerView recyclerView;
    private DailyStories mDailyStories;

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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new Thread(new MyRunnable()).start();
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

    @Override
    public void onDestroy() {

        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
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
//                    try{
//                        story.setThumbnail(String.valueOf(storyObject.getString("thumbnail")));
//                    }catch (Exception e){
//                        Log.i("TAG", story.getTitle());
//                    }
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
            if (msg.what == 0) {
                mDailyStories = (DailyStories) msg.obj;
                Log.i("TAG", mDailyStories.getTopStories().get(0).getTitle());
                recyclerView.setAdapter(new StoryListAdapter(mDailyStories));
            }
        }
    };

}
