package com.dji.lifecycle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class SubscriberMethod {
    public final Method mMethod;
    public final Annotation mAnnotation;

    String mMethodString;

    public SubscriberMethod(Method method, Annotation annotation) {
        this.mMethod = method;
        mAnnotation = annotation;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SubscriberMethod) {
            checkMethodString();
            SubscriberMethod otherSubscriberMethod = (SubscriberMethod)other;
            otherSubscriberMethod.checkMethodString();
            // Don't use method.equals because of http://code.google.com/p/android/issues/detail?id=7811#c6
            return mMethodString.equals(otherSubscriberMethod.mMethodString);
        } else {
            return false;
        }
    }

    private synchronized void checkMethodString() {
        if (mMethodString == null) {
            // Method.toString has more overhead, just take relevant parts of the method
            StringBuilder builder = new StringBuilder(64);
            builder.append(mMethod.getDeclaringClass().getName());
            builder.append('#').append(mMethod.getName());
            mMethodString = builder.toString();
        }
    }

    @Override
    public int hashCode() {
        return mMethod.hashCode();
    }
}
