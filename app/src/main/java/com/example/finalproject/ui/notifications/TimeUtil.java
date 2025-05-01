package com.example.finalproject.ui.notifications;

import android.text.format.DateUtils;

public class TimeUtil {
    public static String getRelativeTime(long timestamp) {
        return DateUtils.getRelativeTimeSpanString(timestamp,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS).toString();
    }
}