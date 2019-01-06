package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.interfaces.observer.IAirLinkLifecycleObserver;
import com.dji.lifecycle.observer.exception.InvalidEventExcption;

import java.util.Set;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/30/18 7:45 PM
 * Description: 飞机链路连接事件观察者管理类，负责向观察者回调事件
 */
public class AirLinkObserver extends AbstractObserver<IAirLinkLifecycleObserver> {

    private static final String TAG = "AirLinkLifecycleObserve";

    AirLinkObserver(Class<IAirLinkLifecycleObserver> observerClass) {
        super(observerClass);
    }

    @Override
    protected void notifyEvent(DroneEvent event) {
        Set<ObserverHolder<IAirLinkLifecycleObserver>> observers = getObservers();
        for (ObserverHolder<IAirLinkLifecycleObserver> observer : observers) {
            if (event.isAirLinkConnected()) {
                observer.instance.onAirLinkConnected();
                observer.counter++;
            } else if (event.isAirLinkDisconnected()) {
                observer.instance.onAirLinkDisconnected();
                observer.counter--;
            } else {
                throw new InvalidEventExcption(event);
            }
        }

        recycleObservers();
    }
}
