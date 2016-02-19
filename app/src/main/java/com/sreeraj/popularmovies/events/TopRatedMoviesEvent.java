package com.sreeraj.popularmovies.events;

import com.sreeraj.popularmovies.api.response.MovieListResponseBean;

/**
 * Created by Sreeraj on 2/18/16.
 */
public class TopRatedMoviesEvent {

    private MovieListResponseBean responseBean;

    public MovieListResponseBean getResponseBean() {
        return responseBean;
    }

    public TopRatedMoviesEvent(MovieListResponseBean responseBean) {
        this.responseBean = responseBean;
    }

}
