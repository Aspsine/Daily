package com.aspsine.zhihu.daily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Theme;
import com.aspsine.zhihu.daily.ui.adapter.holder.StoryViewHolder;
import com.aspsine.zhihu.daily.ui.widget.StoryHeaderView;
import com.aspsine.zhihu.daily.util.UIUtils;

/**
 * Created by Aspsine on 2015/3/26.
 */
public class ThemeStoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Theme mTheme;

    public ThemeStoriesAdapter() {
    }

    public void setTheme(Theme theme) {
        mTheme = theme;
        notifyDataSetChanged();
    }

    public static final class Type {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_AVATAR = 1;
        public static final int TYPE_ITEM = 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(mTheme.getBackground()) && position == 0) {
            return Type.TYPE_HEADER;
        } else if (mTheme.getEditors() != null && mTheme.getEditors().size() > 0 && position == 2) {
            return Type.TYPE_AVATAR;
        } else {
            return Type.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mTheme != null) {
            if (!TextUtils.isEmpty(mTheme.getBackground())) {
                count += 1;
            }
            if (mTheme.getEditors() != null && mTheme.getEditors().size() > 0) {
                count += 1;
            }
            if (mTheme.getStories() != null) {
                count = count + mTheme.getStories().size();
            }
        }
        return count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case Type.TYPE_HEADER:
                viewHolder = new HeaderViewHolder(StoryHeaderView.newInstance(parent));
                break;
            case Type.TYPE_AVATAR:
                viewHolder = new AvatarViewHolder(UIUtils.inflate(R.layout.recycler_item_avatar, parent));
                break;
            case Type.TYPE_ITEM:
                viewHolder = new StoryViewHolder(UIUtils.inflate(R.layout.recycler_item_story, parent));
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case Type.TYPE_HEADER:

                break;
            case Type.TYPE_AVATAR:

                break;
            case Type.TYPE_ITEM:
                StoryViewHolder storyViewHolder = (StoryViewHolder) holder;
                storyViewHolder.bindStoryView(mTheme.getStories(), position);
                break;
            default:
                throw new IllegalArgumentException("error view type!");
        }
    }

    public static final class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static final class AvatarViewHolder extends RecyclerView.ViewHolder {

        public AvatarViewHolder(View itemView) {
            super(itemView);
        }
    }


}
