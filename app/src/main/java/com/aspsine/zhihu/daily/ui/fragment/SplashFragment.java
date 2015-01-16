package com.aspsine.zhihu.daily.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.aspsine.zhihu.daily.R;

/**
 * Created by sf on 2015/1/13.
 * splash page
 */
public class SplashFragment extends Fragment{
    public static final String TAG = SplashFragment.class.getSimpleName();
    public ImageView ivSplash;
    public Animation mIvSplashAnim;


    @Override
    public void onAttach(Activity activity) {
        mIvSplashAnim = AnimationUtils.loadAnimation(activity, R.anim.splash);
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIvSplashAnim = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        ivSplash = (ImageView) rootView.findViewById(R.id.splash);
        ivSplash.setBackgroundResource(R.drawable.bg_splash);
        ivSplash.startAnimation(mIvSplashAnim);
        return rootView;
    }
}
