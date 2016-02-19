package com.sreeraj.popularmovies.events;

/**
 * Created by Sreeraj on 1/27/16.
 */
public class FailureEvent {

    private int failureMessageId;

    public FailureEvent(int failureMessageId) {
        this.failureMessageId = failureMessageId;
    }

    public int getFailureMessageId() {
        return failureMessageId;
    }
}
