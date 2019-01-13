package com.dji.lifecycleannotationplayground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dji.lifecycle.interfaces.sample.FpvLifecycleTest;

import dji.lifecycle.observer.drone.ObserverHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObserverHolder holder = new ObserverHolder(FpvLifecycleTest.class);
    }
}
