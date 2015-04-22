package com.aspsine.zhihu.daily.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.respository.RepositoryImpl;
import com.aspsine.zhihu.daily.respository.interfaces.Repository;
import com.aspsine.zhihu.daily.ui.adapter.DailyStoriesAdapter;
import com.aspsine.zhihu.daily.ui.adapter.holder.DateViewHolder;
import com.aspsine.zhihu.daily.ui.widget.LoadMoreRecyclerView;
import com.aspsine.zhihu.daily.ui.widget.MyViewPager;
import com.aspsine.zhihu.daily.util.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyStoriesFragment extends BaseFragment {
    public static final String TAG = DailyStoriesFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private DailyStoriesAdapter mAdapter;
    private LoadMoreRecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    private Repository mRepository;
    private String mDate;

    /**
     * Flag to indicate whither the data is successful loaded
     */
    private boolean isDataLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new DailyStoriesAdapter();
        mRepository = new RepositoryImpl(getActivity());
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

        recyclerView.setOnLoadMoreListener(new LoadMoreRecyclerView.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                changeActionBarTitle(dy);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (!isDataLoaded) {
                    swipeRefreshLayout.setRefreshing(true);
                    refresh();
                }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mRepository = null;
    }

    private String mTitle;
    private int lastTitlePos = -1;

    private void changeActionBarTitle(int dy) {
        int position = mLayoutManager.findFirstVisibleItemPosition();
        if (lastTitlePos == position) {
            return;
        }
        DailyStoriesAdapter.Item item = mAdapter.getItem(position);
        int type = item.getType();
        if (type == DailyStoriesAdapter.Type.TYPE_HEADER) {
            mTitle = getString(R.string.title_activity_main);
        } else if (dy > 0 && type == DailyStoriesAdapter.Type.TYPE_DATE) {
            mTitle = DateViewHolder.getDate(item.getDate(), getActivity());
        } else if (dy < 0) {
            mTitle = DateViewHolder.getDate(mAdapter.getTitleBeforePosition(position), getActivity());
        }
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
        lastTitlePos = position;
    }

    private void refresh() {
        isDataLoaded = false;

        mRepository.getLatestDailyStories(new Repository.Callback<DailyStories>() {
            @Override
            public void success(DailyStories dailyStories, boolean outDate) {
                isDataLoaded = true;
                swipeRefreshLayout.setRefreshing(false);
                mDate = dailyStories.getDate();
                mAdapter.setList(dailyStories);
            }

            @Override
            public void failure(Exception e) {
                isDataLoaded = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "refresh error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void loadMore() {
        recyclerView.setLoadingMore(true);

        mRepository.getBeforeDailyStories(mDate, new Repository.Callback<DailyStories>() {
            @Override
            public void success(DailyStories dailyStories, boolean outDate) {
                mDate = dailyStories.getDate();
                recyclerView.setLoadingMore(false);
                mAdapter.appendList(dailyStories);
            }

            @Override
            public void failure(Exception e) {
                recyclerView.setLoadingMore(false);
                Toast.makeText(getActivity(), "load more error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}
