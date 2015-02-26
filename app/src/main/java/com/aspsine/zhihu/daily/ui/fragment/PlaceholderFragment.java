package com.aspsine.zhihu.daily.ui.fragment;

/**
 * Created by sf on 2015/1/13.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.activity.NavigationDrawerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int argSectionNumber;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        argSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER, 0);
        ((NavigationDrawerActivity) activity).onSectionAttached(argSectionNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_place_holder, container, false);
        TextView tvSelectionLabel = (TextView) rootView.findViewById(R.id.section_label);
        tvSelectionLabel.setText(argSectionNumber+"");
        return rootView;
    }


}
