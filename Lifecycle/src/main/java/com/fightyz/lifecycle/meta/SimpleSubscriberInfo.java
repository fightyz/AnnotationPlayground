package com.fightyz.lifecycle.meta;

import com.fightyz.lifecycle.LifecycleException;
import com.fightyz.lifecycle.SubscriberMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class SimpleSubscriberInfo implements SubscriberInfo {

    private final Class mSubscriberClass;
    private final SubscriberMethodInfo[] mMethodInfos;

    public SimpleSubscriberInfo(Class subscriberClass, SubscriberMethodInfo[] methodInfos) {
        mSubscriberClass = subscriberClass;
        mMethodInfos = methodInfos;
    }

    @Override
    public Class<?> getSubscriberClass() {
        return mSubscriberClass;
    }

    @Override
    public SubscriberMethod[] getSubscriberMethods() {
        int length = mMethodInfos.length;
        SubscriberMethod[] methods = new SubscriberMethod[length];
        for (int i = 0; i < length; i++) {
            SubscriberMethodInfo info = mMethodInfos[i];
            methods[i] = createSubsciberMethod(info.mMethodName, info.mAnnotationType);
        }
        return methods;
    }

    private SubscriberMethod createSubsciberMethod(String methodName, Class annotationType) {
        try {
            Method method = mSubscriberClass.getDeclaredMethod(methodName);
            Annotation annotation = method.getAnnotation(annotationType);
            return new SubscriberMethod(method, annotation);
        } catch (NoSuchMethodException e) {
            throw new LifecycleException("Could not find subscriber method in " + mSubscriberClass +
            ". Maybe a missing ProGuard rule?", e);
        }
    }

    @Override
    public String toString() {
        return "SimpleSubscriberInfo{" +
                "mSubscriberClass=" + mSubscriberClass +
                ", mMethodInfos=" + Arrays.toString(mMethodInfos) +
                '}';
    }
}
