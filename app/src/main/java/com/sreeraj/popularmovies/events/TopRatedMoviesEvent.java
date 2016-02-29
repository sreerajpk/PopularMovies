package com.sreeraj.popularmovies.events;

import com.sreeraj.popularmovies.api.response.MovieListResponseBean;

/**
 * Event which handles top rated movies api call with response as MovieListResponseBean.
 */
public class TopRatedMoviesEvent {

    private MovieListResponseBean responseBean;


    public TopRatedMoviesEvent(MovieListResponseBean responseBean) {
        this.responseBean = responseBean;
    }

    public MovieListResponseBean getResponseBean() {
        return responseBean;
    }
}

