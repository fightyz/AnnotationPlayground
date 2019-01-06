package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.interfaces.observer.IFlightControllerLifecycleObserver;
import com.dji.lifecycle.observer.exception.InvalidEventExcption;

import java.util.Set;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/30/18 1:41 PM
 * Description: 飞机飞控连接事件观察者管理类，负责向观察者回调事件
 */
public class FlightControllerObserver extends AbstractObserver<IFlightControllerLifecycleObserver> {

    private static final String TAG = "FlightControllerObserve";

    FlightControllerObserver(Class<IFlightControllerLifecycleObserver> observerClass) {
        super(observerClass);
    }

    @Override
    public void notifyEvent(DroneEvent event) {
        Set<ObserverHolder<IFlightControllerLifecycleObserver>> observers = getObservers();
        for (ObserverHolder<IFlightControllerLifecycleObserver> observer : observers) {
            if (event.isFlightControllerConnected()) {
                observer.instance.onFlightControllerConnected();
                observer.counter++;
            } else if (event.isFlightControllerDisconnected()) {
                observer.instance.onFlightControllerDisconnected();
                observer.counter--;
            } else {
                throw new InvalidEventExcption(event);
            }
        }

        recycleObservers();
    }
}
