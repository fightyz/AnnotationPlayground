package com.fightyz.lifecycle.observer.drone;

import android.util.Log;

import com.fightyz.lifecycle.LifecycleException;
import com.fightyz.lifecycle.ObserverHolder2;
import com.fightyz.lifecycle.SubscriberMethod;
import com.fightyz.lifecycle.annotation.OnProductConnected;
import com.fightyz.lifecycle.annotation.OnProductDisconnected;
import com.fightyz.lifecycle.event.ProductEvent;
import com.fightyz.lifecycle.event.IEvent;
import com.fightyz.lifecycle.meta.SubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfoIndex;
import com.fightyz.lifecycle.observer.AbstractObserver2;
import com.fightyz.lifecycleannotationplayground.AnnotationTest1;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

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
        if (!(event instanceof ProductEvent)) {
            return;
        }
        ProductEvent productEvent = (ProductEvent) event;
        Set<ObserverHolder2> observers = getObservers();
        for (ObserverHolder2 holder : observers) {
            Class clazz = holder.clazz;

            if (productEvent.isProductConnected()) {
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

            if (productEvent.isProductDisconnected()) {
                decrementHolder(holder);
            }
        }
    }
}
