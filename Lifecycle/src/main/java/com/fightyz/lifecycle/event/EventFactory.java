package com.fightyz.lifecycle.event;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/6/19 2:49 PM
 * Description: 生命周期事件的工厂方法
 */
public class EventFactory {

    public static IEvent createDroneEvent(int event) {
        return new ProductEvent(event);
    }
}
