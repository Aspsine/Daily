package com.aspsine.zhihu.daily.ui.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.interfaces.NavigationDrawerCallbacks;
import com.aspsine.zhihu.daily.model.Theme;
import com.aspsine.zhihu.daily.util.L;
import com.aspsine.zhihu.daily.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sf on 2015/1/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = NavigationDrawerAdapter.class.getSimpleName();
    private List<Theme> mThemes;
    private NavigationDrawerCallbacks mCallBacks;
    private boolean mIsKitKatWithNavigation;
    private int mSelectedPosition = -1;

    public static final class Type {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_ITEM = 1;
        public static final int TYPE_BOTTOM_SPACE = 2;
    }

    public NavigationDrawerAdapter(boolean isKitKatWithNavigation) {
        mThemes = new ArrayList<Theme>();
        mIsKitKatWithNavigation = isKitKatWithNavigation;
    }

    public void setThemes(List<Theme> themes) {
        mThemes.clear();
        mThemes.addAll(themes);
        notifyDataSetChanged();
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks callbacks) {
        mCallBacks = callbacks;
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsKitKatWithNavigation) {
            if (position == 0) {
                return Type.TYPE_HEADER;
            } else if (position == mThemes.size() + 2) {
                return Type.TYPE_BOTTOM_SPACE;
            } else {
                return Type.TYPE_ITEM;
            }
        } else {
            return position == 0 ? Type.TYPE_HEADER : Type.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (mIsKitKatWithNavigation) {
            return mThemes != null ? mThemes.size() + 3 : 3;
        } else {
            return mThemes != null ? mThemes.size() + 2 : 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = null;
        switch (viewType) {
            case Type.TYPE_HEADER:
                itemView = UIUtils.inflate(R.layout.nav_drawer_header, viewGroup);
                return new HeaderViewHolder(itemView);
            case Type.TYPE_ITEM:
                itemView = UIUtils.inflate(R.layout.nav_drawer_item, viewGroup);
                final ItemViewHolder holder = new ItemViewHolder(itemView, mCallBacks);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPosition(holder.getPosition() - 1);
                    }
                });
                return holder;
            case Type.TYPE_BOTTOM_SPACE:
                View view = new View(viewGroup.getContext());
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getNavigationBarHeight(viewGroup.getContext())));
                UIUtils.setAccessibilityIgnore(view);
                return new BottomViewHolder(view);
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
            case Type.TYPE_ITEM:
                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                bindItemData(itemViewHolder, position);
                break;
            case Type.TYPE_BOTTOM_SPACE:
                break;
            default:
                throw new IllegalArgumentException(TAG + " error view type!");
        }
    }

    public void selectPosition(int position) {
        int realPosition = position + 1;
        int lastPosition = mSelectedPosition;

        if (lastPosition != -1 && lastPosition != realPosition) {
            notifyItemChanged(lastPosition);
        }

        if (mSelectedPosition != realPosition) {
            mSelectedPosition = realPosition;
            notifyItemChanged(mSelectedPosition);
        }

        if (mCallBacks != null) {
            mCallBacks.onNavigationDrawerItemSelected(position);
        }
    }

    private void bindHeaderData(HeaderViewHolder viewHolder, int position) {
    }

    private void bindItemData(ItemViewHolder viewHolder, int position) {
        Resources resources = viewHolder.itemView.getContext().getResources();
        if (position == 1) {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setBackgroundDrawable(resources.getDrawable(R.drawable.menu_home));
            viewHolder.tvItemName.setText(resources.getString(R.string.title_activity_main));
        } else {
            viewHolder.imageView.setBackgroundDrawable(null);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.tvItemName.setText(mThemes.get(position - 2).getName());
        }

        if (mSelectedPosition == position) {
            L.i(TAG, "selected item = " + position);
            viewHolder.itemView.setBackgroundColor(resources.getColor(R.color.navigation_item_selected));
            viewHolder.tvItemName.setTextColor(resources.getColor(R.color.navdrawer_text_color_selected));
        } else if (position != 0) {
            viewHolder.itemView.setBackgroundColor(android.R.attr.selectableItemBackground);
            viewHolder.tvItemName.setTextColor(resources.getColor(R.color.navdrawer_text_color));
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        ImageView imageView;

        public ItemViewHolder(View itemView, final NavigationDrawerCallbacks callBacks) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            imageView = (ImageView) itemView.findViewById(R.id.ivItemIcon);
        }
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        public BottomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
