package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest2;

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

    private static Map<Class, ObserverHolder> triplets = new HashMap();
    {
        triplets.put(FpvLifecycleTest.class, new ObserverHolder(FpvLifecycleTest.class));
        triplets.put(FpvLifecycleTest2.class, new ObserverHolder(FpvLifecycleTest2.class));
    }

    private Set<Class> observerClasses = new HashSet<>();

    AbstractObserver2(Class...classes) {
        for (Class clazz : classes) {
            observerClasses.add(clazz);
        }
    }

    protected Set<ObserverHolder> getObservers() {
        Set<ObserverHolder> observers = new HashSet();
        for (Class clazz : observerClasses) {
            if (clazz == FpvLifecycleTest.class) {
                if (triplets.get(clazz).counter == 0) {
                    triplets.get(clazz).instance = new FpvLifecycleTest();
                }
            }
            if (clazz == FpvLifecycleTest2.class) {
                if (triplets.get(clazz).counter == 0) {
                    triplets.get(clazz).instance = new FpvLifecycleTest2();
                }
            }
            observers.add(triplets.get(clazz));
        }
        return observers;
    }

    protected void recycleObservers() {
        for (Class clazz : observerClasses) {
            if (triplets.get(clazz).counter == 0) {
                triplets.get(clazz).instance = null;
            }
        }
    }

    protected abstract void notifyEvent(IEvent event);

//    public static class ObserverHolder {
//
//        protected Class clazz;
//
//        protected Object instance;
//
//        protected int counter;
//
//        private ObserverHolder(Class clazz) {
//            this.clazz = clazz;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof ObserverHolder)) return false;
//            ObserverHolder that = (ObserverHolder) o;
//            return Objects.equals(instance, that.instance);
//        }
//
//        @Override
//        public int hashCode() {
//
//            return Objects.hash(instance);
//        }
//    }
}
