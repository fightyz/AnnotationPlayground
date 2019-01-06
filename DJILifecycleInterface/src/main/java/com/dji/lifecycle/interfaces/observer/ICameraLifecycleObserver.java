package com.dji.lifecycle.interfaces.observer;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/21/18 12:49 PM
 * Description: 相机模块的生命周期回调
 */
public interface ICameraLifecycleObserver extends IDroneLifecycleObserver {
    void onCameraConnected();
    void onCameraDisconnected();
}
