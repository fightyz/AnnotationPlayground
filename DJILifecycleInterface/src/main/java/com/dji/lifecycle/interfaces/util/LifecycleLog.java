package com.dji.lifecycle.interfaces.util;

import android.util.Log;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/27/18 6:14 PM
 * Description: ${TODO}
 */
public class LifecycleLog {

    private static final String TAG = "DJILifecycle";

    public static void i(String tag, String message) {
        Log.i(TAG, "[" + tag + "] " + message);
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void d(String tag, String message) {
        Log.d(TAG, "[" + tag + "] " + message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String tag, String message) {
        Log.e(TAG, "[" + tag + "] " + message);
    }
}
