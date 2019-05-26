package com.dji.lifecycle.meta;

public class SubscriberMethodInfo {
    final String mMethodName;
    final Class mAnnotationType;

    public SubscriberMethodInfo(String methodName, Class annotationType) {
        this.mMethodName = methodName;
        mAnnotationType = annotationType;
    }
}
