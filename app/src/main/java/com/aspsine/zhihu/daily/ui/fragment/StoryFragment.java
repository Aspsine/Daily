package com.aspsine.zhihu.daily.ui.fragment;


import android.content.Context;
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
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.ui.widget.StoryHeaderView;
import com.aspsine.zhihu.daily.util.WebUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

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

    @InjectView(R.id.webView)
    WebView webView;

    private StoryHeaderView storyHeaderView;

    private String mStoryId;

    private DisplayImageOptions mOptions;

    public static StoryFragment newInstance(String storyId) {
        StoryFragment fragment = new StoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StoriesFragment.EXTRA_STORY_ID, storyId);
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
            mStoryId = getArguments().getString(StoriesFragment.EXTRA_STORY_ID);
        }

        this.mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storyHeaderView = StoryHeaderView.newInstance(container);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread(new GetStoryTask(mStoryId)).start();
    }

    public void setToolBar(Toolbar toolbar) {
        this.mActionBarToolbar = toolbar;
    }

    private void bindData(Story story){
        Context context = getActivity();
        if(context == null){
            return;
        }
        if(TextUtils.isEmpty(story.getBody())){
            return;
        }
        String data = WebUtils.BuildHtmlWithCss(story.getBody(), story.getCssList(), false);
        webView.loadDataWithBaseURL(WebUtils.BASE_URL, data, WebUtils.MIME_TYPE, WebUtils.ENCODING, WebUtils.FAIL_URL);
        storyHeaderView.BindData(story.getTitle(), story.getImage(), mOptions);

        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                changeHeaderPosition();
                changeToolbarAlpha();
            }

        });
    }

    private void changeHeaderPosition(){

    }

    private void changeToolbarAlpha(){
        int scrollY= scrollView.getScrollY();
        int storyHeaderViewHeight = getResources().getDimensionPixelSize(R.dimen.view_header_story_height);
        int toolbarHeight = mActionBarToolbar.getHeight();
        float contentHeight = storyHeaderViewHeight - toolbarHeight;
        float ratio = Math.min(scrollY / contentHeight, 1.0f);
        if(scrollY <= contentHeight){
            mActionBarToolbar.setY(0f);
            return;
        }

        // Don't show toolbar if user has pulled up the whole article
        if(scrollY + scrollView.getHeight() > webView.getMeasuredHeight()+storyHeaderViewHeight){
            return;
        }

        // Show the toolbar if user is pulling down


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
