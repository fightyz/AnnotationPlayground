package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.interfaces.observer.IProductLifecycleObserver;
import com.dji.lifecycle.observer.exception.InvalidEventExcption;

import java.util.Set;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/27/18 4:02 PM
 * Description: 飞机连接事件观察者管理类，负责向观察者回调事件
 */
public class ProductObserver extends AbstractObserver<IProductLifecycleObserver> {

    private static final String TAG = "ProductObserver";

    ProductObserver(Class<IProductLifecycleObserver> observerClass) {
        super(observerClass);
    }

    @Override
    public void notifyEvent(DroneEvent event) {
        Set<ObserverHolder<IProductLifecycleObserver>> observers = getObservers();
        for (ObserverHolder<IProductLifecycleObserver> observer : observers) {
            if (event.isProductConnected()) {
                observer.instance.onProductConnected();
                observer.counter++;
            } else if (event.isProductDisconnected()) {
                observer.instance.onProductDisconnected();
                observer.counter--;
            } else {
                throw new InvalidEventExcption(event);
            }
        }

        recycleObservers();
    }
}
