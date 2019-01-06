package com.dji.lifecycle.interfaces.sample;

import com.dji.lifecycle.interfaces.observer.IAirLinkLifecycleObserver;
import com.dji.lifecycle.interfaces.observer.IProductLifecycleObserver;
import com.dji.lifecycle.interfaces.util.LifecycleLog;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/21/18 5:52 PM
 * Description: ${TODO}
 */
public class FpvLifecycleTest2 implements IProductLifecycleObserver, IAirLinkLifecycleObserver {

    private static final String TAG = "FpvLifecycleTest2";

    @Override
    public void onAirLinkConnected() {
        LifecycleLog.e(TAG, "2 onAirLinkConnected");
    }

    @Override
    public void onAirLinkDisconnected() {
        LifecycleLog.e(TAG, "2 onAirLinkDisconnected");
    }

    @Override
    public void onProductConnected() {
        LifecycleLog.e(TAG, "2 onProductConnected");
    }

    @Override
    public void onProductDisconnected() {
        LifecycleLog.e(TAG, "2 onProductDisconnected");
    }
}
