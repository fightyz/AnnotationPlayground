package com.fightyz.lifecycle.observer;

import com.fightyz.lifecycle.annotation.OnActivityCreated;
import com.fightyz.lifecycle.annotation.OnProductConnected;
import com.fightyz.lifecycle.meta.SimpleSubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfoIndex;
import com.fightyz.lifecycle.meta.SubscriberMethodInfo;
import com.fightyz.lifecycleannotationplayground.AnnotationTest1;

import java.util.HashMap;
import java.util.Map;

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
