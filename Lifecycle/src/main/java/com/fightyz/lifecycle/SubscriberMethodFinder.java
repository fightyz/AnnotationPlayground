package com.fightyz.lifecycle;

import com.fightyz.lifecycle.meta.SubscriberInfoIndex;

import java.util.List;

public class SubscriberMethodFinder {
    private List<SubscriberInfoIndex> mSubscriberInfoIndexes;

    public SubscriberMethodFinder(List<SubscriberInfoIndex> subscriberInfoIndexes) {
        this.mSubscriberInfoIndexes = subscriberInfoIndexes;
    }
}
