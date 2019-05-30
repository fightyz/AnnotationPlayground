package com.fightyz.lifecycle.meta;

import java.util.Set;

public interface SubscriberInfoIndex {
    SubscriberInfo getSubscriberInfo(Class<?> subscriberClass);

    Set<Class<?>> getSubscriberClasses();

    Set<Class<?>> getActivitySubscriberClasses();

    Set<Class<?>> getProductSubscriberClasses();
}
