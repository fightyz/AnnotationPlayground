package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.interfaces.observer.IGimbalLifecyclObserver;
import com.dji.lifecycle.observer.exception.InvalidEventExcption;

import java.util.Set;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/2/19 11:22 AM
 * Description: 云台连接事件观察者管理类，负责向观察者回调事件
 */
public class GimbalObserver extends AbstractObserver<IGimbalLifecyclObserver> {

    private static final String TAG = "GimbalObserver";

    GimbalObserver(Class observerClass) {
        super(observerClass);
    }

    @Override
    protected void notifyEvent(DroneEvent event) {
        Set<ObserverHolder<IGimbalLifecyclObserver>> observers = getObservers();
        for (ObserverHolder<IGimbalLifecyclObserver> observer : observers) {
            if (event.isGimbalConnected()) {
                observer.instance.onGimbalConnected();
                observer.counter++;
            } else if (event.isGimbalDisconnected()) {
                observer.instance.onGimbalDisconnected();
                observer.counter--;
            } else {
                throw new InvalidEventExcption(event);
            }
        }

        recycleObservers();
    }
}
