package com.aspsine.zhihu.daily.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.Animation;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.animation.AnimationEndListener;
import com.aspsine.zhihu.daily.ui.fragment.GuideFragment;
import com.aspsine.zhihu.daily.ui.fragment.SplashFragment;

public class GuiderActivity extends FragmentActivity {
    boolean isFirst = true;
    SplashFragment mSplashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider);

        FragmentManager fm = getSupportFragmentManager();

        if (isFirst) {
            splash(fm);
        } else {
            guide(fm);
        }
    }

    void splash(FragmentManager fm) {

        mSplashFragment = new SplashFragment();

        if (fm.findFragmentByTag(SplashFragment.TAG) == null) {
            fm.beginTransaction()
                    .add(R.id.container, mSplashFragment, SplashFragment.TAG)
                    .commit();
        }
    }

    void guide(FragmentManager fm) {
        if (fm.findFragmentByTag(GuideFragment.TAG) == null) {
            fm.beginTransaction()
                    .add(R.id.container, new GuideFragment(), GuideFragment.TAG)
                    .commit();
        }
    }

    void intentToMainActivity() {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    private final Animation.AnimationListener animListener = new AnimationEndListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            mSplashFragment.ivSplash.setVisibility(View.GONE);
            intentToMainActivity();
        }
    };

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SplashFragment && mSplashFragment != null) {
            mSplashFragment.mIvSplashAnim.setAnimationListener(animListener);
        }
    }
}
