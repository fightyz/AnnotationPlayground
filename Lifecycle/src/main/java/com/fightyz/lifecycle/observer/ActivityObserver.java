package com.fightyz.lifecycle.observer;

import com.fightyz.lifecycle.LifecycleException;
import com.fightyz.lifecycle.ObserverHolder2;
import com.fightyz.lifecycle.meta.SubscriberMethod;
import com.fightyz.lifecycle.annotation.OnActivityCreated;
import com.fightyz.lifecycle.annotation.OnActivityDestroy;
import com.fightyz.lifecycle.event.ActivityEvent;
import com.fightyz.lifecycle.event.IEvent;
import com.fightyz.lifecycle.meta.SubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfoIndex;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ActivityObserver extends AbstractObserver2 {

    /**
     * 各个 module 生成的 SubscriberInfoIndex
     */
    private Set<SubscriberInfoIndex> subscriberInfoIndexes = new HashSet<>();

    public ActivityObserver(SubscriberInfoIndex... subscriberInfoIndexes) {
        if (subscriberInfoIndexes == null || subscriberInfoIndexes.length == 0) {
            throw new RuntimeException("ActivityObserver subscriberInfoIndexes");
        }
        for (SubscriberInfoIndex infoIndex : subscriberInfoIndexes) {
            addObserverClasses(infoIndex.getActivitySubscriberClasses());
        }
        this.subscriberInfoIndexes.addAll(Arrays.asList(subscriberInfoIndexes));
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

            for (SubscriberInfoIndex subscriberInfoIndex : subscriberInfoIndexes) {
                SubscriberInfo subscriberInfo = subscriberInfoIndex.getSubscriberInfo(clazz);
                if (subscriberInfo == null) {
                    continue;
                }
                SubscriberMethod[] subscriberMethods = subscriberInfo.getSubscriberMethods();
                try {
                    for (SubscriberMethod subscriberMethod : subscriberMethods) {
                        Class annotationType = subscriberMethod.mAnnotation.annotationType();
                        if (annotationType.equals(OnActivityCreated.class) ||
                                annotationType.equals(OnActivityDestroy.class)) {
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
}
