package com.dji.lifecycle.interfaces.observer;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/21/18 2:49 PM
 * Description: 电池模块的生命周期回调
 */
public interface IBatteryLifecycleObserver extends IDroneLifecycleObserver {
    void onBatteryConnected();
    void onBatteryDisconnected();
}
