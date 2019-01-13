package com.dji.lifecycle.observer.drone;

import com.dji.lifecycle.event.DroneEvent;
import com.dji.lifecycle.event.IEvent;
import com.dji.lifecycle.observer.AbstractLifecycleObserver;

public class DroneLifecycleObserver2 extends AbstractLifecycleObserver {

    private static final String TAG = "DroneLifecycleObserver2";

    private ProductObserver2 productObserver2 = new ProductObserver2();

    public DroneLifecycleObserver2() {}

    @Override
    public void handleLifecycleEvent(IEvent event) {
        DroneEvent droneEvent = (DroneEvent) event;
        if (droneEvent.isProductLifecycle()) {
            productObserver2.notifyEvent(droneEvent);
        }
    }
}
