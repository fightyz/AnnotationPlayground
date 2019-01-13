package com.dji.lifecycle.meta;

import com.dji.lifecycle.SubscriberMethod;

public interface SubscriberInfo {
    Class<?> getSubscriberClass();

    SubscriberMethod[] getSubscriberMethods();
}
