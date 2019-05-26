package com.dji.lifecycle.observer;

import com.dji.lifecycle.LifecycleException;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest;
import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest2;
import com.dji.lifecycle.meta.SimpleSubscriberInfo;
import com.dji.lifecycle.meta.SubscriberInfo;
import com.dji.lifecycle.meta.SubscriberInfoIndex;
import com.dji.lifecycle.meta.SubscriberMethodInfo;
import com.dji.lifecycle.ObserverHolder2;
import com.dji.lifecycleannotationplayground.AnnotationTest1;

import java.util.HashMap;
import java.util.Map;

import dji.lifecycle.annotation.OnActivityCreated;
import dji.lifecycle.annotation.OnProductConnected;
import dji.lifecycle.annotation.OnProductDisconnected;

/**
 * 由 apt 生成
 */
public class LifecycleSubscriberInfoIndex implements SubscriberInfoIndex {

    private static final Map<Class<?>, SubscriberInfo> SUBSCRIBER_INDEX;


    static {
        SUBSCRIBER_INDEX = new HashMap<>();
        putIndex(new SimpleSubscriberInfo(AnnotationTest1.class, new SubscriberMethodInfo[]{
                new SubscriberMethodInfo("onProductConnected", OnProductConnected.class),
                new SubscriberMethodInfo("onActivityCreated", OnActivityCreated.class)
        }));
//        putIndex(new SimpleSubscriberInfo(FpvLifecycleTest2.class, new SubscriberMethodInfo[]{
//                new SubscriberMethodInfo("onProductConnected", OnProductConnected.class),
//                new SubscriberMethodInfo("onProductDisconnected", OnProductDisconnected.class)
//        }));
    }

    @Override
    public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
        SubscriberInfo info = SUBSCRIBER_INDEX.get(subscriberClass);
        if (info != null) {
            return info;
        } else {
            return null;
        }
    }

    private static void putIndex(SubscriberInfo info) {
        SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info);
    }
}
