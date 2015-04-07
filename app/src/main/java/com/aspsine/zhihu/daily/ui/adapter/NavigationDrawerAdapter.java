package com.aspsine.zhihu.daily.ui.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.interfaces.NavigationDrawerCallbacks;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.util.L;
import com.aspsine.zhihu.daily.util.UIUtils;

import java.util.List;

/**
 * Created by sf on 2015/1/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = NavigationDrawerAdapter.class.getSimpleName();
    private List<Theme> mThemes;
    private NavigationDrawerCallbacks mCallBacks;
    private int mSelectedPosition = 1;

    public static final class Type {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_MAIN_ITEM = 1;
        public static final int TYPE_ITEM = 2;
    }

    public NavigationDrawerAdapter(List<Theme> themes) {
        mThemes = themes;
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks callbacks) {
        mCallBacks = callbacks;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return Type.TYPE_HEADER;
            case 1:
                return Type.TYPE_MAIN_ITEM;
            default:
                return Type.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mThemes != null ? mThemes.size() + 2 : 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = null;
        switch (viewType) {
            case Type.TYPE_HEADER:
                itemView = UIUtils.inflate(R.layout.nav_drawer_header, viewGroup);
                return new HeaderViewHolder(itemView);
            case Type.TYPE_MAIN_ITEM:
                itemView = UIUtils.inflate(R.layout.nav_drawer_item, viewGroup);
                return new MainItemViewHolder(itemView, mCallBacks);
            case Type.TYPE_ITEM:
                itemView = UIUtils.inflate(R.layout.nav_drawer_item, viewGroup);
                return new ItemViewHolder(itemView, mCallBacks);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case Type.TYPE_HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                bindHeaderData(headerViewHolder, position);
                break;
            case Type.TYPE_MAIN_ITEM:
                MainItemViewHolder mainItemViewHolder = (MainItemViewHolder) viewHolder;
                bindMainItemData(mainItemViewHolder, position);
                break;
            case Type.TYPE_ITEM:
                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                bindItemData(itemViewHolder, position);
                break;
            default:
                throw new IllegalArgumentException(TAG + " error view type!");
        }
    }

    private void bindHeaderData(HeaderViewHolder viewHolder, int position) {
    }

    private void bindMainItemData(MainItemViewHolder viewHolder, int position) {
        Resources resources = viewHolder.itemView.getContext().getResources();
        if (mSelectedPosition == position) {
            L.i(TAG, "selected = " + position);
            viewHolder.itemView.setBackgroundColor(resources.getColor(R.color.navigation_item_selected));
            viewHolder.tvMainItem.setTextColor(resources.getColor(R.color.navdrawer_text_color_selected));
        } else if (position != 0) {
            viewHolder.itemView.setBackgroundColor(android.R.attr.selectableItemBackground);
            viewHolder.tvMainItem.setTextColor(resources.getColor(R.color.navdrawer_text_color));
        }
    }

    private void bindItemData(ItemViewHolder viewHolder, int position) {
        viewHolder.tvItemName.setText(mThemes.get(position - 2).getName());
        Resources resources = viewHolder.itemView.getContext().getResources();
        if (mSelectedPosition == position) {
            L.i(TAG, "selected = " + position);
            viewHolder.itemView.setBackgroundColor(resources.getColor(R.color.navigation_item_selected));
            viewHolder.tvItemName.setTextColor(resources.getColor(R.color.navdrawer_text_color_selected));
        } else if (position != 0) {
            viewHolder.itemView.setBackgroundColor(android.R.attr.selectableItemBackground);
            viewHolder.tvItemName.setTextColor(resources.getColor(R.color.navdrawer_text_color));
        }
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position + 1;

        notifyItemChanged(lastPosition);
        notifyItemChanged(mSelectedPosition);
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
        }
    }

    public static class MainItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvMainItem;
        ImageView imageView;

        public MainItemViewHolder(View itemView, final NavigationDrawerCallbacks callbacks) {
            super(itemView);
            tvMainItem = (TextView) itemView.findViewById(R.id.tvItemName);
            imageView = (ImageView) itemView.findViewById(R.id.ivItemIcon);
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundResource(R.drawable.menu_home);
            tvMainItem.setText(itemView.getResources().getString(R.string.title_activity_main));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callbacks != null) {
                        callbacks.onNavigationDrawerItemSelected(getPosition() - 1);
                    }
                }
            });
        }
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;

        public ItemViewHolder(View itemView, final NavigationDrawerCallbacks callBacks) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBacks != null) {
                        callBacks.onNavigationDrawerItemSelected(getPosition() - 1);
                    }
                }
            });
        }
    }
}
