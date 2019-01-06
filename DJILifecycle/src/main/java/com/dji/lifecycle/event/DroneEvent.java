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

    DroneEvent(int event) {
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

    public boolean isCameraLifecycle() {
        return event == IEvent.ON_CAMERA_CONNECTED || event == IEvent.ON_CAMERA_DISCONNECTED;
    }

    public boolean isCameraConnected() {
        return event == IEvent.ON_CAMERA_CONNECTED;
    }

    public boolean isCameraDisconnected() {
        return event == IEvent.ON_CAMERA_DISCONNECTED;
    }

    public boolean isBatteryLifecycle() {
        return event == IEvent.ON_BATTERY_CONNECTED || event == IEvent.ON_BATTERY_DISCONNECTED;
    }

    public boolean isBatteryConnected() {
        return event == IEvent.ON_BATTERY_CONNECTED;
    }

    public boolean isBatteryDisconnected() {
        return event == IEvent.ON_BATTERY_DISCONNECTED;
    }

    public boolean isGimbalLifecycle() {
        return event == IEvent.ON_GIMBAL_CONNECTED || event == IEvent.ON_GIMBAL_DISCONNECTED;
    }

    public boolean isGimbalConnected() {
        return event == IEvent.ON_GIMBAL_CONNECTED;
    }

    public boolean isGimbalDisconnected() {
        return event == IEvent.ON_GIMBAL_DISCONNECTED;
    }

    public boolean isFlightControllerLifecycle() {
        return event == IEvent.ON_FLIGHT_CONTROLLER_CONNECTED || event == IEvent.ON_FLIGHT_CONTROLLER_DISCONNECTED;
    }

    public boolean isFlightControllerConnected() {
        return event == IEvent.ON_FLIGHT_CONTROLLER_CONNECTED;
    }

    public boolean isFlightControllerDisconnected() {
        return event == IEvent.ON_FLIGHT_CONTROLLER_DISCONNECTED;
    }

    public boolean isAirLinkLifecycle() {
        return event == IEvent.ON_AIR_LINK_CONNECTED || event == IEvent.ON_AIR_LINK_DISCONNECTED;
    }

    public boolean isAirLinkConnected() {
        return event == IEvent.ON_AIR_LINK_CONNECTED;
    }

    public boolean isAirLinkDisconnected() {
        return event == IEvent.ON_AIR_LINK_DISCONNECTED;
    }

    public boolean isRemoteControllerLifecycle() {
        return event == IEvent.ON_REMOTE_CONTROLLER_CONNECTED || event == IEvent.ON_REMOTE_CONTROLEEER_DISCONNECTED;
    }

    public boolean isRemoteControllerConnected() {
        return event == IEvent.ON_REMOTE_CONTROLLER_CONNECTED;
    }

    public boolean isRemoteControllerDisconnected() {
        return event == IEvent.ON_REMOTE_CONTROLEEER_DISCONNECTED;
    }

    @Override
    public String toString() {
        return "DroneEvent{" +
                "event=" + event +
                '}';
    }
}
