package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.interfaces.observer.ICameraLifecycleObserver;
import com.dji.lifecycle.observer.exception.InvalidEventExcption;

import java.util.Set;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/2/19 11:16 AM
 * Description: 相机连接事件观察者管理类，负责向观察者回调事件
 */
public class CameraObserver extends AbstractObserver<ICameraLifecycleObserver> {

    private static final String TAG = "CameraObserver";

    CameraObserver(Class<ICameraLifecycleObserver> observerClass) {
        super(observerClass);
    }

    @Override
    protected void notifyEvent(DroneEvent event) {
        Set<ObserverHolder<ICameraLifecycleObserver>> observers = getObservers();
        for (ObserverHolder<ICameraLifecycleObserver> observer : observers) {
            if (event.isCameraConnected()) {
                observer.instance.onCameraConnected();
                observer.counter++;
            } else if (event.isCameraDisconnected()) {
                observer.instance.onCameraDisconnected();
                observer.counter--;
            } else {
                throw new InvalidEventExcption(event);
            }
        }

        recycleObservers();
    }
}
