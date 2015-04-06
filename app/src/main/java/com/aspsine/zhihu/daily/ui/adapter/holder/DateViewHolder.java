package com.aspsine.zhihu.daily.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.util.DateUtils;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class DateViewHolder extends RecyclerView.ViewHolder {
    public TextView tvDate;

    public DateViewHolder(View itemView) {
        super(itemView);

        tvDate = (TextView) itemView.findViewById(R.id.date);
    }

    public void bindDateView(String date) {
        tvDate.setText(getDate(date, itemView.getContext()));
    }

    public static String getDate(String date, Context context) {
        if (DateUtils.isToday(date)) {
            return context.getResources().getString(R.string.main_page_today_hottest);
        } else {
            return DateUtils.getMainPageDate(date);
        }
    }

}
