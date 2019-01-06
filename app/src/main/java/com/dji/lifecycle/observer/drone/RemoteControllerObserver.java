package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.interfaces.observer.IRemoteControllerObserver;
import com.dji.lifecycle.observer.exception.InvalidEventExcption;

import java.util.Set;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/2/19 11:26 AM
 * Description: 遥控器连接事件观察者管理类，负责向观察者回调事件
 */
public class RemoteControllerObserver extends AbstractObserver<IRemoteControllerObserver> {

    private static final String TAG = "RemoteController";

    RemoteControllerObserver(Class<IRemoteControllerObserver> observerClass) {
        super(observerClass);
    }

    @Override
    protected void notifyEvent(DroneEvent event) {
        Set<ObserverHolder<IRemoteControllerObserver>> observers = getObservers();
        for (ObserverHolder<IRemoteControllerObserver> observer : observers) {
            if (event.isRemoteControllerConnected()) {
                observer.instance.onRemoteControllerConnected();
                observer.counter++;
            } else if (event.isRemoteControllerDisconnected()) {
                observer.instance.onRemoteControllerDisconnected();
                observer.counter--;
            } else {
                throw new InvalidEventExcption(event);
            }
        }

        recycleObservers();
    }
}
