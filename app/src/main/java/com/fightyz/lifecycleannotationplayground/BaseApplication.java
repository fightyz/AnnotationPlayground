package com.fightyz.lifecycleannotationplayground;

import android.app.Application;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Lifecycle.build(this);
    }
}
