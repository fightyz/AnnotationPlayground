package com.dji.lifecycle.interfaces.observer;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/21/18 2:54 PM
 * Description: 飞控模块的生命周期回调，有 FlightAssistant 子模块
 */
public interface IFlightControllerLifecycleObserver extends IDroneLifecycleObserver {
    void onFlightControllerConnected();
    void onFlightControllerDisconnected();
}
