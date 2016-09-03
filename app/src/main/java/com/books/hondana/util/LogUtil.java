package com.books.hondana.util;

import android.util.Log;

import com.books.hondana.BuildConfig;

public class LogUtil {
    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d (tag, message);
        }
    }

    public static void w(String tag, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.w (tag, throwable);
        }
    }

    public static void w(String tag, String message, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.w (tag, message, throwable);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e (tag, message, throwable);
        } else {
            // TODO Crashlyticsに送信
        }
    }
}
