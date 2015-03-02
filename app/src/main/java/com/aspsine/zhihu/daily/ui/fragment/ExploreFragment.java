package com.aspsine.zhihu.daily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.util.DateUtils;

import java.util.Calendar;

/**
 * Created by Aspsine on 2015/2/26.
 */
public class ExploreFragment extends PlaceholderFragment {
    public static final String TAG = ExploreFragment.class.getSimpleName();
    public static final int TAB_NUM = 7;
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

    protected final class ViewPagerTabAdapter extends FragmentStatePagerAdapter {

        public ViewPagerTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1 - position);
            return StoriesFragment.newInstance(DateUtils.getDate(calendar.getTime(), "yyyyMMdd"));
        }

        @Override
        public int getCount() {
            return TAB_NUM;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if(position == 0){
                title = "今天";
            }else if(position == 1){
                title = "昨天";
            } else{
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 0 - position);
                title = DateUtils.getDate(calendar.getTime(), "yyyy年MM月dd日");
            }

            return title;
        }
    }
}
