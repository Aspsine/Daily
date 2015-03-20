package com.aspsine.zhihu.daily.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.aspsine.zhihu.daily.util.L;

/**
 * Created by Aspsine on 2015/3/20.
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

        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!(getLayoutManager() instanceof LinearLayoutManager)) {
                    return;
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                int itemCount = layoutManager.getItemCount();
                if (lastVisibleItem >= itemCount - 1 && !mIsLoadingMore) {
                    mListener.onLoadMore();
                    L.i(TAG, "load more: lastVisibleItem = " + lastVisibleItem + ", itemCount" + itemCount);
                } else {
                    super.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    public void setonLoadMoreListener(onLoadMoreListener listener) {
        this.mListener = listener;
        init();
    }

    public void setLoadingMore(boolean isLoading) {
        mIsLoadingMore = isLoading;
    }

    public interface onLoadMoreListener {
        public void onLoadMore();
    }
}
