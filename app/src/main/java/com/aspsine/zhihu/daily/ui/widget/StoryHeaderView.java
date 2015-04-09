package com.aspsine.zhihu.daily.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Aspsine on 2015/3/24.
 */
public class StoryHeaderView extends RelativeLayout {
    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.tvAuthor)
    TextView author;

    public StoryHeaderView(Context context) {
        this(context, null);
    }

    public StoryHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoryHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.view_header_story_height)));
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_header_story, this, true);
        ButterKnife.inject(this);
    }

    public void bindData(String title, String author, String url) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        this.bindData(title, author, url, options);
    }

    public void bindData(String title, String author, String url, DisplayImageOptions options) {
        this.title.setText(title);
        if (TextUtils.isEmpty(author)) {
            this.author.setVisibility(View.GONE);
        } else {
            this.author.setVisibility(View.VISIBLE);
            this.author.setText(author);
        }
        ImageLoader.getInstance().displayImage(url, image, options);
    }

    public static StoryHeaderView newInstance(ViewGroup container) {
        return new StoryHeaderView(container.getContext());
    }
}
