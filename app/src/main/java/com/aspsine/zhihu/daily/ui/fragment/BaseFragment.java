package com.aspsine.zhihu.daily.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Aspsine on 2015/3/19.
 */
public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_THEME_NUMBER = "theme_number";
    public static final String ARG_THEME_ID = "theme_id";

    public static final int THEME_NUMBER_MAIN = 0;
    public static final String THEME_ID_MAIN = "section_id_main";

    public static final String EXTRA_STORY_ID = "extra_story_id";

    private int mArgThemeNumber;

    private String mArgThemeId;

    public BaseFragment() {
    }

    public static BaseFragment newInstance(int position, String sectionId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_THEME_NUMBER, position);
        bundle.putString(ARG_THEME_ID, sectionId);
        BaseFragment fragment = null;
        if (position == 0) {
            fragment = new DailyStoriesFragment();
        } else {
            fragment = new ThemeStoriesFragment();
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgThemeNumber = getArguments().getInt(ARG_THEME_NUMBER);
        mArgThemeId = getArguments().getString(ARG_THEME_ID);
    }

    public int getThemeNumber() {
        return mArgThemeNumber;
    }

    public String getThemeId() {
        return mArgThemeId;
    }
}
