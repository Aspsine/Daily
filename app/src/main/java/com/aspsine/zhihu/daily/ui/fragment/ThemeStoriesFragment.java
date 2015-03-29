package com.aspsine.zhihu.daily.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Theme;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.ui.adapter.ThemeStoriesAdapter;
import com.aspsine.zhihu.daily.ui.widget.LoadMoreRecyclerView;
import com.aspsine.zhihu.daily.util.L;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

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

        recyclerView.setonLoadMoreListener(new LoadMoreRecyclerView.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.setLoadingMore(true);
                loadMore();
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mThemeId = getThemeId();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });
    }

    private void refresh() {
        new Thread(new GetThemeTask(GetThemeTask.TYPE_REFRESH, mThemeId))
                .start();
    }

    private void loadMore() {
        new Thread(new GetThemeTask(GetThemeTask.TYPE_LOAD_MORE, mThemeId, mLastStoryId))
                .start();
    }

    private final class GetThemeTask implements Runnable {
        private int type;
        private String id;
        private String lastStoryId;
        public static final int TYPE_REFRESH = 0;
        public static final int TYPE_LOAD_MORE = 1;

        public static final int TYPE_REFRESH_ERROR = -1;
        public static final int TYPE_LOAD_MORE_ERROR = -2;


        public GetThemeTask(int type, String themeId) {
            this(type, themeId, null);
        }

        public GetThemeTask(int type, String themeId, String storyId) {
            this.type = type;
            this.id = themeId;
            this.lastStoryId = storyId;
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
                String jsonStr = Http.get(Constants.Url.ZHIHU_DAILY_THEME, id);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                Theme theme = gson.fromJson(jsonStr, Theme.class);
                handler.obtainMessage(type, theme).sendToTarget();
            } catch (IOException e) {
                handler.obtainMessage(GetThemeTask.TYPE_REFRESH_ERROR).sendToTarget();
                e.printStackTrace();
            }

        }

        private void runLoadMore() {
            // TODO loadMore
            String url = Constants.Url.ZHIHU_DAILY_THEME + id + "/before/";
            try {
                String jsonStr = Http.get(url, lastStoryId);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                Theme theme = gson.fromJson(jsonStr, Theme.class);
                handler.obtainMessage(type, theme).sendToTarget();
            } catch (Exception e) {
                handler.obtainMessage(GetThemeTask.TYPE_LOAD_MORE_ERROR).sendToTarget();
                e.printStackTrace();
            }
        }

    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GetThemeTask.TYPE_REFRESH:
                    swipeRefreshLayout.setRefreshing(false);
                    if (msg.obj != null && mAdapter != null) {
                        Theme theme = (Theme) msg.obj;
                        if(theme.getStories().size()>0){
                            mLastStoryId = theme.getStories().get(theme.getStories().size() - 1).getId();
                        }
                        L.i(TAG, "last story id: " + mLastStoryId);
                        mAdapter.setTheme(theme);
                    }
                    break;
                case GetThemeTask.TYPE_LOAD_MORE:
                    recyclerView.setLoadingMore(false);
                    if (msg.obj != null && mAdapter != null) {
                        Theme theme = (Theme) msg.obj;
                        if(theme.getStories().size()>0){
                            mLastStoryId = theme.getStories().get(theme.getStories().size() - 1).getId();
                            mAdapter.appendStories(theme.getStories());
                        }
                    }
                    break;
                case GetThemeTask.TYPE_REFRESH_ERROR:
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "refresh error", Toast.LENGTH_SHORT).show();
                    break;
                case GetThemeTask.TYPE_LOAD_MORE_ERROR:
                    recyclerView.setLoadingMore(false);
                    Toast.makeText(getActivity(), "load more error", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
}
