package com.aspsine.zhihu.daily.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.util.UIUtils;
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

    public StoryHeaderView(Context context) {
        this(context, null);
    }

    public StoryHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoryHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static StoryHeaderView newInstance(ViewGroup parent) {
        return (StoryHeaderView) UIUtils.inflate(R.layout.view_header_story, parent);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void BindData(String txt, String url, DisplayImageOptions options) {
        title.setText(txt);
        ImageLoader.getInstance().displayImage(url, image, options);
    }

}
