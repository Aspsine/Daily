package com.aspsine.zhihu.daily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.model.DailyStories;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.ui.adapter.holder.DateViewHolder;
import com.aspsine.zhihu.daily.ui.adapter.holder.HeaderViewPagerHolder;
import com.aspsine.zhihu.daily.ui.adapter.holder.StoryViewHolder;
import com.aspsine.zhihu.daily.util.UIUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class DailyStoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = DailyStoriesAdapter.class.getSimpleName();
    protected List<Item> mItems;
    protected List<Item> mTmpItem;

    public class Type {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_DATE = 1;
        public static final int TYPE_STORY = 2;
    }

    public DailyStoriesAdapter() {
        mItems = new ArrayList<Item>();
        mTmpItem = new ArrayList<Item>();
    }

    public void setList(DailyStories dailyStories) {
        mItems.clear();
        appendList(dailyStories);
    }

    public void appendList(DailyStories dailyStories) {
        int positionStart = mItems.size();

        if (positionStart == 0) {
            Item headerItem = new Item();
            headerItem.setType(Type.TYPE_HEADER);
            headerItem.setStories(dailyStories.getTopStories());
            mItems.add(headerItem);
        }
        Item dateItem = new Item();
        dateItem.setType(Type.TYPE_DATE);
        dateItem.setDate(dailyStories.getDate());
        mItems.add(dateItem);
        List<Story> stories = dailyStories.getStories();
        for (int i = 0, num = stories.size(); i < num; i++) {
            Item storyItem = new Item();
            storyItem.setType(Type.TYPE_STORY);
            storyItem.setStory(stories.get(i));
            mItems.add(storyItem);
        }

        int itemCount = mItems.size() - positionStart;

        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }


    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof HeaderViewPagerHolder) {
            HeaderViewPagerHolder headerHolder = (HeaderViewPagerHolder) holder;
            if (headerHolder.isAutoScrolling()) {
                headerHolder.stopAutoScroll();
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof HeaderViewPagerHolder) {
            HeaderViewPagerHolder headerHolder = (HeaderViewPagerHolder) holder;
            if (!headerHolder.isAutoScrolling()) {
                headerHolder.startAutoScroll();
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case Type.TYPE_HEADER:
                itemView = UIUtils.inflate(R.layout.recycler_header_viewpager, parent);
                return new HeaderViewPagerHolder(itemView);
            case Type.TYPE_DATE:
                itemView = UIUtils.inflate(R.layout.recycler_item_date, parent);
                return new DateViewHolder(itemView);
            case Type.TYPE_STORY:
                itemView = UIUtils.inflate(R.layout.recycler_item_story, parent);
                return new StoryViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Item item = mItems.get(position);
        switch (viewType) {
            case Type.TYPE_HEADER:
                ((HeaderViewPagerHolder) holder).bindHeaderView(item.getStories());
                break;
            case Type.TYPE_DATE:
                ((DateViewHolder) holder).bindDateView(item.getDate());
                break;
            case Type.TYPE_STORY:
                ((StoryViewHolder) holder).bindStoryView(item.getStory());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }

    public String getTitleBeforePosition(int position) {
        mTmpItem.clear();
        //subList [0 , position)
        mTmpItem.addAll(mItems.subList(0, position + 1));
        Collections.reverse(mTmpItem);
        for (Item item : mTmpItem) {
            if (item.getType() == Type.TYPE_DATE) {
                return item.getDate();
            }
        }
        //L.i(TAG, "POSITION = " + position);
        return "";
    }

    public static class Item {
        private int type;
        private String date;
        private Story story;
        private List<Story> stories;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Story getStory() {
            return story;
        }

        public void setStory(Story story) {
            this.story = story;
        }

        public List<Story> getStories() {
            return stories;
        }

        public void setStories(List<Story> stories) {
            this.stories = stories;
        }
    }
}
