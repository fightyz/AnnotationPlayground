package com.dji.lifecycle;

import com.dji.lifecycle.meta.SubscriberInfoIndex;

import java.util.List;

public class SubscriberMethodFinder {
    private List<SubscriberInfoIndex> mSubscriberInfoIndexes;

    public SubscriberMethodFinder(List<SubscriberInfoIndex> subscriberInfoIndexes) {
        this.mSubscriberInfoIndexes = subscriberInfoIndexes;
    }
}
