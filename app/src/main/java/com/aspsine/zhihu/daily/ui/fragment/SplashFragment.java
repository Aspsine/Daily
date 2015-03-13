package com.aspsine.zhihu.daily.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.util.DensityUtil;
import com.aspsine.zhihu.daily.util.L;
import com.aspsine.zhihu.daily.util.NetWorkUtil;
import com.aspsine.zhihu.daily.util.SharedPrefUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by sf on 2015/1/13.
 * splash page
 */
public class SplashFragment extends Fragment {
    public static final String TAG = SplashFragment.class.getSimpleName();

    private TextView tvCopyrightHolder;
    public ImageView ivSplash;
    public Animation mIvSplashAnim;

    private String mOldJsonString;
    private DisplayImageOptions mOptions;

    private int mWidth;
    private int mHeight;

    @Override
    public void onAttach(Activity activity) {
        mIvSplashAnim = AnimationUtils.loadAnimation(activity, R.anim.splash);
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOldJsonString = SharedPrefUtils.getSplashJson(getActivity());
        mWidth = DensityUtil.getScreenWidth(getActivity());
        mHeight = DensityUtil.getScreenHeight(getActivity());
        this.mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCopyrightHolder = (TextView) view.findViewById(R.id.tvCopyrightHolder);
        ivSplash = (ImageView) view.findViewById(R.id.splash);
        ivSplash.startAnimation(mIvSplashAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (NetWorkUtil.isNetWorkAvailable(getActivity())) {
            new Thread(new GetSplashTask()).start();
        } else {
            if (TextUtils.isEmpty(mOldJsonString)) {
                setDefaultData();
            } else {
                setData(String.valueOf(mOldJsonString));
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIvSplashAnim = null;
    }

    private void setDefaultData() {
        ivSplash.setBackgroundResource(R.drawable.bg_splash);
        tvCopyrightHolder.setText("@Aspsine");
    }

    private void setData(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            tvCopyrightHolder.setText(jsonObject.getString("text"));
            ImageLoader.getInstance().displayImage(jsonObject.getString("img"), ivSplash, mOptions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final class GetSplashTask implements Runnable {

        @Override
        public void run() {
            try {
                String newJsonString = Http.get(Constants.Url.ZHIHU_DAILY_SPLASH + mWidth + "*" + mHeight);
                if (!TextUtils.isEmpty(newJsonString) && TextUtils.isEmpty(mOldJsonString)) {
                    L.i(TAG, "splash has been updated");
                    SharedPrefUtils.setSplashJson(getActivity(), newJsonString);
                    mOldJsonString = newJsonString;
                } else if (TextUtils.isEmpty(newJsonString) && TextUtils.isEmpty(mOldJsonString)) {
                    handler.obtainMessage(0).sendToTarget();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.obtainMessage(10, mOldJsonString).sendToTarget();
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setDefaultData();
                    break;
                case 10:
                    L.i(TAG, "use imageLoader set splash bg");
                    setData(String.valueOf(msg.obj));
                    break;
            }
        }
    };
}
