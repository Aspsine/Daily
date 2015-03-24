package com.aspsine.zhihu.daily.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.network.Http;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    private String mStoryId;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
//        if (mActionBarToolbar != null) {
//            mActionBarToolbar.setAlpha(80);
//        }
        Toast.makeText(getActivity(), mStoryId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread(new GetStoryTask(mStoryId)).start();
    }

    public void setToolBar(Toolbar toolbar) {
        this.mActionBarToolbar = toolbar;
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
            switch (msg.what) {
                case 0:
                    Story story = (Story) msg.obj;
                    Toast.makeText(getActivity(), story.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


}
