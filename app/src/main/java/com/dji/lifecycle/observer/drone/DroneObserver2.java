package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.SubscriberMethod;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest2;
import com.dji.lifecycle.meta.SubscriberInfo;
import com.dji.lifecycle.observer.LifecycleSubscriberInfoIndex;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class DroneObserver2 extends AbstractObserver2 {

    private LifecycleSubscriberInfoIndex subscriberInfoIndex;

    DroneObserver2() {
        super(FpvLifecycleTest.class, FpvLifecycleTest2.class);
    }

    @Override
    protected void notifyEvent(IEvent event) {
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
        }
    }
}
