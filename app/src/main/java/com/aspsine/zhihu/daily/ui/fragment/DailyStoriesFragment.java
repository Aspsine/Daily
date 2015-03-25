package com.aspsine.zhihu.daily.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.DailyStories;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.ui.adapter.StoryListAdapter;
import com.aspsine.zhihu.daily.ui.widget.LoadMoreRecyclerView;
import com.aspsine.zhihu.daily.ui.widget.MyViewPager;
import com.aspsine.zhihu.daily.util.L;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyStoriesFragment extends BaseFragment {
    public static final String TAG = DailyStoriesFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private StoryListAdapter mAdapter;
    private LoadMoreRecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DailyStories mDailyStories;

    private String mDate;


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
        return inflater.inflate(R.layout.fragment_daily_stories, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        recyclerView.setonLoadMoreListener(new LoadMoreRecyclerView.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
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
    public void onResume() {
        L.i(TAG, "onResume");
        super.onResume();
        if (recyclerView != null) {
            L.i(TAG, "recyclerView != null");
            View view = recyclerView.findViewById(R.id.viewPager);
            if (view != null) {
                L.i(TAG, "MyViewPager startAutoScroll");
                ((MyViewPager) view).startAutoScroll();
            }
        }
    }

    @Override
    public void onPause() {
        L.i(TAG, "onPause");
        super.onPause();
        if (recyclerView != null) {
            L.i(TAG, "recyclerView != null");
            View view = recyclerView.findViewById(R.id.viewPager);
            if (view != null) {
                L.i(TAG, "MyViewPager stopAutoScroll");
                ((MyViewPager) view).stopAutoScroll();
            }
        }
    }

    private void refresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                new Thread(new GetDailyStoryTask(GetDailyStoryTask.TYPE_REFRESH)).start();
            }
        });
    }

    private void loadMore() {
        recyclerView.setLoadingMore(true);
        new Thread(new GetDailyStoryTask(GetDailyStoryTask.TYPE_LOAD_MORE)).start();
    }

    private class GetDailyStoryTask implements Runnable {
        private int type;
        public static final int TYPE_REFRESH = 0;
        public static final int TYPE_LOAD_MORE = 1;

        public static final int TYPE_REFRESH_ERROR = -1;
        public static final int TYPE_LOAD_MORE_ERROR = -2;

        public GetDailyStoryTask(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            switch (type) {
                case TYPE_REFRESH:
                    runRefresh();
                    break;
                case TYPE_LOAD_MORE:
                    runLoadMore();
                    break;
            }
        }

        private void runRefresh() {
            try {
                String response = Http.get(Constants.Url.ZHIHU_DAILY_LATEST);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                DailyStories dailyStories = gson.fromJson(response, DailyStories.class);
                handler.obtainMessage(type, dailyStories).sendToTarget();
            } catch (Exception e) {
                handler.obtainMessage(TYPE_REFRESH_ERROR).sendToTarget();
                e.printStackTrace();
            }
        }

        private void runLoadMore() {
            try {
                String response = Http.get(Constants.Url.ZHIHU_DAILY_BEFORE, mDate);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                DailyStories dailyStories = gson.fromJson(response, DailyStories.class);
                handler.obtainMessage(type, dailyStories).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
                handler.obtainMessage(TYPE_LOAD_MORE_ERROR).sendToTarget();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GetDailyStoryTask.TYPE_LOAD_MORE_ERROR:
                    recyclerView.setLoadingMore(false);
                    Toast.makeText(getActivity(), "load more error", Toast.LENGTH_SHORT).show();
                    break;

                case GetDailyStoryTask.TYPE_REFRESH_ERROR:
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "refresh error", Toast.LENGTH_SHORT).show();
                    break;

                case GetDailyStoryTask.TYPE_REFRESH:
                    swipeRefreshLayout.setRefreshing(false);
                    DailyStories tmpDailyStories = (DailyStories) msg.obj;
                    mDate = tmpDailyStories.getDate();
                    mDailyStories.setDate(tmpDailyStories.getDate());
                    mDailyStories.getTopStories().clear();
                    mDailyStories.getTopStories().addAll(tmpDailyStories.getTopStories());
                    mDailyStories.getStories().clear();
                    mDailyStories.getStories().addAll(tmpDailyStories.getStories());
                    mAdapter.notifyDataSetChanged();
                    break;

                case GetDailyStoryTask.TYPE_LOAD_MORE:
                    recyclerView.setLoadingMore(false);
                    DailyStories beforeDailyStories = (DailyStories) msg.obj;
                    mDate = beforeDailyStories.getDate();
                    mDailyStories.getStories().addAll(beforeDailyStories.getStories());
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

}
