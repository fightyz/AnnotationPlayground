package com.fightyz.lifecycle.meta;

import com.fightyz.lifecycle.SubscriberMethod;

public interface SubscriberInfo {
    Class<?> getSubscriberClass();

    SubscriberMethod[] getSubscriberMethods();
}
