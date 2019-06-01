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
import java.util.Set;

/**
 * 由 apt 生成
 */
public class MyLifecycleSubscriberInfoIndex implements SubscriberInfoIndex {

    private static final Map<Class<?>, SubscriberInfo> PRODUCT_SUBSCRIBER_INDEX;
    private static final Map<Class<?>, SubscriberInfo> ACTIVITY_SUBSCRIBER_INDEX;

    static {
        PRODUCT_SUBSCRIBER_INDEX = new HashMap<>();
        ACTIVITY_SUBSCRIBER_INDEX = new HashMap<>();

        putProductIndex(new SimpleSubscriberInfo(AnnotationTest1.class, new SubscriberMethodInfo[]{
                new SubscriberMethodInfo<>("onProductConnected", OnProductConnected.class)
        }));

        putActivityIndex(new SimpleSubscriberInfo(AnnotationTest1.class, new SubscriberMethodInfo[]{
                new SubscriberMethodInfo<>("onActivityCreated", OnActivityCreated.class)
        }));
    }

    @Override
    public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
        SubscriberInfo productInfo = PRODUCT_SUBSCRIBER_INDEX.get(subscriberClass);
        SubscriberInfo activityInfo = ACTIVITY_SUBSCRIBER_INDEX.get(subscriberClass);

        if (productInfo != null && activityInfo != null) {
            return SimpleSubscriberInfo.combine(productInfo, activityInfo);
        } else if (productInfo != null) {
            return productInfo;
        } else {
            return activityInfo;
        }
    }

    @Override
    public Set<Class<?>> getActivitySubscriberClasses() {
        return ACTIVITY_SUBSCRIBER_INDEX.keySet();
    }

    @Override
    public Set<Class<?>> getProductSubscriberClasses() {
        return PRODUCT_SUBSCRIBER_INDEX.keySet();
    }

    private static void putProductIndex(SubscriberInfo info) {
        PRODUCT_SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info);
    }

    private static void putActivityIndex(SubscriberInfo info) {
        ACTIVITY_SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info);
    }
}
