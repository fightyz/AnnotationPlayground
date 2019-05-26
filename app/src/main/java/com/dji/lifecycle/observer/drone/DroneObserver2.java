package com.dji.lifecycle.observer.drone;

import android.util.Log;

import com.dji.lifecycle.LifecycleException;
import com.dji.lifecycle.ObserverHolder2;
import com.dji.lifecycle.SubscriberMethod;
import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.meta.SubscriberInfo;
import com.dji.lifecycle.meta.SubscriberInfoIndex;
import com.dji.lifecycle.observer.AbstractObserver2;
import com.dji.lifecycleannotationplayground.AnnotationTest1;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import dji.lifecycle.annotation.OnProductConnected;
import dji.lifecycle.annotation.OnProductDisconnected;

/**
 * 由 apt 生成，因为需要 FpvLifecycleTest, FpvLifecycleTest2 等类的信息。
 * subscriberInfoIndex 可以由 dagger2 来注入。
 */
public class DroneObserver2 extends AbstractObserver2 {

    private SubscriberInfoIndex subscriberInfoIndex;

    public DroneObserver2(SubscriberInfoIndex subscriberInfoIndex) {
        super(AnnotationTest1.class);
        this.subscriberInfoIndex = subscriberInfoIndex;
    }

    @Override
    public void notifyEvent(IEvent event) {
        if (!(event instanceof DroneEvent)) {
            return;
        }
        DroneEvent droneEvent = (DroneEvent) event;
        Set<ObserverHolder2> observers = getObservers();
        for (ObserverHolder2 holder : observers) {
            Class clazz = holder.clazz;

            if (droneEvent.isProductConnected()) {
                incrementHolder(holder);
            }

            SubscriberInfo subscriberInfo = subscriberInfoIndex.getSubscriberInfo(clazz);
            Log.e("jojo", "notifyEvent: " + subscriberInfo);
            SubscriberMethod[] subscriberMethods = subscriberInfo.getSubscriberMethods();
            try {
                for (SubscriberMethod subscriberMethod : subscriberMethods) {
                    Log.e("jojo", "subscriberMethod: " + subscriberMethod);
                    Class annotationType = subscriberMethod.mAnnotation.annotationType();
                    if (annotationType.equals(OnProductConnected.class) ||
                            annotationType.equals(OnProductDisconnected.class)) {
                        subscriberMethod.mMethod.invoke(holder.instance);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new LifecycleException(e);
            } catch (InvocationTargetException e) {
                throw new LifecycleException(e);
            }

            if (droneEvent.isProductDisconnected()) {
                decrementHolder(holder);
            }
        }
    }
}