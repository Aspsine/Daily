package com.aspsine.zhihu.daily.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.zhihu.daily.App;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.respository.interfaces.Repository;
import com.aspsine.zhihu.daily.ui.adapter.ThemeStoriesAdapter;
import com.aspsine.zhihu.daily.ui.widget.LoadMoreRecyclerView;
import com.aspsine.zhihu.daily.util.L;

/**
 * Created by Aspsine on 2015/3/25.
 */
public class ThemeStoriesFragment extends BaseFragment {
    public static final String TAG = ThemeStoriesFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;

    private LoadMoreRecyclerView recyclerView;

    private ThemeStoriesAdapter mAdapter;

    private String mThemeId;

    private String mLastStoryId;

    /**
     * Flag to indicate whither the data is successful loaded
     */
    private boolean isDataLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mAdapter = new ThemeStoriesAdapter();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.i(TAG, getThemeNumber() + " : " + getThemeId());
        return inflater.inflate(R.layout.fragment_theme_stories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // TODO adapter
        recyclerView.setAdapter(mAdapter);
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
                recyclerView.setLoadingMore(true);
                loadMore();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mThemeId = getThemeId();
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
    public void onDetach() {
        super.onDetach();
    }

    private void refresh() {
        isDataLoaded = false;

        App.getRepository().getTheme(mThemeId, new Repository.Callback<Theme>() {
            @Override
            public void success(Theme theme, boolean outDate) {
                isDataLoaded = true;
                swipeRefreshLayout.setRefreshing(false);
                if (theme != null && mAdapter != null) {
                    if (theme.getStories().size() > 0) {
                        mLastStoryId = theme.getStories().get(theme.getStories().size() - 1).getId();
                    }
                    L.i(TAG, "last story id: " + mLastStoryId);
                    mAdapter.setTheme(theme);
                }
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

        App.getRepository().getThemeBeforeStory(mThemeId, mLastStoryId, new Repository.Callback<Theme>() {
            @Override
            public void success(Theme theme, boolean outDate) {
                recyclerView.setLoadingMore(false);
                if (theme != null && mAdapter != null) {
                    if (theme.getStories().size() > 0) {
                        mLastStoryId = theme.getStories().get(theme.getStories().size() - 1).getId();
                        mAdapter.appendStories(theme.getStories());
                    }
                }
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
