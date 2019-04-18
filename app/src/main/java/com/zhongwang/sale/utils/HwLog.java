package com.zhongwang.sale.utils;

import android.util.Log;


public class HwLog {
    private static boolean isDebug = true;
    private static String logfilter = "HwLog : ";

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, logfilter + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, logfilter + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, logfilter + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, logfilter + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, logfilter + msg);
        }
    }
}
