package com.aspsine.zhihu.daily.ui.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.zhihu.daily.App;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.animation.AnimationEndListener;
import com.aspsine.zhihu.daily.model.StartImage;
import com.aspsine.zhihu.daily.respository.interfaces.Repository;
import com.aspsine.zhihu.daily.ui.activity.GuiderActivity;
import com.aspsine.zhihu.daily.util.DensityUtil;
import com.aspsine.zhihu.daily.util.L;
import com.aspsine.zhihu.daily.util.UIUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by sf on 2015/1/13.
 * splash page
 */
public class SplashFragment extends Fragment {
    public static final String TAG = SplashFragment.class.getSimpleName();

    private TextView tvAuthor;
    private ImageView ivLogo;
    public ImageView ivSplash;
    public Animation mIvSplashAnim;

    private final DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    private int mWidth;
    private int mHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIvSplashAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.splash);

        // if api >= 19 use themes in values-v19 has a full screen splash.
        mWidth = DensityUtil.getScreenWidth(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mHeight = DensityUtil.getScreenHeightWithDecorations(getActivity());
        } else {
            mHeight = DensityUtil.getScreenHeight(getActivity());
        }
        L.i(TAG, "screen height = " + mHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
        ivSplash = (ImageView) view.findViewById(R.id.splash);
        ivLogo = (ImageView) view.findViewById(R.id.ivLogo);

        // if api >= 19 use themes in values-v19 has a full screen splash.
        // so we need relayout the tvAuthor and ivLogo by set a bigger marginBottom
        int navigationBarHeight = UIUtils.getNavigationBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            int tvMarginBottom = navigationBarHeight + DensityUtil.dip2px(getActivity(), 8);
            int logoMarginBottom = navigationBarHeight + DensityUtil.dip2px(getActivity(), 48);
            ((RelativeLayout.LayoutParams) tvAuthor.getLayoutParams()).setMargins(0, 0, 0, tvMarginBottom);
            ((RelativeLayout.LayoutParams) ivLogo.getLayoutParams()).setMargins(0, 0, 0, logoMarginBottom);

            tvAuthor.requestLayout();
            ivLogo.requestLayout();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIvSplashAnim.setAnimationListener(animListener);
        refresh();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIvSplashAnim = null;
    }

    private final Animation.AnimationListener animListener = new AnimationEndListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            ivSplash.setVisibility(View.GONE);
            ((GuiderActivity) getActivity()).intentToMainActivity();
        }
    };

    private void refresh() {

        App.getRepository().getStartImage(mWidth, mHeight, mOptions, new Repository.Callback<StartImage>() {
            @Override
            public void success(StartImage startImage, boolean outDate) {
                tvAuthor.setText(startImage.getText());
                ImageLoader.getInstance().displayImage(startImage.getImg(), ivSplash, mOptions, new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        ivSplash.startAnimation(mIvSplashAnim);
                    }
                });

            }

            @Override
            public void failure(Exception e) {
                L.i(TAG, "default image.");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivSplash.setBackground(getResources().getDrawable(R.drawable.bg_splash));
                }else{
                    ivSplash.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_splash));
                }
                ivSplash.setAnimation(mIvSplashAnim);
                tvAuthor.setText(getResources().getString(R.string.splash_text));
                e.printStackTrace();
            }
        });
    }
}
