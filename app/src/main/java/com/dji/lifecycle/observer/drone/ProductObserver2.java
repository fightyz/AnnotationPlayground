package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.SubscriberMethod;
import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest2;
import com.dji.lifecycle.meta.SubscriberInfo;
import com.dji.lifecycle.observer.LifecycleSubscriberInfoIndex;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/5/19 2:46 PM
 * Description: ${TODO}
 */
public class ProductObserver2 extends AbstractObserver2 {

    private LifecycleSubscriberInfoIndex subscriberInfoIndex;

    ProductObserver2() {
        super(FpvLifecycleTest.class, FpvLifecycleTest2.class);
    }

    @Override
    protected void notifyEvent(IEvent event) {
        DroneEvent droneEvent = (DroneEvent) event;
        Set<ObserverHolder2> observers = getObservers();
        for (ObserverHolder2 observerHolder : observers) {
            Class clazz = observerHolder.clazz;
            SubscriberInfo subscriberInfo = subscriberInfoIndex.getSubscriberInfo(clazz);
            SubscriberMethod[] subscriberMethods = subscriberInfo.getSubscriberMethods();
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                // 如果 subscriberMethod 中取出的 annotation 是对应了 event 的
                try {
                    subscriberMethod.mMethod.invoke(observerHolder.instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            if (droneEvent.isProductConnected()) {
                Class clazz = observerHolder.clazz;
                if (clazz == FpvLifecycleTest.class) {
                    FpvLifecycleTest obj = (FpvLifecycleTest) observerHolder.instance;
                    obj.onProductConnected(); // 这个是 @onProductConnected 的方法
                } else if (clazz == FpvLifecycleTest2.class) {
                    FpvLifecycleTest2 obj = (FpvLifecycleTest2) observerHolder.instance;
                    obj.onProductConnected(); // 这个是 @onProductConnected 的方法
                }
                observerHolder.counter++;
            } else if (droneEvent.isProductDisconnected()) {
                Class clazz = observerHolder.clazz;
                if (clazz == FpvLifecycleTest.class) {
                    FpvLifecycleTest obj = (FpvLifecycleTest) observerHolder.instance;
                    obj.onProductDisconnected(); // 这个是 @onProductDisconnected 的方法
                } else if (clazz == FpvLifecycleTest2.class) {
                    FpvLifecycleTest2 obj = (FpvLifecycleTest2) observerHolder.instance;
                    obj.onProductDisconnected(); // 这个是 @onProductDisconnected 的方法
                }
                observerHolder.counter--;
            }
        }
        recycleObservers();
    }
}
