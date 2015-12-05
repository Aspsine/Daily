package com.aspsine.zhihu.daily.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.ui.fragment.GuideFragment;
import com.aspsine.zhihu.daily.ui.fragment.SplashFragment;
import com.aspsine.zhihu.daily.util.IntentUtils;
import com.aspsine.zhihu.daily.util.SharedPrefUtils;

public class GuiderActivity extends FragmentActivity {
    private SplashFragment mSplashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider);

        FragmentManager fm = getSupportFragmentManager();

        if (SharedPrefUtils.isFirstLaunch(this)) {
            guide(fm);
        } else {
            splash(fm);
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

    public void intentToMainActivity() {
        IntentUtils.intentToMainActivity(this);
    }
}
