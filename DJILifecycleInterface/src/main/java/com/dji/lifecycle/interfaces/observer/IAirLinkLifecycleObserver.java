package com.dji.lifecycle.interfaces.observer;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/21/18 2:57 PM
 * Description: 链路模块的生命周期回调，分为以下子模块：SDR(OcuSync), Wifi, LightBridge(LB)
 */
public interface IAirLinkLifecycleObserver extends IDroneLifecycleObserver {
    void onAirLinkConnected();
    void onAirLinkDisconnected();
}
