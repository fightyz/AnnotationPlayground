package com.dji.lifecycle;

public class LifecycleException extends RuntimeException {
    public LifecycleException(String s) {
        super(s);
    }

    public LifecycleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public LifecycleException(Throwable throwable) {
        super(throwable);
    }
}
