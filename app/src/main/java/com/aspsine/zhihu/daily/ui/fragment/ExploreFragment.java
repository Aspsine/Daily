package com.aspsine.zhihu.daily.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.adapter.ExploreAdapter;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.interfaces.OnItemClickListener;
import com.aspsine.zhihu.daily.interfaces.OnItemLongClickListener;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.ui.activity.StoryActivity;
import com.aspsine.zhihu.daily.util.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/2/26.
 */
public class ExploreFragment extends PlaceholderFragment {
    public static final String TAG = ExploreFragment.class.getSimpleName();
    public static final int TAB_NUM = 5;
    private ViewPagerTabAdapter mViewPagerTabAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPagerTabAdapter = new ViewPagerTabAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(mViewPagerTabAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    protected final class ViewPagerTabAdapter extends FragmentStatePagerAdapter{

        public ViewPagerTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return StoriesFragment.newInstance(DateUtils.getCurrentDate());
        }

        @Override
        public int getCount() {
            return TAB_NUM;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return DateUtils.getCurrentDate();
        }
    }
}
