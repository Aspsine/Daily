package com.aspsine.zhihu.daily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.adapter.holder.DateViewHolder;
import com.aspsine.zhihu.daily.adapter.holder.HeaderViewPagerHolder;
import com.aspsine.zhihu.daily.adapter.holder.StoryViewHolder;
import com.aspsine.zhihu.daily.entity.DailyStories;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.util.UIUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class StoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DailyStories mDailyStories;
    private DisplayImageOptions mOptions;

    protected static class Type {
        private static final int ITEM_HEADER = -1;
        private static final int ITEM_DATE = 0;
        private static final int ITEM_NORMAL = 1;
    }

    public StoryListAdapter(DailyStories dailyStories) {
        this.mDailyStories = dailyStories;
        this.mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case Type.ITEM_HEADER:
                itemView = UIUtils.inflate(R.layout.recycler_header_viewpager, parent);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case Type.ITEM_HEADER:
                ((HeaderViewPagerHolder) holder).bindHeaderView(mDailyStories.getTopStories());
                break;
            case Type.ITEM_DATE:
                bindDateView((DateViewHolder) holder, position);
                break;
            case Type.ITEM_NORMAL:
                bindStoryView((StoryViewHolder) holder, position);
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

    private void bindStoryView(StoryViewHolder holder, int position) {
        Story story = mDailyStories.getStories().get(position - 1);
        holder.text.setText(String.valueOf(story.getTitle()));
        ImageLoader.getInstance().displayImage(story.getImages().get(0), holder.image, mOptions);
    }
}
