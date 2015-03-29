package com.aspsine.zhihu.daily.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.activity.GuiderActivity;
import com.aspsine.zhihu.daily.util.SharedPrefUtils;

/**
 *
 */
public class GuideFragment extends Fragment {
    public static final String TAG = GuideFragment.class.getSimpleName();

    String hello = "<h1>Welcome<h1><p>This application is a simple demonstration of Zhihu Daily Android developed by me. It's a free app. All the information, content and api copyright is belong to Zhihu. Inc.</Br><p>-Aspsine</p></p><h2>About</h2><p>Email: littleximail@gmail.com</p><p>Github: github.com/aspsine</p>";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guide, container, false);
        TextView tvHello = (TextView) rootView.findViewById(R.id.tvHello);
        tvHello.setText(Html.fromHtml(hello));
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
