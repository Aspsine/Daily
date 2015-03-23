package com.aspsine.zhihu.daily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Aspsine on 2015/3/19.
 */
public abstract class BaseSectionFragment extends Fragment {
    private static final String TAG = BaseSectionFragment.class.getSimpleName();
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
    }


}
