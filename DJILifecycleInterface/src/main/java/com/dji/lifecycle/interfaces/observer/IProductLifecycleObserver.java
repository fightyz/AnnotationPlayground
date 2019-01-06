package com.dji.lifecycle.interfaces.observer;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/20/18 9:23 PM
 * Description: 产品识别的生命周期回调
 */
public interface IProductLifecycleObserver extends IDroneLifecycleObserver {
    void onProductConnected();
    void onProductDisconnected();
}
