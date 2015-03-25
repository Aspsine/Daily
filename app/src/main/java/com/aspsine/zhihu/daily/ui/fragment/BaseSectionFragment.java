package com.aspsine.zhihu.daily.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Aspsine on 2015/3/19.
 */
public class BaseSectionFragment extends Fragment {
    private static final String TAG = BaseSectionFragment.class.getSimpleName();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public static final String ARG_SECTION_ID = "section_id";

    public static final int SECTION_NUMBER_MAIN = 0;

    public static final String SECTION_ID_MAIN = "section_id_main";
    public static final String EXTRA_STORY_ID = "extra_story_id";

    private int mArgSectionNumber;

    private String mArgSectionId;

    public BaseSectionFragment() {
    }

    ;

    public static BaseSectionFragment newInstance(int position, String sectionId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, position);
        bundle.putString(ARG_SECTION_ID, sectionId);
        BaseSectionFragment fragment = null;
        if (position == 0) {
            fragment = new StoryListFragment();
        } else {
            fragment = new SectionFragment();
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        mArgSectionId = getArguments().getString(ARG_SECTION_ID);
    }

    public int getSectionNumber() {
        return mArgSectionNumber;
    }

    public String getSectionId() {
        return mArgSectionId;
    }
}
