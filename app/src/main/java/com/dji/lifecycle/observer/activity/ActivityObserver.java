package com.dji.lifecycle.observer.activity;

import android.util.Log;

import com.dji.lifecycle.LifecycleException;
import com.dji.lifecycle.ObserverHolder2;
import com.dji.lifecycle.SubscriberMethod;
import com.dji.lifecycle.event.ActivityEvent;
import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.meta.SubscriberInfo;
import com.dji.lifecycle.meta.SubscriberInfoIndex;
import com.dji.lifecycle.observer.AbstractObserver2;
import com.dji.lifecycleannotationplayground.AnnotationTest1;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import dji.lifecycle.annotation.OnActivityCreated;
import dji.lifecycle.annotation.OnProductConnected;
import dji.lifecycle.annotation.OnProductDisconnected;
import dji.lifecycle.annotation.onActivityDestroy;

public class ActivityObserver extends AbstractObserver2 {

    private SubscriberInfoIndex subscriberInfoIndex;

    public ActivityObserver(SubscriberInfoIndex subscriberInfoIndex) {
        super(AnnotationTest1.class);
        this.subscriberInfoIndex = subscriberInfoIndex;
    }

    @Override
    public void notifyEvent(IEvent event) {
        if (!(event instanceof ActivityEvent)) {
            return;
        }
        ActivityEvent activityEvent = (ActivityEvent) event;
        Set<ObserverHolder2> observers = getObservers();
        for (ObserverHolder2 holder : observers) {
            Class clazz = holder.clazz;

            if (activityEvent.isActivityCreated()) {
                incrementHolder(holder);
            }

            SubscriberInfo subscriberInfo = subscriberInfoIndex.getSubscriberInfo(clazz);
            SubscriberMethod[] subscriberMethods = subscriberInfo.getSubscriberMethods();
            try {
                for (SubscriberMethod subscriberMethod : subscriberMethods) {
                    Class annotationType = subscriberMethod.mAnnotation.annotationType();
                    if (annotationType.equals(OnActivityCreated.class) ||
                            annotationType.equals(onActivityDestroy.class)) {
                        subscriberMethod.mMethod.invoke(holder.instance);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new LifecycleException(e);
            } catch (InvocationTargetException e) {
                throw new LifecycleException(e);
            }

            if (activityEvent.isActivityDestroy()) {
                decrementHolder(holder);
            }
        }
    }
}
