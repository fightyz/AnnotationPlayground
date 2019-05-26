package com.fightyz.lifecycle.observer.activity;

import com.fightyz.lifecycle.LifecycleException;
import com.fightyz.lifecycle.ObserverHolder2;
import com.fightyz.lifecycle.SubscriberMethod;
import com.fightyz.lifecycle.event.ActivityEvent;
import com.fightyz.lifecycle.event.IEvent;
import com.fightyz.lifecycle.meta.SubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfoIndex;
import com.fightyz.lifecycle.observer.AbstractObserver2;
import com.fightyz.lifecycleannotationplayground.AnnotationTest1;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import com.fightyz.lifecycle.annotation.OnActivityCreated;
import com.fightyz.lifecycle.annotation.onActivityDestroy;

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
