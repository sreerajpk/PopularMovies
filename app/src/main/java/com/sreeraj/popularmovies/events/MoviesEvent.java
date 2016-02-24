package com.sreeraj.popularmovies.events;

import com.sreeraj.popularmovies.api.response.MovieListResponseBean;

/**
 * Event which handles api calls with response as MovieListResponseBean.
 */
public class MoviesEvent {

    private int sortType;
    private MovieListResponseBean responseBean;

    public int getSortType() {
        return sortType;
    }

    public MovieListResponseBean getResponseBean() {
        return responseBean;
    }

    public MoviesEvent(int sortType, MovieListResponseBean responseBean) {
        this.sortType = sortType;
        this.responseBean = responseBean;
    }
}

