package com.dji.lifecycle.event;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/6/19 2:23 PM
 * Description: 生命周期发射的事件基本类型
 */
public interface IEvent {
    int ON_CREATE = 0;
    int ON_START = 1;
    int ON_RESUME = 2;
    int ON_PAUSE = 3;
    int ON_STOP = 4;
    int ON_DESTROY = 5;
    int ON_ANY = 6;

    int ON_PRODUCT_CONNECTED = 7;
    int ON_PRODUCT_DISCONNECTED = 8;

    int UNDEFINED = -1;
}
