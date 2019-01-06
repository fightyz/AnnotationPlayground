package com.dji.lifecycle.interfaces.sample;

import com.dji.lifecycle.interfaces.observer.IFlightControllerLifecycleObserver;
import com.dji.lifecycle.interfaces.observer.IProductLifecycleObserver;
import com.dji.lifecycle.interfaces.util.LifecycleLog;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/20/18 4:00 PM
 * Description: ${TODO}
 */
public class FpvLifecycleTest implements IProductLifecycleObserver, IFlightControllerLifecycleObserver {

    private static final String TAG = "FpvLifecycleTest";

//    @Override
//    public void onActivityCreate(Activity activity) {
//        LifecycleLog.e(TAG, "1 onLifecycleCreate");
//    }
//
//    @Override
//    public void onActivityStart() {
//        LifecycleLog.e(TAG, "1 onLifecycleStart");
//    }
//
//    @Override
//    public void onActivityResume() {
//        LifecycleLog.e(TAG, "1 onLifecycleResume");
//    }
//
//    @Override
//    public void onActivityPause() {
//        LifecycleLog.e(TAG, "onLifecyclePause");
//    }
//
//    @Override
//    public void onActivityStop() {
//        LifecycleLog.e(TAG, "onLifecycleStop");
//    }
//
//    @Override
//    public void onActivityDestroy() {
//        LifecycleLog.e(TAG, "1 onLifecycleDestroy");
//    }

    @Override
    public void onProductConnected() {
        LifecycleLog.e(TAG, "1 onLifecycleProductConnected");
    }

    @Override
    public void onProductDisconnected() {
        LifecycleLog.e(TAG, "1 onLifecycleProductDisconnected");
    }

    @Override
    public void onFlightControllerConnected() {
        LifecycleLog.e(TAG, "1 onFlightControllerConnected");
    }

    @Override
    public void onFlightControllerDisconnected() {
        LifecycleLog.e(TAG, "1 onFlightControllerDisconnected");
    }
}
