package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.interfaces.observer.IDroneLifecycleObserver;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest2;
import com.dji.lifecycle.interfaces.util.LifecycleLog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/28/18 7:50 PM
 * Description: 管理所有生命周期观察者实例的 创建 和 销毁
 */
public abstract class AbstractObserver<T extends IDroneLifecycleObserver> {

    /**
     * 观察者、实例的对应静态表
     */
    protected static Map<Class<? extends IDroneLifecycleObserver>, ObserverHolder> triplets = new HashMap();
    {
        triplets.put(FpvLifecycleTest.class, new ObserverHolder());
        triplets.put(FpvLifecycleTest2.class, new ObserverHolder());
//        triplets.put(DJIGSManager.class, new ObserverHolder());
    }

    /**
     * 实现了接口 {@linkplain T} 的类 .class 集合
     */
    protected Set<Class<T>> observerClasses = new HashSet<>();

    @SuppressWarnings("unchecked")
    AbstractObserver(Class<T> observerClass) {
        for (Class<? extends IDroneLifecycleObserver> clazz : triplets.keySet()) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class interfaze : interfaces) {
                if (interfaze.isAssignableFrom(observerClass)) {
                    addObserverClass(clazz);
                    break;
                }
            }
        }
    }

    /**
     * 获取业务的实例对象。如果没有实例则创建，如果已经有则直接获取并加入返回集合
     * @return 实例集合
     */
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    protected Set<ObserverHolder<T>> getObservers() {
        Set<ObserverHolder<T>> observers = new HashSet<>();
        for (Class<T> clazz : observerClasses) {
            if (clazz == FpvLifecycleTest.class) {
                LifecycleLog.i("FpvLifecycleTest instance counter " + triplets.get(clazz).counter);
                if (triplets.get(clazz).counter == 0) {
                    triplets.get(clazz).instance = new FpvLifecycleTest();
                }
            }
            if (clazz == FpvLifecycleTest2.class) {
                LifecycleLog.i("FpvLifecycleTest2 instance counter " + triplets.get(clazz).counter);
                if (triplets.get(clazz).counter == 0) {
                    triplets.get(clazz).instance = new FpvLifecycleTest2();
                }
            }
            observers.add(triplets.get(clazz));
        }
        return observers;
    }

    /**
     * 将实现了 {@linkplain T} 接口的 class 加入到集合中
     * @param clazz {@linkplain T} 类事件的观察者 class 类
     */
    @SuppressWarnings("unchecked")
    private void addObserverClass(Class clazz) {
        observerClasses.add(clazz);
    }

    /**
     * 当计数器 {@linkplain ObserverHolder#counter} 为 0 时，销毁实例
     */
    @SuppressWarnings("ConstantConditions")
    protected void recycleObservers() {
        for (Class clazz : observerClasses) {
            LifecycleLog.i("cleanObserver " + clazz + ", counter " + triplets.get(clazz).counter);
            if (triplets.get(clazz).counter == 0) {
                triplets.get(clazz).instance = null;
            }
        }
    }

    /**
     * 向观察者的生命周期事件回调
     * @param event 相应实现类所在生命周期的事件 {@code com.dji.lifecycle.observer.ILifecycle.Event Event}
     */
    protected abstract void notifyEvent(DroneEvent event);

    public static class ObserverHolder <U extends IDroneLifecycleObserver> {
        /**
         * 生命周期观察者的类实例
         */
        protected U instance;

        /**
         * 该实例存活于生命周期个数的计数器
         */
        protected int counter;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ObserverHolder)) {
                return false;
            }
            ObserverHolder that = (ObserverHolder) o;
            return Objects.equals(instance, that.instance);
        }

        @Override
        public int hashCode() {

            return Objects.hash(instance);
        }
    }
}
