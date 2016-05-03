package com.sreeraj.popularmovies.api.response;

import com.sreeraj.popularmovies.models.Video;

import org.parceler.Parcel;

import java.util.List;

/**
 * Model object for videos list of a movie
 */
@Parcel
public class VideoResponseBean {

    public int id;
    public List<Video> results;

    public int getId() {
        return id;
    }

    public List<Video> getResults() {
        return results;
    }
}
