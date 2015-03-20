package com.aspsine.zhihu.daily.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Theme;
import com.aspsine.zhihu.daily.interfaces.NavigationDrawerCallbacks;

import java.util.List;

/**
 * Created by sf on 2015/1/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {
    private List<Theme> mThemes;
    private NavigationDrawerCallbacks mCallBacks;
    private int mSelectedPosition;
    private int mTouchedPosition = -1;


    public NavigationDrawerAdapter(List<Theme> themes) {
        mThemes = themes;
    }

    public NavigationDrawerCallbacks getNavigationDrawerCallbacks() {
        return mCallBacks;
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks callbacks) {
        mCallBacks = callbacks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_drawer_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Theme navItem = mThemes.get(i);
        viewHolder.tvItemName.setText(navItem.getName());
//        viewHolder.tvItemName.setCompoundDrawablesWithIntrinsicBounds(navItem.getDrawable(), null, null, null);

        viewHolder.itemView.setOnTouchListener(
                new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                touchPosition(i);
                                return false;
                            case MotionEvent.ACTION_CANCEL:
                                touchPosition(-1);
                                return false;
                            case MotionEvent.ACTION_MOVE:
                                return false;
                            case MotionEvent.ACTION_UP:
                                touchPosition(-1);
                                return false;
                        }
                        return true;
                    }
                }
        );

        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mCallBacks != null) {
                            mCallBacks.onNavigationDrawerItemSelected(i);
                        }
                    }
                }
        );

        //TODO: selected menu position, change layout accordingly
        if (mSelectedPosition == i || mTouchedPosition == i) {
            // viewHolder.itemView.getContext().getResources().getColor(R.color.navigation_item_selected)
            viewHolder.itemView.setBackgroundColor(Color.GRAY);
        } else {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    @Override
    public int getItemCount() {
        return mThemes != null ? mThemes.size() : 0;
    }

    private void touchPosition(int position) {
        int lastPosition = mTouchedPosition;
        mTouchedPosition = position;
        if (lastPosition >= 0) notifyItemChanged(lastPosition);
        if (position >= 0) notifyItemChanged(position);
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
        }
    }
}
