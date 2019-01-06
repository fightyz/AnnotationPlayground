package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.interfaces.observer.IAirLinkLifecycleObserver;
import com.dji.lifecycle.interfaces.observer.IBatteryLifecycleObserver;
import com.dji.lifecycle.interfaces.observer.ICameraLifecycleObserver;
import com.dji.lifecycle.interfaces.observer.IFlightControllerLifecycleObserver;
import com.dji.lifecycle.interfaces.observer.IGimbalLifecyclObserver;
import com.dji.lifecycle.interfaces.observer.IProductLifecycleObserver;
import com.dji.lifecycle.interfaces.observer.IRemoteControllerObserver;
import com.dji.lifecycle.interfaces.util.LifecycleLog;
import com.dji.lifecycle.observer.AbstractLifecycleObserver;
import com.dji.lifecycle.observer.exception.InvalidEventExcption;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/27/18 11:07 AM
 * Description:
 * 1. 单例，注册于 {@code LifecycleObservable} 上，
 * 关注于飞机的生命周期
 * 2. 关注飞机生命周期的业务类挂在于该类上，由该类负责创建和销毁业务实例
 */
public class DroneLifecycleObserver extends AbstractLifecycleObserver {

    private static final String TAG = "DroneLifecycleObserver";

    private ProductObserver productObserver = new ProductObserver(IProductLifecycleObserver.class);
    private FlightControllerObserver flightControllerObserver = new FlightControllerObserver(IFlightControllerLifecycleObserver.class);
    private AirLinkObserver airLinkObserver = new AirLinkObserver(IAirLinkLifecycleObserver.class);
    private BatteryObserver batteryObserver = new BatteryObserver(IBatteryLifecycleObserver.class);
    private CameraObserver cameraObserver = new CameraObserver(ICameraLifecycleObserver.class);
    private GimbalObserver gimbalObserver = new GimbalObserver(IGimbalLifecyclObserver.class);
    private RemoteControllerObserver remoteControllerObserver = new RemoteControllerObserver(IRemoteControllerObserver.class);

    public DroneLifecycleObserver() {}

    @Override
    public void handleLifecycleEvent(IEvent event) {
        LifecycleLog.i(TAG, "handleLifecycleEvent " + event + ", " + Thread.currentThread());
        DroneEvent droneEvent = (DroneEvent) event;
        if (droneEvent.isProductLifecycle()) {
            productObserver.notifyEvent(droneEvent);
        } else if (droneEvent.isFlightControllerLifecycle()) {
            flightControllerObserver.notifyEvent(droneEvent);
        } else if (droneEvent.isAirLinkLifecycle()) {
            airLinkObserver.notifyEvent(droneEvent);
        } else if (droneEvent.isBatteryLifecycle()) {
            batteryObserver.notifyEvent(droneEvent);
        } else if (droneEvent.isCameraLifecycle()) {
            cameraObserver.notifyEvent(droneEvent);
        } else if (droneEvent.isGimbalLifecycle()) {
            gimbalObserver.notifyEvent(droneEvent);
        } else if (droneEvent.isRemoteControllerLifecycle()) {
            remoteControllerObserver.notifyEvent(droneEvent);
        } else {
            throw new InvalidEventExcption(event);
        }
    }
}
