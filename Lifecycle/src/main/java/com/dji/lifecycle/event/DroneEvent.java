package com.dji.lifecycle.event;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/6/19 2:37 PM
 * Description: 飞机的生命周期事件
 */
public class DroneEvent implements IEvent {
    private final int event;

    public DroneEvent(int event) {
        this.event = event;
    }

    public boolean isProductLifecycle() {
        return event == IEvent.ON_PRODUCT_CONNECTED || event == IEvent.ON_PRODUCT_DISCONNECTED;
    }

    public boolean isProductConnected() {
        return event == IEvent.ON_PRODUCT_CONNECTED;
    }

    public boolean isProductDisconnected() {
        return event == IEvent.ON_PRODUCT_DISCONNECTED;
    }

    @Override
    public String toString() {
        return "DroneEvent{" +
                "event=" + event +
                '}';
    }
}
