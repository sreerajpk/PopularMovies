package com.sreeraj.popularmovies.events;

import com.sreeraj.popularmovies.api.response.MovieListResponseBean;

/**
 * Event which handles popular movies api call with response as MovieListResponseBean.
 */
public class PopularMoviesEvent {

    private MovieListResponseBean responseBean;

    public PopularMoviesEvent(MovieListResponseBean responseBean) {
        this.responseBean = responseBean;
    }

    public MovieListResponseBean getResponseBean() {
        return responseBean;
    }
}

