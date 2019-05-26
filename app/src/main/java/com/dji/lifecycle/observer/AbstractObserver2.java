package com.dji.lifecycle.observer;

import com.dji.lifecycle.LifecycleException;
import com.dji.lifecycle.ObserverHolder2;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycleannotationplayground.AnnotationTest1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/5/19 2:17 PM
 * Description: apt 生成
 */
public abstract class AbstractObserver2 {

    private static Map<Class, ObserverHolder2> triplets = new HashMap<>();

    {
        // 由 apt 添加，所有关心生命周期的类集合
//        triplets.put(FpvLifecycleTest.class, new ObserverHolder2(FpvLifecycleTest.class));
//        triplets.put(FpvLifecycleTest2.class, new ObserverHolder2(FpvLifecycleTest2.class));
        triplets.put(AnnotationTest1.class, new ObserverHolder2(AnnotationTest1.class));
    }

    protected Set<Class> observerClasses = new HashSet<>();

    /**
     * 每个具体的 Observer 传进来的类
     *
     * @param classes
     */
    public AbstractObserver2(Class... classes) {
        observerClasses.addAll(Arrays.asList(classes));
    }

    protected Set<ObserverHolder2> getObservers() {
        Set<ObserverHolder2> observers = new HashSet<>();
        for (Class clazz : observerClasses) {
            ObserverHolder2 holder = triplets.get(clazz);
            observers.add(holder);
        }
        return observers;
    }

    protected void incrementHolder(ObserverHolder2 holder) {
        if (holder.counter == 0) {
            try {
                holder.instance = holder.clazz.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new LifecycleException("Could not create instance.", e);
            }
        }
        holder.counter++;
    }

    protected void decrementHolder(ObserverHolder2 holder) {
        holder.counter--;
        if (holder.counter == 0) {
            holder.instance = null;
        }
    }

    public abstract void notifyEvent(IEvent event);
}
