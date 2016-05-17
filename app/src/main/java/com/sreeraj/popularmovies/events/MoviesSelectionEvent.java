package com.sreeraj.popularmovies.events;

import android.view.View;

import com.sreeraj.popularmovies.models.MovieBasicDetails;

/**
 * Events which handles movie selection from grid.
 */
public class MoviesSelectionEvent {

    private MovieBasicDetails selectedMovie;
    private View view;
    private int movieListSortType;

    public MoviesSelectionEvent(MovieBasicDetails selectedMovie, View view, int movieListSortType) {
        this.selectedMovie = selectedMovie;
        this.view = view;
        this.movieListSortType = movieListSortType;
    }

    public MovieBasicDetails getSelectedMovie() {
        return selectedMovie;
    }

    public View getView() {
        return view;
    }

    public int getMovieListSortType() {
        return movieListSortType;
    }
}
