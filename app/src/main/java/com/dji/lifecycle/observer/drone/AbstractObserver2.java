package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.LifecycleException;
import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/5/19 2:17 PM
 * Description: ${TODO}
 */
public abstract class AbstractObserver2 {

    private static Map<Class, ObserverHolder2> triplets = new HashMap<>();
    {
        triplets.put(FpvLifecycleTest.class, new ObserverHolder2(FpvLifecycleTest.class));
        triplets.put(FpvLifecycleTest2.class, new ObserverHolder2(FpvLifecycleTest2.class));
    }

    private Set<Class> observerClasses = new HashSet<>();

    AbstractObserver2(Class...classes) {
        observerClasses.addAll(Arrays.asList(classes));
    }

    protected Set<ObserverHolder2> getObservers() {
        Set<ObserverHolder2> observers = new HashSet<>();
        try {
            for (Class clazz : observerClasses) {
                if (triplets.get(clazz).counter == 0) {
                    triplets.get(clazz).instance = clazz.newInstance();
                }
                observers.add(triplets.get(clazz));
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new LifecycleException("Could not create instance.", e);
        }

        return observers;
    }

    protected Set<ObserverHolder2> getObserversByEvent(IEvent event) {
        DroneEvent droneEvent = (DroneEvent) event;
        Set<ObserverHolder2> observers = new HashSet<>();
        if (droneEvent.isProductLifecycle()) {

        }
    }

    protected void recycleObservers() {
        for (Class clazz : observerClasses) {
            if (triplets.get(clazz).counter == 0) {
                triplets.get(clazz).instance = null;
            }
        }
    }

    protected abstract void notifyEvent(IEvent event);
}
