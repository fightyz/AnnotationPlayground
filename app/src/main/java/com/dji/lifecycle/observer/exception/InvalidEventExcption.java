package com.dji.lifecycle.observer.exception;

import com.dji.lifecycle.event.IEvent;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/4/19 4:55 PM
 * Description: 处理的 event 时抛出的异常
 */
public class InvalidEventExcption extends RuntimeException {

    public InvalidEventExcption(IEvent event) {
        super("Invalid event " + event);
    }
}
