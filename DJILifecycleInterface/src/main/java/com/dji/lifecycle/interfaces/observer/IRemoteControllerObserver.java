package com.dji.lifecycle.interfaces.observer;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/21/18 3:00 PM
 * Description: 遥控器模块的生命周期回调
 */
public interface IRemoteControllerObserver extends IDroneLifecycleObserver {
    void onRemoteControllerConnected();
    void onRemoteControllerDisconnected();
}
