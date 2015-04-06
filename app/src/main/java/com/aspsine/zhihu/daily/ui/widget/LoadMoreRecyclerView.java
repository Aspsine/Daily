package com.aspsine.zhihu.daily.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.aspsine.zhihu.daily.util.L;

/**
 * Created by Aspsine on 2015/3/20.
 * <p/>
 * be careful! current version just support LinearLayoutManager
 * <p/>
 * Version 1.0 beta
 */
public class LoadMoreRecyclerView extends RecyclerView {
    private static final String TAG = LoadMoreRecyclerView.class.getSimpleName();
    private boolean mIsLoadingMore = false;
    private onLoadMoreListener mListener;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void init() {

        this.setOnScrollListener(new onLoadMoreScrollListener());
    }

    public static class onLoadMoreScrollListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LoadMoreRecyclerView view = (LoadMoreRecyclerView) recyclerView;
            onLoadMoreListener onLoadMoreListener = view.getOnLoadMoreListener();

            onLoadMoreListener.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager layoutManager = (LinearLayoutManager) view.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            int itemCount = layoutManager.getItemCount();
            if (lastVisibleItem >= itemCount - 1 && !view.getLoadingMore()) {
                onLoadMoreListener.onLoadMore();
                L.i(TAG, "load more: lastVisibleItem = " + lastVisibleItem + ", itemCount" + itemCount);
            } else {
                super.onScrolled(recyclerView, dx, dy);
            }
        }


    }


    public void setOnLoadMoreListener(onLoadMoreListener listener) {
        this.mListener = listener;
        init();
    }

    public onLoadMoreListener getOnLoadMoreListener() {
        return this.mListener;
    }

    public void setLoadingMore(boolean isLoading) {
        mIsLoadingMore = isLoading;
    }

    public boolean getLoadingMore() {
        return mIsLoadingMore;
    }

    public interface onLoadMoreListener {
        public void onLoadMore();

        public void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof LinearLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException("LoadMoreRecyclerView must have a LinearLayoutManager!");
        }
    }
}
