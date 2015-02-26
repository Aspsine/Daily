package com.aspsine.zhihu.daily.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;

import org.w3c.dom.Text;

/**
 * Created by sf on 2015/1/20.
 */
public class Section extends LinearLayout {
    private TextView tvTitle;
    private TextView tvCount;
    private ImageView ivIcon;


    public static final int TYPE_TEXT = 0;
    public static final int TYPE_ICON = 1;
    public static final int TYPE_TEXT_COUNT = 2;
    public static final int TYPE_ICON_TEXT = 3;
    public static final int TYPE_ICON_COUNT = 4;
    public static final int TYPE_ICON_TEXT_COUNT = 5;

    public Section(Context context) {
        super(context);
    }

    public Section(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Section(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.nav_drawer_item, this, false);
        tvTitle = (TextView) view.findViewById(R.id.tvItemName);
        tvCount = (TextView) view.findViewById(R.id.tvItemCount);
        ivIcon = (ImageView) view.findViewById(R.id.ivItemIcon);
    }

    public Section setType(int type) {
        switch (type) {
            case TYPE_TEXT:
                tvTitle.setVisibility(View.VISIBLE);
                ivIcon.setVisibility(View.GONE);
                tvCount.setVisibility(View.GONE);
                return this;
            case TYPE_ICON:
                tvTitle.setVisibility(View.GONE);
                ivIcon.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.GONE);
                return this;
            case TYPE_TEXT_COUNT:
                tvTitle.setVisibility(View.VISIBLE);
                ivIcon.setVisibility(View.GONE);
                tvCount.setVisibility(View.VISIBLE);
                return this;
            case TYPE_ICON_TEXT:
                tvTitle.setVisibility(View.VISIBLE);
                ivIcon.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.GONE);
                return this;
            case TYPE_ICON_COUNT:
                tvTitle.setVisibility(View.GONE);
                ivIcon.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
                return this;
            case TYPE_ICON_TEXT_COUNT:
                tvTitle.setVisibility(View.VISIBLE);
                ivIcon.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
                return this;
        }
        return null;
    }

    public Section setTitle(CharSequence title) {
        tvTitle.setText(title);
        return this;
    }

    public Section setIcon(int resId) {
        ivIcon.setBackgroundResource(resId);
        return this;
    }

    public Section setCount(int count) {
        CharSequence charCount = String.valueOf(count);
        tvCount.setText(charCount);
        return this;
    }


}
