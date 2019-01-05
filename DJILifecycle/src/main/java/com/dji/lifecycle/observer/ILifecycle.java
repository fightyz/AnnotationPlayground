package com.dji.lifecycle.observer;


import java.util.Arrays;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/19/18 4:30 PM
 * Description: 定义 DJI Lifecycle 的相关方法(method)、状态(state)、事件(event)
 */
public interface ILifecycle {

    String TAG = "DJILifecycle";

    PublishSubject<Event> getPublishSubject();

    void handleLifecycleEvent(Event event);

    /**
     * 生命周期的事件。我们认为生命周期的事件应该是成对出现的。观察者注册时也应该成对注册
     */
    enum Event {
        ON_CREATE,
        ON_START,
        ON_RESUME,
        ON_PAUSE,
        ON_STOP,
        ON_DESTROY,
        ON_ANY,

        ON_PRODUCT_CONNECTED,
        ON_PRODUCT_DISCONNECTED,

        ON_CAMERA_CONNECTED,
        ON_CAMERA_DISCONNECTED,

        ON_BATTERY_CONNECTED,
        ON_BATTERY_DISCONNECTED,

        ON_GIMBAL_CONNECTED,
        ON_GIMBAL_DISCONNECTED,

        ON_FLIGHT_CONTROLLER_CONNECTED,
        ON_FLIGHT_CONTROLLER_DISCONNECTED,

        ON_AIR_LINK_CONNECTED,
        ON_AIR_LINK_DISCONNECTED,

        ON_REMOTE_CONTROLLER_CONNECTED,
        ON_REMOTE_CONTROLEEER_DISCONNECTED,

        UNDEFINED;

        public static List<Event> getActivityCreateEvents() {
            return Arrays.asList(ON_CREATE, ON_START, ON_RESUME);
        }

        public boolean isActivityCreateEvent() {
            return getActivityCreateEvents().contains(this);
        }

        public boolean isActivityLifecycle() {
            return equals(ON_CREATE) || equals(ON_START) || equals(ON_RESUME)
                    || equals(ON_PAUSE) || equals(ON_STOP) || equals(ON_DESTROY);
        }

        public boolean isProductLifecycle() {
            return equals(ON_PRODUCT_CONNECTED) || equals(ON_PRODUCT_DISCONNECTED);
        }

        public boolean isCameraLifecycle() {
            return equals(ON_CAMERA_CONNECTED) || equals(ON_CAMERA_DISCONNECTED);
        }

        public boolean isBatteryLifecycle() {
            return equals(ON_BATTERY_CONNECTED) || equals(ON_BATTERY_DISCONNECTED);
        }

        public boolean isGimbalLifecycle() {
            return equals(ON_GIMBAL_CONNECTED) || equals(ON_GIMBAL_DISCONNECTED);
        }

        public boolean isFlightControllerLifecycle() {
            return equals(ON_FLIGHT_CONTROLLER_CONNECTED) || equals(ON_FLIGHT_CONTROLLER_DISCONNECTED);
        }

        public boolean isAirLinkLifecycle() {
            return equals(ON_AIR_LINK_CONNECTED) || equals(ON_AIR_LINK_DISCONNECTED);
        }

        public boolean isRemoteControllerLifecycle() {
            return equals(ON_REMOTE_CONTROLLER_CONNECTED) || equals(ON_REMOTE_CONTROLEEER_DISCONNECTED);
        }
    }

    /**
     * 当前处于哪个生命周期状态。暂时未使用。
     */
    enum State {
        FPV_CREATE,
        FPV_DESTROY,
        ACTIVITY_CREATED,
        ACTIVITY_STARTED,
        ACTIVITY_RESUMED
    }
}
