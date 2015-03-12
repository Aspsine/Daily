package com.aspsine.zhihu.daily.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class MyViewPager extends ViewPager {
    private static final String TAG = MyViewPager.class.getSimpleName();
    private static final int WHAT_SCROLL = 0;
    private long mDelayTime = 5000;

    private float mDownX;
    private float mDownY;

    private boolean isAutoScroll;
    private boolean isStopByTouch;

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {

                    //不允许拦截事件，自己处理
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            stopAutoScroll();
            isStopByTouch = true;
        } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
            startAutoScroll();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setDelayTime(int delayTime) {
        this.mDelayTime = delayTime;
    }

    public void destroyView() {
        stopAutoScroll();
        this.removeAllViews();
    }

    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage(mDelayTime);
    }

    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(WHAT_SCROLL);
    }

    private void sendScrollMessage(long delayTimeInMills) {
        handler.removeMessages(WHAT_SCROLL);
        handler.sendEmptyMessageDelayed(WHAT_SCROLL, delayTimeInMills);
    }

    private void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int count;
        if (adapter == null || (count = adapter.getCount()) < 1) {
            stopAutoScroll();
            return;
        }
        if (currentItem < count) {
            currentItem++;
        }
        if (currentItem == count) {
            currentItem = 0;
        }
        Log.i(TAG, currentItem + "");
        setCurrentItem(currentItem);
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_SCROLL) {
                scrollOnce();
                sendScrollMessage(mDelayTime);
            }
        }
    };
}
