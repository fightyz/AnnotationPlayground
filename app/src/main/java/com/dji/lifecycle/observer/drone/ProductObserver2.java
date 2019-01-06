package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/5/19 2:46 PM
 * Description: ${TODO}
 */
public class ProductObserver2 extends AbstractObserver2 {

    ProductObserver2() {
        super(FpvLifecycleTest.class);
    }

    @Override
    protected void notifyEvent(IEvent event) {
//        Set<ObserverHolder> observers = getObservers();
//        for (ObserverHolder observerHolder : observers) {
//            if (event == IEvent.ON_PRODUCT_CONNECTED) {
//                Class clazz = observerHolder.clazz;
//                if (FpvLifecycleTest.class == clazz) {
//                    FpvLifecycleTest obj = (FpvLifecycleTest) observerHolder.instance;
//                    obj.onProductConnected();
//                    observerHolder.counter++;
//                }
//            } else if (event == ILifecycle.Event.ON_PRODUCT_DISCONNECTED) {
//                if (FpvLifecycleTest.class == observerHolder.clazz) {
//                    FpvLifecycleTest obj = (FpvLifecycleTest) observerHolder.instance;
//                    obj.onProductDisconnected();
//                    observerHolder.counter--;
//                }
//            }
//        }
//        recycleObservers();
    }
}
