package com.fightyz.lifecycle.meta;

public class SubscriberMethodInfo<T> {
    final String mMethodName;
    final Class<T> mAnnotationType;

    public SubscriberMethodInfo(String methodName, Class<T> annotationType) {
        this.mMethodName = methodName;
        mAnnotationType = annotationType;
    }
}
