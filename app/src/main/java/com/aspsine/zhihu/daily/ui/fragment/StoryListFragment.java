package com.aspsine.zhihu.daily.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.aspsine.zhihu.daily.util.L;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryListFragment extends BaseSectionFragment {
    public static final String TAG = StoryListFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private StoryListAdapter mAdapter;
    RecyclerView recyclerView;
    private DailyStories mDailyStories;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance(int sectionNumber) {
        Fragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
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
        L.i(TAG, "onDestroyView");
        if (recyclerView != null) {
            L.i(TAG, "recyclerView != null");
            View view = recyclerView.findViewById(R.id.viewPager);
            if (view != null) {
                L.i(TAG, "MyViewPager stopAutoScroll");
                ((MyViewPager) view).stopAutoScroll();
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
            try {
                String response = Http.get(Constants.Url.ZHIHU_DAILY_LATEST);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                DailyStories dailyStories = gson.fromJson(response, DailyStories.class);
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
                mDailyStories.getTopStories().clear();
                mDailyStories.getTopStories().addAll(tmpDailyStories.getTopStories());
                mDailyStories.getStories().clear();
                mDailyStories.getStories().addAll(tmpDailyStories.getStories());
                mAdapter.notifyDataSetChanged();
            }
        }
    };

}
