package com.dji.lifecycle.event;

public class ActivityEvent implements IEvent {
    private final int event;

    public ActivityEvent(int event) {
        this.event = event;
    }

    public boolean isActivityCreated() {
        return event == IEvent.ON_CREATE;
    }

    public boolean isActivityDestroy() {
        return event == IEvent.ON_DESTROY;
    }

    @Override
    public String toString() {
        return "ActivityEvent{" +
                "event=" + event +
                '}';
    }
}
