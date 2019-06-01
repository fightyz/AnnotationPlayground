package com.fightyz.testandroidmodule;

import android.util.Log;

import com.fightyz.lifecycle.annotation.OnActivityCreated;
import com.fightyz.lifecycle.annotation.OnProductConnected;

public class AnnotationTest3 {

    private static final String TAG = "AnnotationTest3";

    @OnProductConnected
    public void productConnected() {
        Log.e(TAG, "productConnected");
    }

    @OnActivityCreated
    public void activityCreated() {
        Log.e(TAG, "activityCreated");
    }
}
