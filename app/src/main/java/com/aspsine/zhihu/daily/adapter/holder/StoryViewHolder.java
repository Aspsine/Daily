package com.aspsine.zhihu.daily.adapter.holder;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.util.IntentUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CardView card;
    private TextView text;
    private ImageView image;
    private DisplayImageOptions mOptions;
    private Story mStory;

    public StoryViewHolder(View itemView) {
        super(itemView);

        this.mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        card = (CardView) itemView.findViewById(R.id.card);
        text = (TextView) itemView.findViewById(R.id.text);
        image = (ImageView) itemView.findViewById(R.id.image);
        card.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        IntentUtils.IntentToStoryActivity((Activity) v.getContext(), mStory);
    }

    public void bindStoryView(List<Story> stories) {
        mStory = stories.get(getPosition() - 1);
        text.setText(String.valueOf(mStory.getTitle()));
        String imageUrl = mStory.getImages() == null ? "" : mStory.getImages().get(0);
        ImageLoader.getInstance().displayImage(imageUrl, image, mOptions);
    }
}
