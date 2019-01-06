package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.interfaces.observer.IBatteryLifecycleObserver;
import com.dji.lifecycle.observer.exception.InvalidEventExcption;

import java.util.Set;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/31/18 11:34 AM
 * Description: 电池连接事件观察者管理类，负责向观察者回调事件
 */
public class BatteryObserver extends AbstractObserver<IBatteryLifecycleObserver> {

    private static final String TAG = "BatteryObserver";

    public BatteryObserver(Class<IBatteryLifecycleObserver> observerClass) {
        super(observerClass);
    }

    @Override
    protected void notifyEvent(DroneEvent event) {
        Set<ObserverHolder<IBatteryLifecycleObserver>> observers = getObservers();
        for (ObserverHolder<IBatteryLifecycleObserver> observer : observers) {
            if (event.isBatteryConnected()) {
                observer.instance.onBatteryConnected();
                observer.counter++;
            } else if (event.isBatteryDisconnected()) {
                observer.instance.onBatteryDisconnected();
                observer.counter--;
            } else {
                throw new InvalidEventExcption(event);
            }
        }

        recycleObservers();
    }
}
