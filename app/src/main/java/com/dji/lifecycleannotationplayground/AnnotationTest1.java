package com.dji.lifecycleannotationplayground;

import android.util.Log;

import dji.lifecycle.annotation.OnActivityCreated;
import dji.lifecycle.annotation.OnProductConnected;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/2/19 9:36 PM
 * Description: ${TODO}
 */
public class AnnotationTest1 {

    private static final String TAG = "AnnotationTest1";

    @OnProductConnected
    public void onProductConnected() {
        Log.e(TAG, "onProductConnected");
    }

    @OnActivityCreated
    public void onActivityCreated() {
        Log.e(TAG, "onActivityCreated");
    }
}
