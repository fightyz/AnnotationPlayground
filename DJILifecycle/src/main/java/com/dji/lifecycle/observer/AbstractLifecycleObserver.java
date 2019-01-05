package com.dji.lifecycle.observer;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/27/18 8:18 PM
 * Description: 生命周期观察者的抽象类，主要使所有观察者都在一个线程 {@link AbstractLifecycleObserver#executors} 中订阅
 */
public abstract class AbstractLifecycleObserver implements ILifecycle {

    private static final String TAG = "AbsLifecycleObserver";

    private static Executor executors = Executors.newSingleThreadExecutor(r -> new Thread(r, "lifecycle"));

    protected PublishSubject<Event> publishSubject = PublishSubject.create();

    protected AbstractLifecycleObserver() {
        subscribe(publishSubject.hide());
    }

    @Override
    public PublishSubject<Event> getPublishSubject() {
        return publishSubject;
    }

    private void subscribe(Observable<Event> observable) {
        observable.observeOn(Schedulers.from(executors))
                .subscribe(event -> handleLifecycleEvent(event));
    }
}
