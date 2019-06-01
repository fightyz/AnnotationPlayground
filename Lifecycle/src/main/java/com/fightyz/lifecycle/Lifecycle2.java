package com.fightyz.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.fightyz.lifecycle.event.ActivityEvent;
import com.fightyz.lifecycle.event.IEvent;
import com.fightyz.lifecycle.event.ProductEvent;
import com.fightyz.lifecycle.meta.SubscriberInfoIndex;
import com.fightyz.lifecycle.observer.ActivityObserver;
import com.fightyz.lifecycle.observer.ProductObserver2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Lifecycle2 implements ProductConnectionListener {

    private static final String TAG = "Lifecycle2";

    private static final String SPECIFIC_ACTIVITY = "MainActivity";

    private static Lifecycle2 sInstance;

    private ActivityObserver activityObserver;
    private ProductObserver2 productObserver2;

    private static synchronized Lifecycle2 build(Application application) {
        if (sInstance == null) {
            sInstance = new Lifecycle2(application);
        } else {
            throw new RuntimeException("Lifecycle is already built");
        }
        return sInstance;
    }

    private Lifecycle2(Application application) {
        Observable.timer(9, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> onProductConnected());

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                if (activity.getClass().getSimpleName().contains(SPECIFIC_ACTIVITY)) {
                    activityObserver.notifyEvent(new ActivityEvent(IEvent.ON_CREATE));
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
                    activityObserver.notifyEvent(new ActivityEvent(IEvent.ON_DESTROY));
                }
            }
        });
    }

    @Override
    public void onProductConnected() {
        productObserver2.notifyEvent(new ProductEvent(IEvent.ON_PRODUCT_CONNECTED));
    }

    @Override
    public void onProductDisconnected() {
        productObserver2.notifyEvent(new ProductEvent(IEvent.ON_PRODUCT_DISCONNECTED));
    }

    public static class Builder {

        private List<SubscriberInfoIndex> indexList = new LinkedList<>();

        public Builder addIndex(SubscriberInfoIndex index) {
            indexList.add(index);
            return this;
        }

        public Lifecycle2 build(Application application) {
            Lifecycle2 lifecycle2 = Lifecycle2.build(application);
            int size = indexList.size();
            SubscriberInfoIndex[] indexArray = indexList.toArray(new SubscriberInfoIndex[size]);
            for (SubscriberInfoIndex subscriberInfoIndex : indexArray) {
                Log.d(TAG, "build subscriberInfoIndex " + subscriberInfoIndex);
            }
            ActivityObserver activityObserver = new ActivityObserver(indexArray);
            ProductObserver2 productObserver = new ProductObserver2(indexArray);
            lifecycle2.activityObserver = activityObserver;
            lifecycle2.productObserver2 = productObserver;
            return lifecycle2;
        }
    }
}
