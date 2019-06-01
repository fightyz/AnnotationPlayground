package com.fightyz.lifecycle;

import java.util.Objects;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/5/19 3:49 PM
 * Description: 动态生成
 */
public class ObserverHolder2 {

    public Class clazz;

    public Object instance;

    public int counter;

    public ObserverHolder2(Class clazz) {
        this.clazz = clazz;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObserverHolder2 that = (ObserverHolder2) o;
        return counter == that.counter &&
                Objects.equals(clazz, that.clazz) &&
                Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, instance, counter);
    }

    @Override
    public String toString() {
        return "ObserverHolder2{" +
                "clazz=" + clazz +
                ", instance=" + instance +
                ", counter=" + counter +
                '}';
    }
}
