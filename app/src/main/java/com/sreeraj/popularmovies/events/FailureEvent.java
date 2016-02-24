package com.sreeraj.popularmovies.events;

/**
 * Event which handles failure of api calls.
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
