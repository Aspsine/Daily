package com.aspsine.zhihu.daily.ui.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.aspsine.zhihu.daily.App;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.model.Editor;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.respository.interfaces.Repository;
import com.aspsine.zhihu.daily.ui.widget.AvatarsView;
import com.aspsine.zhihu.daily.ui.widget.StoryHeaderView;
import com.aspsine.zhihu.daily.util.IntentUtils;
import com.aspsine.zhihu.daily.util.ScrollPullDownHelper;
import com.aspsine.zhihu.daily.util.WebUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

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

    @InjectView(R.id.avatarsView)
    AvatarsView avatarsView;

    @InjectView(R.id.spaceView)
    View spaceView;

    private SoftReference<WebView> refWebView;
//    WebView webView;

    private StoryHeaderView storyHeaderView;

    private DisplayImageOptions mOptions;

    private ScrollPullDownHelper mScrollPullDownHelper;

    private String mStoryId;

    private Story mStory;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mStoryId = getArguments().getString(DailyStoriesFragment.EXTRA_STORY_ID);
        }

        mScrollPullDownHelper = new ScrollPullDownHelper();

        this.mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storyHeaderView = StoryHeaderView.newInstance(container);
        avatarsView = (AvatarsView) inflater.inflate(R.layout.layout_avatars, container, false);
        refWebView = new SoftReference<WebView>(new WebView(getActivity()));
        refWebView.get().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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

        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);

        if (isWebViewOK()) {
            llWebViewContainer.addView(refWebView.get());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            if (mStory != null) {
                IntentUtils.share(getActivity(), mStory);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        App.getRepository().getStoryDetail(mStoryId, new Repository.Callback<Story>() {
            @Override
            public void success(Story story, boolean outDate) {
                if (getActivity() == null) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                mStory = story;
                bindData(story);
            }

            @Override
            public void failure(Exception e) {
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }
        });
    }

    private boolean isWebViewOK() {
        return refWebView != null && refWebView.get() != null;
    }

    public void setToolBar(Toolbar toolbar) {
        this.mActionBarToolbar = toolbar;
    }

    private void bindData(Story story) {
        boolean hasImage = !TextUtils.isEmpty(story.getImage());
        bindHeaderView(hasImage);
        bindAvatarsView();
        bindWebView(hasImage);
    }


    private void bindHeaderView(boolean hasImage) {
        // bind header view
        if (hasImage) {
            if (mActionBarToolbar != null) {
                mActionBarToolbar.getBackground().setAlpha(0);
            }
            spaceView.setVisibility(View.VISIBLE);
            rlStoryHeader.addView(storyHeaderView);
            storyHeaderView.bindData(mStory.getTitle(), mStory.getImageSource(), mStory.getImage(), mOptions);
        } else {
            spaceView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mActionBarToolbar.getHeight()));
        }
    }

    private void bindAvatarsView() {
        List<Editor> recommenders = mStory.getRecommenders();
        if (recommenders != null && recommenders.size() > 0) {
            avatarsView.setVisibility(View.VISIBLE);
            List<String> avatars = new ArrayList<>(recommenders.size());
            for (Editor editor : recommenders) {
                avatars.add(editor.getAvatar());
            }
            avatarsView.bindData(getString(R.string.avatar_title_referee), avatars);
        } else {
            avatarsView.setVisibility(View.GONE);
        }
    }

    private void bindWebView(boolean hasImage) {
        if (TextUtils.isEmpty(mStory.getBody())) {
            WebSettings settings = refWebView.get().getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setAppCacheEnabled(true);
            refWebView.get().setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            refWebView.get().loadUrl(mStory.getShareUrl());
        } else {
            String data = WebUtils.BuildHtmlWithCss(mStory.getBody(), mStory.getCssList(), false);
            refWebView.get().loadDataWithBaseURL(WebUtils.BASE_URL, data, WebUtils.MIME_TYPE, WebUtils.ENCODING, WebUtils.FAIL_URL);
            if (hasImage) {
                addSrollListener();
            }
        }

    }

    private void addSrollListener() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!isAdded()) {
                    return;
                }
                changeHeaderPosition();
                changeToolbarAlpha();
            }
        });
    }


    private void changeHeaderPosition() {
        int scrollY = scrollView.getScrollY();

        // Set height
        float storyHeaderViewHeight = getStoryHeaderViewHeight();
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
        int storyHeaderViewHeight = getStoryHeaderViewHeight();
        int toolbarHeight = mActionBarToolbar.getHeight();
        float contentHeight = storyHeaderViewHeight - toolbarHeight;
        float ratio = Math.min(scrollY / contentHeight, 1.0f);
        mActionBarToolbar.getBackground().setAlpha((int) (ratio * 0xFF));
        if (scrollY <= contentHeight) {
            mActionBarToolbar.setY(0f);
            return;
        }

        // Don't show toolbar if user has pulled up the whole article
        if (scrollY + scrollView.getHeight() > llWebViewContainer.getMeasuredHeight() + storyHeaderViewHeight) {
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

    private int getStoryHeaderViewHeight() {
        return getResources().getDimensionPixelSize(R.dimen.view_header_story_height);
    }


}
