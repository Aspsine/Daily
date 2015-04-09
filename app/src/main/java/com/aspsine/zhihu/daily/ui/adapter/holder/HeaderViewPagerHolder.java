package com.aspsine.zhihu.daily.ui.adapter.holder;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.ui.widget.CirclePageIndicator;
import com.aspsine.zhihu.daily.ui.widget.MyViewPager;
import com.aspsine.zhihu.daily.ui.widget.StoryHeaderView;
import com.aspsine.zhihu.daily.util.IntentUtils;
import com.aspsine.zhihu.daily.util.ListUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class HeaderViewPagerHolder extends RecyclerView.ViewHolder {
    private static final String TAG = HeaderViewPagerHolder.class.getSimpleName();
    private MyViewPager viewPager;
    private CirclePageIndicator indicator;
    private PagerAdapter mPagerAdapter;

    public HeaderViewPagerHolder(@Nullable View itemView, List<Story> stories) {
        super(itemView);

        viewPager = (MyViewPager) itemView.findViewById(R.id.viewPager);
        indicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
        if (ListUtils.isEmpty(stories)) {
            return;
        } else if (stories.size() < 2) {
            indicator.setVisibility(View.GONE);
        }
        mPagerAdapter = new HeaderPagerAdapter(stories);
    }

    public void bindHeaderView() {
        if (viewPager.getAdapter() == null) {
            viewPager.setAdapter(mPagerAdapter);
            indicator.setViewPager(viewPager);
        } else {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    public boolean isAutoScrolling() {
        if (viewPager != null) {
            return viewPager.isAutoScrolling();
        }
        return false;
    }

    public void stopAutoScroll() {
        if (viewPager != null) {
            viewPager.stopAutoScroll();
        }
    }

    public void startAutoScroll() {
        if (viewPager != null) {
            viewPager.startAutoScroll();
        }
    }

    private final static class HeaderPagerAdapter extends PagerAdapter {
        private List<Story> mStories;
        private DisplayImageOptions mOptions;

        private int mChildCount;

        public HeaderPagerAdapter(List<Story> stories) {
            mStories = stories;
            this.mOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
        }

        @Override
        public int getCount() {
            return mStories == null ? 0 : mStories.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            StoryHeaderView storyHeaderView = StoryHeaderView.newInstance(container);
            final Story story = mStories.get(position);
            storyHeaderView.bindData(story.getTitle(), story.getImageSource(), story.getImage(), mOptions);
            storyHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.intentToStoryActivity((Activity) v.getContext(), story);
                }
            });
            container.addView(storyHeaderView);
            return storyHeaderView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((StoryHeaderView) object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            mChildCount = getCount();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }
}
