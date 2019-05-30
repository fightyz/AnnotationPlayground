package com.fightyz.testandroidmodule;

import com.fightyz.lifecycle.annotation.OnActivityCreated;
import com.fightyz.lifecycle.annotation.OnProductConnected;

public class AnnotationTest3 {

    @OnProductConnected
    public void productConnected() {
        throw new RuntimeException("Annotation 3 crash");
    }

    @OnActivityCreated
    public void activityCreated() {

    }
}
