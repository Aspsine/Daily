package com.aspsine.zhihu.daily.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.activity.GuiderActivity;
import com.aspsine.zhihu.daily.util.SharedPrefUtils;

/**
 *
 */
public class GuideFragment extends Fragment {
    public static final String TAG = GuideFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guide, container, false);
        rootView.findViewById(R.id.btnEnter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefUtils.markFirstLaunch(getActivity());
                ((GuiderActivity)getActivity()).intentToMainActivity();
            }
        });
        return rootView;
    }


}
