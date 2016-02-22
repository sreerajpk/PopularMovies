package com.sreeraj.popularmovies.events;

import android.view.View;

import com.sreeraj.popularmovies.models.Movie;

/**
 * Created by Sreeraj on 2/19/16.
 */
public class MoviesSelectionEvent {

    private Movie selectedMovie;
    private View view;
    private int movieListSortType;

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public View getView() {
        return view;
    }

    public int getMovieListSortType() {
        return movieListSortType;
    }

    public MoviesSelectionEvent(Movie selectedMovie, View view, int movieListSortType) {
        this.selectedMovie = selectedMovie;
        this.view = view;
        this.movieListSortType = movieListSortType;
    }
}
