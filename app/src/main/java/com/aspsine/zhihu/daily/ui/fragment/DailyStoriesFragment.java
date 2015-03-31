package com.aspsine.zhihu.daily.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.api.DailyApi;
import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.ui.adapter.DailyStoriesAdapter;
import com.aspsine.zhihu.daily.ui.widget.LoadMoreRecyclerView;
import com.aspsine.zhihu.daily.ui.widget.MyViewPager;
import com.aspsine.zhihu.daily.util.L;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyStoriesFragment extends BaseFragment {
    public static final String TAG = DailyStoriesFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private DailyStoriesAdapter mAdapter;
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
        mAdapter = new DailyStoriesAdapter(mDailyStories);
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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });
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
        DailyApi.createApi(getActivity().getApplicationContext()).getLatestDailyStories(new Callback<DailyStories>() {
            @Override
            public void success(DailyStories dailyStories, Response response) {
                swipeRefreshLayout.setRefreshing(false);
                mDate = dailyStories.getDate();
                mDailyStories.setDate(dailyStories.getDate());
                mDailyStories.getTopStories().clear();
                mDailyStories.getTopStories().addAll(dailyStories.getTopStories());
                mDailyStories.getStories().clear();
                mDailyStories.getStories().addAll(dailyStories.getStories());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "refresh error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    private void loadMore() {
        recyclerView.setLoadingMore(true);
        DailyApi.createApi(getActivity().getApplicationContext()).getBeforeDailyStories(mDate, new Callback<DailyStories>() {
            @Override
            public void success(DailyStories dailyStories, Response response) {
                recyclerView.setLoadingMore(false);
                mDate = dailyStories.getDate();
                mDailyStories.getStories().addAll(dailyStories.getStories());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                recyclerView.setLoadingMore(false);
                Toast.makeText(getActivity(), "load more error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

}
