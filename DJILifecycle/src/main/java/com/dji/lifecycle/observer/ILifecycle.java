package com.dji.lifecycle.observer;

import com.dji.lifecycle.event.IEvent;

/**
 * Copyright ©2018 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 12/19/18 4:30 PM
 * Description: 定义 DJI Lifecycle 的相关方法(method)、状态(state)、事件(event)
 */
public interface ILifecycle {

    String TAG = "DJILifecycle";

//    PublishSubject<IEvent> getPublishSubject();

//    void removeObserver(@NonNull ILifecycleObserver observer);

    void handleLifecycleEvent(IEvent event);
}
