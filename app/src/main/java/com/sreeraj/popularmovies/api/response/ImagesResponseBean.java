package com.sreeraj.popularmovies.api.response;

import com.sreeraj.popularmovies.models.Image;

import java.util.List;

/**
 * Created by Sreeraj on 3/2/16.
 */
public class ImagesResponseBean {

    private int id;
    private List<Image> backdrops;

    public int getId() {
        return id;
    }

    public List<Image> getBackdrops() {
        return backdrops;
    }
}
