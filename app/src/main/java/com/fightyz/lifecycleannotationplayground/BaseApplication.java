package com.fightyz.lifecycleannotationplayground;

import android.app.Application;

import com.fightyz.lifecycle.Lifecycle2;
import com.fightyz.lifecycle.observer.MyLifecycleSubscriberInfoIndex;
import com.fightyz.testandroidmodule.TestAndroidLifecycleSubscriberInfoIndex;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Lifecycle.build(this);
        Lifecycle2.Builder builder = new Lifecycle2.Builder();
        builder.addIndex(new MyLifecycleSubscriberInfoIndex())
                .addIndex(new TestAndroidLifecycleSubscriberInfoIndex());
        builder.build(this);
    }
}
