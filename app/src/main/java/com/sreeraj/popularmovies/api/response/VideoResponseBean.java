package com.sreeraj.popularmovies.api.response;

import com.sreeraj.popularmovies.models.Video;

import java.util.List;

/**
 * Created by Sreeraj on 3/1/16.
 */
public class VideoResponseBean {

    private int id;
    private List<Video> results;

    public int getId() {
        return id;
    }

    public List<Video> getResults() {
        return results;
    }
}
