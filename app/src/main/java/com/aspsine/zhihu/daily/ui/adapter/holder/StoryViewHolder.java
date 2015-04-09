package com.aspsine.zhihu.daily.ui.adapter.holder;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.model.Story;
import com.aspsine.zhihu.daily.ui.activity.NavigationDrawerActivity;
import com.aspsine.zhihu.daily.ui.fragment.NavigationFragment;
import com.aspsine.zhihu.daily.util.IntentUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CardView card;
    private TextView text;
    private TextView theme;
    private ImageView image;
    private ImageView ivMultiPic;
    private ImageView line;
    private RelativeLayout rlContent;
    private DisplayImageOptions mOptions;
    private Story mStory;

    public StoryViewHolder(View itemView) {
        super(itemView);

        this.mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_small_default)
                .showImageForEmptyUri(R.drawable.image_small_default)
                .showImageOnFail(R.drawable.image_small_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        card = (CardView) itemView.findViewById(R.id.card);
        text = (TextView) itemView.findViewById(R.id.text);
        image = (ImageView) itemView.findViewById(R.id.image);
        ivMultiPic = (ImageView) itemView.findViewById(R.id.ivMultiPic);
        rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_content);
        theme = (TextView) itemView.findViewById(R.id.theme);
        line = (ImageView) itemView.findViewById(R.id.line);
        card.setOnClickListener(this);
        theme.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.card) {
            IntentUtils.intentToStoryActivity((Activity) v.getContext(), mStory);
        } else if (v.getId() == R.id.theme) {
            final NavigationFragment navigationFragment = (NavigationFragment) ((NavigationDrawerActivity) itemView.getContext()).getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
            if (navigationFragment != null && navigationFragment.isAdded()) {
                navigationFragment.openDrawer();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigationFragment.selectTheme(mStory.getTheme());
                    }
                }, 500);
            }
        }
    }

    public void bindStoryView(Story story) {
        mStory = story;
        if (mStory.getTheme() == null) {
            theme.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            theme.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            sb.append(itemView.getResources().getString(R.string.recycler_item_main_select_from))
                    .append(" ").append("<font color=\"#2196F3\">")
                    .append(mStory.getTheme().getName())
                    .append("</font>");
            theme.setText(Html.fromHtml(sb.toString()));
        }
        text.setText(String.valueOf(mStory.getTitle()));

        String imageUrl = mStory.getImages() == null ? "" : mStory.getImages().get(0);
        if (TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(mStory.getMultiPic())) {
            ivMultiPic.setVisibility(View.GONE);
        } else if (Boolean.valueOf(mStory.getMultiPic())) {
            ivMultiPic.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(imageUrl)) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(imageUrl, image, mOptions);
        }
    }
}
