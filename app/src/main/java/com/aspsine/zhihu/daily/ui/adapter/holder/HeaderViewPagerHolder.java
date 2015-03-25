package com.aspsine.zhihu.daily.ui.adapter.holder;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.ui.widget.CirclePageIndicator;
import com.aspsine.zhihu.daily.ui.widget.MyViewPager;
import com.aspsine.zhihu.daily.ui.widget.StoryHeaderView;
import com.aspsine.zhihu.daily.util.IntentUtils;
import com.aspsine.zhihu.daily.util.L;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class HeaderViewPagerHolder extends RecyclerView.ViewHolder {
    private static final String TAG = HeaderViewPagerHolder.class.getSimpleName();
    private List<Story> mStories;
    private MyViewPager viewPager;
    private CirclePageIndicator indicator;
    private PagerAdapter mPagerAdapter;

    public HeaderViewPagerHolder(@Nullable View itemView) {
        super(itemView);
        // TODO findViewById
        viewPager = (MyViewPager) itemView.findViewById(R.id.viewPager);
        indicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
    }

    public void bindHeaderView(List<Story> stories) {
        mStories = stories;
        if (mStories == null || mStories.size() == 0) {
            return;
        } else if (mStories.size() == 1) {
            indicator.setVisibility(View.GONE);
        }

        if (viewPager.getAdapter() == null) {
            L.i(TAG, "mPagerAdapter == null");
            mPagerAdapter = new HeaderPagerAdapter(mStories);
            viewPager.setAdapter(mPagerAdapter);
            indicator.setViewPager(viewPager);
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
        private List<Story> mmStories;
        private DisplayImageOptions mOptions;

        public HeaderPagerAdapter(List<Story> stories) {
            mmStories = stories;
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
        public int getCount() {
            return mmStories == null ? 0 : mmStories.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            StoryHeaderView storyHeaderView = StoryHeaderView.newInstance(container);
            final Story story = mmStories.get(position);
            storyHeaderView.BindData(story.getTitle(), story.getImage(), mOptions);
            storyHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.IntentToStoryActivity((Activity) v.getContext(), story);
                }
            });
            container.addView(storyHeaderView);
            return storyHeaderView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((StoryHeaderView) object);
        }
    }
}
