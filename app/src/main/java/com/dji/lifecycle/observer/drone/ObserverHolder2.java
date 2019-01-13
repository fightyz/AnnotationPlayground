package com.dji.lifecycle.observer.drone;

import java.util.Objects;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/5/19 3:49 PM
 * Description: ${TODO}
 */
public class ObserverHolder2 {

    Class clazz;

    Object instance;

    int counter;

    ObserverHolder2(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObserverHolder2)) return false;
        ObserverHolder2 that = (ObserverHolder2) o;
        return Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(instance);
    }
}
