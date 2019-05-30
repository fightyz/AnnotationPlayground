package com.fightyz.lifecycle.observer;

import com.fightyz.lifecycle.LifecycleException;
import com.fightyz.lifecycle.ObserverHolder2;
import com.fightyz.lifecycle.event.IEvent;

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

    private Set<Class> observerClasses = new HashSet<>();

    AbstractObserver2() {}

    protected void addObserverClasses(Set<Class<?>> classes) {
        for (Class<?> klass : classes) {
            triplets.put(klass, new ObserverHolder2(klass));
        }
        observerClasses.addAll(classes);
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
