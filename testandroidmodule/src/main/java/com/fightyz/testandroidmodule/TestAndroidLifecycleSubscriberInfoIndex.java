package com.fightyz.testandroidmodule;

import com.fightyz.lifecycle.annotation.OnActivityCreated;
import com.fightyz.lifecycle.annotation.OnProductConnected;
import com.fightyz.lifecycle.meta.SimpleSubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfoIndex;
import com.fightyz.lifecycle.meta.SubscriberMethodInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestAndroidLifecycleSubscriberInfoIndex implements SubscriberInfoIndex {

    private static final Map<Class<?>, SubscriberInfo> PRODUCT_SUBSCRIBER_INDEX = new HashMap<>();
    private static final Map<Class<?>, SubscriberInfo> ACTIVITY_SUBSCRIBER_INDEX = new HashMap<>();

    static {

        putProductIndex(new SimpleSubscriberInfo(AnnotationTest3.class, new SubscriberMethodInfo[] {
                new SubscriberMethodInfo("productConnected", OnProductConnected.class)
        }));

        putActivityIndex(new SimpleSubscriberInfo(AnnotationTest3.class, new SubscriberMethodInfo[]{
                new SubscriberMethodInfo("activityCreated", OnActivityCreated.class)
        }));
    }

    @Override
    public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
        SubscriberInfo productInfo = PRODUCT_SUBSCRIBER_INDEX.get(subscriberClass);
        SubscriberInfo activityInfo = ACTIVITY_SUBSCRIBER_INDEX.get(subscriberClass);

        if (productInfo != null) {
            return productInfo;
        } else if (activityInfo != null) {
            return activityInfo;
        } else {
            return null;
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
