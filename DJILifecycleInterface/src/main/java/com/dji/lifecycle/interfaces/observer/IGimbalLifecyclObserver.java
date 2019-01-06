package com.dji.lifecycle.interfaces.observer;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/21/18 2:51 PM
 * Description: 云台模块的生命周期回调
 */
public interface IGimbalLifecyclObserver extends IDroneLifecycleObserver {
    void onGimbalConnected();
    void onGimbalDisconnected();
}
