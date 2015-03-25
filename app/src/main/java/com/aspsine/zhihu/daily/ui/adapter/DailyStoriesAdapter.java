package com.aspsine.zhihu.daily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.DailyStories;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.ui.adapter.holder.DateViewHolder;
import com.aspsine.zhihu.daily.ui.adapter.holder.HeaderViewPagerHolder;
import com.aspsine.zhihu.daily.ui.adapter.holder.StoryViewHolder;
import com.aspsine.zhihu.daily.util.L;
import com.aspsine.zhihu.daily.util.UIUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class DailyStoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = DailyStoriesAdapter.class.getSimpleName();
    private DailyStories mDailyStories;
    private DisplayImageOptions mOptions;

    protected static class Type {
        private static final int ITEM_HEADER = -1;
        private static final int ITEM_DATE = 0;
        private static final int ITEM_NORMAL = 1;
    }

    public DailyStoriesAdapter(DailyStories dailyStories) {
        this.mDailyStories = dailyStories;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case Type.ITEM_HEADER:
                itemView = UIUtils.inflate(R.layout.recycler_header_viewpager, parent);
                Log.i(TAG, "onCreateViewHolder: ITEM_HEADER");
                return new HeaderViewPagerHolder(itemView);
            case Type.ITEM_DATE:
                itemView = UIUtils.inflate(R.layout.recycler_item_date, parent);
                return new DateViewHolder(itemView);
            case Type.ITEM_NORMAL:
                itemView = UIUtils.inflate(R.layout.recycler_item_story, parent);
                return new StoryViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof HeaderViewPagerHolder) {
            L.i(TAG, "onViewDetachedFromWindow: ITEM_HEADER");
            HeaderViewPagerHolder headerHolder = (HeaderViewPagerHolder) holder;
            if (headerHolder.isAutoScrolling()) {
                headerHolder.stopAutoScroll();
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof HeaderViewPagerHolder) {
            L.i(TAG, "onViewAttachedToWindow: ITEM_HEADER");
            HeaderViewPagerHolder headerHolder = (HeaderViewPagerHolder) holder;
            if (!headerHolder.isAutoScrolling()) {
                headerHolder.startAutoScroll();
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case Type.ITEM_HEADER:
                Log.i(TAG, "onBindViewHolder: ITEM_HEADER");
                ((HeaderViewPagerHolder) holder).bindHeaderView(mDailyStories.getTopStories());
                break;
            case Type.ITEM_DATE:
                bindDateView((DateViewHolder) holder, position);
                break;
            case Type.ITEM_NORMAL:
                ((StoryViewHolder) holder).bindStoryView(mDailyStories.getStories());
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        if (mDailyStories != null) {
            List<Story> topStories = mDailyStories.getStories();
            List<Story> stories = mDailyStories.getStories();
            return (topStories == null || topStories.size() == 0 ? 0 : 1) + (stories == null ? 0 : stories.size());
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? Type.ITEM_HEADER : Type.ITEM_NORMAL;
    }

    private void bindDateView(DateViewHolder holder, int position) {

    }


}
