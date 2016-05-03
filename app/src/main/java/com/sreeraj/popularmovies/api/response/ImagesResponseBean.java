package com.sreeraj.popularmovies.api.response;

import com.sreeraj.popularmovies.models.Image;

import org.parceler.Parcel;

import java.util.List;

/**
 * Model object for images list of a movie.
 */
@Parcel
public class ImagesResponseBean {

    public int id;
    public List<Image> backdrops;

    public int getId() {
        return id;
    }

    public List<Image> getBackdrops() {
        return backdrops;
    }
}
