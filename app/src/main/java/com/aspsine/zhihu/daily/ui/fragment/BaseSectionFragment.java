package com.aspsine.zhihu.daily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.aspsine.zhihu.daily.ui.activity.NavigationDrawerActivity;

/**
 * Created by Aspsine on 2015/3/19.
 */
public abstract class BaseSectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    private int argSectionNumber;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        argSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER, 0);
        ((NavigationDrawerActivity) getActivity()).onSectionAttached(argSectionNumber);
    }
}
