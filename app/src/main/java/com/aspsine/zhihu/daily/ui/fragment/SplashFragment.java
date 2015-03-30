package com.aspsine.zhihu.daily.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
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

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.api.DailyApi;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.util.DensityUtil;
import com.aspsine.zhihu.daily.util.L;
import com.aspsine.zhihu.daily.util.NetWorkUtils;
import com.aspsine.zhihu.daily.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sf on 2015/1/13.
 * splash page
 */
public class SplashFragment extends Fragment {
    public static final String TAG = SplashFragment.class.getSimpleName();

    private TextView tvCopyrightHolder;
    public ImageView ivSplash;
    public Animation mIvSplashAnim;

    private StartImage mOldStartImage;

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
        String mOldJsonString = SharedPrefUtils.getSplashJson(getActivity());
        if (!TextUtils.isEmpty(mOldJsonString)) {
            mOldStartImage = new Gson().fromJson(mOldJsonString, StartImage.class);
        }

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

        if (NetWorkUtils.isNetWorkAvailable(getActivity())) {
            refresh();
        } else {
            setDefaultData();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIvSplashAnim = null;
    }

    private void setDefaultData() {
        if (mOldStartImage == null) {
            L.i(TAG, "default image.");
            ivSplash.setBackgroundResource(R.drawable.bg_splash);
            tvCopyrightHolder.setText("@Aspsine");
        } else {
            L.i(TAG, "old image.");
            setData(mOldStartImage);
        }
    }

    private void setData(StartImage image) {
        tvCopyrightHolder.setText(image.getText());
        ImageLoader.getInstance().displayImage(image.getImg(), ivSplash, mOptions);
    }

    private void refresh() {
        DailyApi.createApi().getStartImage(mWidth, mHeight, new Callback<StartImage>() {
            @Override
            public void success(StartImage startImage, Response response) {
                SharedPrefUtils.setSplashJson(getActivity(), new Gson().toJson(startImage));
                L.i(TAG, "new image.");
                setData(startImage);
            }

            @Override
            public void failure(RetrofitError error) {
                setDefaultData();
                error.printStackTrace();
            }
        });
    }
}
