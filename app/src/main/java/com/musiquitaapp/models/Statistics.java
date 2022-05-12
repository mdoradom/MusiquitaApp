package com.musiquitaapp.models;

public class Statistics {
    long viewCount;
    long commentCount;
    long subscriberCount;
    boolean hiddenSubscriberCount;
    long videoCount;

    public Statistics(long viewCount, long commentCount, long subscriberCount, boolean hiddenSubscriberCount, long videoCount) {
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.subscriberCount = subscriberCount;
        this.hiddenSubscriberCount = hiddenSubscriberCount;
        this.videoCount = videoCount;
    }
}
