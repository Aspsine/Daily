package com.aspsine.zhihu.daily.util;


import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by aspsine on 15-3-24. thanks chentian.
 * Detect several latest moves to ensure a smooth showing-hiding toolbar effect
 *
 * @author chentian
 */
public class ScrollPullDownHelper {

    private static final int PULLING_DOWN_TIME_MAX = 8;
    private static final int PULLING_DOWN_TIME_THRESHOLD = 6;

    private int lastScrollY;
    private Queue<Boolean> latestPullingDown;

    public ScrollPullDownHelper() {
        lastScrollY = 0;
        latestPullingDown = new LinkedList<>();
    }

    /**
     * Call this when scroll changed
     *
     * @return true if user is pulling down
     */
    public boolean onScrollChanged(int scrollY) {
        boolean isPullingDownNow = scrollY < lastScrollY;
        latestPullingDown.offer(isPullingDownNow);
        if (latestPullingDown.size() > PULLING_DOWN_TIME_MAX) {
            latestPullingDown.poll();
        }
        lastScrollY = scrollY;

        return getPullingDownTime() >= PULLING_DOWN_TIME_THRESHOLD;
    }

    private int getPullingDownTime() {
        int result = 0;
        for (Boolean isPullingDown : latestPullingDown) {
            if (isPullingDown) {
                result++;
            }
        }
        return result;
    }
}
