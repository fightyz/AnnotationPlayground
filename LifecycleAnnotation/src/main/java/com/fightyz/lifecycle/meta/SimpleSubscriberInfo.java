package com.fightyz.lifecycle.meta;

import com.fightyz.lifecycle.LifecycleException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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

    public static SubscriberInfo combine(SubscriberInfo... infos) {
        if (infos == null) {
            throw new RuntimeException("cannot combine with null");
        }
        if (infos.length > 1) {
            Class<?> subscriberClass = null;
            List<SubscriberMethodInfo> subscriberMethodInfos = new LinkedList<>();
            for (SubscriberInfo info : infos) {
                subscriberClass = info.getSubscriberClass();
                for (SubscriberMethod subscriberMethod : info.getSubscriberMethods()) {
                    subscriberMethodInfos.add(new SubscriberMethodInfo(subscriberMethod.mMethodString, subscriberMethod.mAnnotation.getClass()));
                }
            }
            SubscriberMethodInfo[] subscriberMethodInfoArray = new SubscriberMethodInfo[subscriberMethodInfos.size()];
            return new SimpleSubscriberInfo(subscriberClass, subscriberMethodInfos.toArray(subscriberMethodInfoArray));
        } else {
            throw new RuntimeException("infos with wrong size " + infos.length);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleSubscriberInfo that = (SimpleSubscriberInfo) o;
        return Objects.equals(mSubscriberClass, that.mSubscriberClass) &&
                Arrays.equals(mMethodInfos, that.mMethodInfos);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mSubscriberClass);
        result = 31 * result + Arrays.hashCode(mMethodInfos);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleSubscriberInfo{" +
                "mSubscriberClass=" + mSubscriberClass +
                ", mMethodInfos=" + Arrays.toString(mMethodInfos) +
                '}';
    }
}
