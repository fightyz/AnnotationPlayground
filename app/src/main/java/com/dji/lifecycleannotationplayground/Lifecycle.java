package com.dji.lifecycleannotationplayground;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.dji.lifecycle.ProductConnectionListener;
import com.dji.lifecycle.event.ActivityEvent;
import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.observer.AbstractObserver2;
import com.dji.lifecycle.observer.LifecycleSubscriberInfoIndex;
import com.dji.lifecycle.observer.activity.ActivityObserver;
import com.dji.lifecycle.observer.drone.DroneObserver2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Lifecycle implements ProductConnectionListener {

    private static final String SPECIFIC_ACTIVITY = "MainActivity";

    private static Lifecycle sInstance;

    private List<AbstractObserver2> observerList = new LinkedList<>();

    public static synchronized void build(Application application) {
        if (sInstance == null) {
            sInstance = new Lifecycle(application);
        } else {
            throw new RuntimeException("Lifecycle is already built");
        }
    }

    private Lifecycle(Application applicationContext) {
        Observable.timer(9, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    onProductConnected();
                });

        observerList.add(new DroneObserver2(new LifecycleSubscriberInfoIndex()));
        observerList.add(new ActivityObserver(new LifecycleSubscriberInfoIndex()));

        applicationContext.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                if (activity.getClass().getSimpleName().contains(SPECIFIC_ACTIVITY)) {
                    for (AbstractObserver2 observer : observerList) {
                        observer.notifyEvent(new ActivityEvent(IEvent.ON_CREATE));
                    }
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity.getClass().getSimpleName().contains(SPECIFIC_ACTIVITY)) {
                    for (AbstractObserver2 observer : observerList) {
                        observer.notifyEvent(new ActivityEvent(IEvent.ON_DESTROY));
                    }
                }
            }
        });
    }

    @Override
    public void onProductConnected() {
        for (AbstractObserver2 observer : observerList) {
            observer.notifyEvent(new DroneEvent(IEvent.ON_PRODUCT_CONNECTED));
        }
    }

    @Override
    public void onProductDisconnected() {
        for (AbstractObserver2 observer : observerList) {
            observer.notifyEvent(new DroneEvent(IEvent.ON_PRODUCT_DISCONNECTED));
        }
    }
}
