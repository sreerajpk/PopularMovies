package com.sreeraj.popularmovies.events;

import android.view.View;

import com.sreeraj.popularmovies.models.MovieInList;

/**
 * Events which handles movie selection from grid.
 */
public class MoviesSelectionEvent {

    private MovieInList selectedMovie;
    private View view;
    private int movieListSortType;

    public MovieInList getSelectedMovie() {
        return selectedMovie;
    }

    public View getView() {
        return view;
    }

    public int getMovieListSortType() {
        return movieListSortType;
    }

    public MoviesSelectionEvent(MovieInList selectedMovie, View view, int movieListSortType) {
        this.selectedMovie = selectedMovie;
        this.view = view;
        this.movieListSortType = movieListSortType;
    }
}
