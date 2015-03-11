package com.aspsine.zhihu.daily.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.interfaces.OnItemClickListener;
import com.aspsine.zhihu.daily.interfaces.OnItemLongClickListener;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public CardView card;
    public TextView text;
    public ImageView image;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public StoryViewHolder(View itemView) {
        super(itemView);
        card = (CardView) itemView.findViewById(R.id.card);
        text = (TextView) itemView.findViewById(R.id.text);
        image = (ImageView) itemView.findViewById(R.id.image);
    }

    public StoryViewHolder(View itemView, OnItemClickListener onItemClickListener,
                           OnItemLongClickListener onItemLongClickListener) {
        this(itemView);
        this.mOnItemClickListener = onItemClickListener;
        this.mOnItemLongClickListener = onItemLongClickListener;
        card.setOnClickListener(this);
        card.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener == null) return;
        mOnItemClickListener.onItemClick(getPosition(), v);
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener == null) return false;
        mOnItemLongClickListener.onItemLongClick(getPosition(), v);
        return true;
    }
}
