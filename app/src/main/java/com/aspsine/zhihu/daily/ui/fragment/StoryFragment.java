package com.aspsine.zhihu.daily.ui.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.ui.widget.StoryHeaderView;
import com.aspsine.zhihu.daily.util.ScrollPullDownHelper;
import com.aspsine.zhihu.daily.util.WebUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.lang.ref.SoftReference;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment {
    public static final String TAG = StoryFragment.class.getSimpleName();
    private Toolbar mActionBarToolbar;

    @InjectView(R.id.pb)
    ProgressBar progressBar;

    @InjectView(R.id.rl_container_header)
    RelativeLayout rlStoryHeader;

    @InjectView(R.id.scrollView)
    ScrollView scrollView;

    @InjectView(R.id.webViewContainer)
    LinearLayout llWebViewContainer;

    private SoftReference<WebView> refWebView;
//    WebView webView;

    private StoryHeaderView storyHeaderView;

    private String mStoryId;

    private DisplayImageOptions mOptions;

    private ScrollPullDownHelper mScrollPullDownHelper;

    public static StoryFragment newInstance(String storyId) {
        StoryFragment fragment = new StoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DailyStoriesFragment.EXTRA_STORY_ID, storyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mStoryId = getArguments().getString(DailyStoriesFragment.EXTRA_STORY_ID);
        }

        mScrollPullDownHelper = new ScrollPullDownHelper();

        this.mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storyHeaderView = StoryHeaderView.newInstance(container);
        refWebView = new SoftReference<WebView>(new WebView(getActivity().getApplicationContext()));

        if (isWebViewOK()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            refWebView.get().setLayoutParams(lp);
        }
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        if (mActionBarToolbar != null) {
            mActionBarToolbar.getBackground().setAlpha(0);
        }
        rlStoryHeader.addView(storyHeaderView);
        if (isWebViewOK()) {
            llWebViewContainer.addView(refWebView.get());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread(new GetStoryTask(mStoryId)).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isWebViewOK()) {
            refWebView.get().removeAllViews();
            refWebView.get().destroy();
            llWebViewContainer.removeView(refWebView.get());
            refWebView = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private boolean isWebViewOK() {
        return refWebView != null && refWebView.get() != null;
    }

    public void setToolBar(Toolbar toolbar) {
        this.mActionBarToolbar = toolbar;
    }

    private void bindData(Story story) {
        Context context = getActivity();
        if (context == null) {
            return;
        }

        storyHeaderView.BindData(story.getTitle(), story.getImage(), mOptions);

        if (TextUtils.isEmpty(story.getBody())) {
            WebSettings settings = refWebView.get().getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            refWebView.get().setWebViewClient(new WebViewClient());
            refWebView.get().loadUrl(story.getShareUrl());
        } else {
            String data = WebUtils.BuildHtmlWithCss(story.getBody(), story.getCssList(), false);
            refWebView.get().loadDataWithBaseURL(WebUtils.BASE_URL, data, WebUtils.MIME_TYPE, WebUtils.ENCODING, WebUtils.FAIL_URL);

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (!isWebViewOK()) {
                        return;
                    }
                    changeHeaderPosition();
                    changeToolbarAlpha();
                }

            });
        }

    }

    private void changeHeaderPosition() {
        int scrollY = scrollView.getScrollY();

        // Set height
        float storyHeaderViewHeight = getResources().getDimensionPixelSize(R.dimen.view_header_story_height);
        if (scrollY < 0) {
            // Pull down, zoom in the image
            storyHeaderViewHeight += Math.abs(scrollY);
        }
        storyHeaderView.getLayoutParams().height = (int) storyHeaderViewHeight;

        // Set scroll
        int headerScrollY = (scrollY > 0) ? (scrollY / 2) : 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            storyHeaderView.setScrollY(headerScrollY);
            storyHeaderView.requestLayout();
        }
    }

    private void changeToolbarAlpha() {

        int scrollY = scrollView.getScrollY();
        int storyHeaderViewHeight = getResources().getDimensionPixelSize(R.dimen.view_header_story_height);
        int toolbarHeight = mActionBarToolbar.getHeight();
        float contentHeight = storyHeaderViewHeight - toolbarHeight;
        float ratio = Math.min(scrollY / contentHeight, 1.0f);
        mActionBarToolbar.getBackground().setAlpha((int) (ratio * 0xFF));
        if (scrollY <= contentHeight) {
            mActionBarToolbar.setY(0f);
            return;
        }

        // Don't show toolbar if user has pulled up the whole article
        if (scrollY + scrollView.getHeight() > refWebView.get().getMeasuredHeight() + storyHeaderViewHeight) {
            return;
        }

        // Show the toolbar if user is pulling down
        boolean isPullingDown = mScrollPullDownHelper.onScrollChanged(scrollY);
        float toolBarPositionY = isPullingDown ? 0 : (contentHeight - scrollY);
        if (scrollY < storyHeaderViewHeight + toolbarHeight) {
            toolBarPositionY = storyHeaderViewHeight - scrollY - toolbarHeight;
        }

        mActionBarToolbar.setY(toolBarPositionY);

    }


    private final class GetStoryTask implements Runnable {
        String id;

        public GetStoryTask(String storyId) {
            this.id = storyId;
        }

        @Override
        public void run() {
            try {
                String jsonStr = Http.get(Constants.Url.ZHIHU_DAILY_DETAIL, id);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                Story story = gson.fromJson(jsonStr, Story.class);
                handler.obtainMessage(0, story).sendToTarget();
            } catch (Exception e) {
                handler.obtainMessage(-1).sendToTarget();
                e.printStackTrace();
            }
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressBar.setVisibility(View.GONE);
            switch (msg.what) {
                case 0:
                    Story story = (Story) msg.obj;
                    bindData(story);
                    break;
                default:
                    break;
            }
        }
    };


}
