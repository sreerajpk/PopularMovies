package com.sreeraj.popularmovies.events;

import android.view.View;

import com.sreeraj.popularmovies.models.MovieGeneral;

/**
 * Events which handles movie selection from grid.
 */
public class MoviesSelectionEvent {

    private MovieGeneral selectedMovie;
    private View view;
    private int movieListSortType;

    public MoviesSelectionEvent(MovieGeneral selectedMovie, View view, int movieListSortType) {
        this.selectedMovie = selectedMovie;
        this.view = view;
        this.movieListSortType = movieListSortType;
    }

    public MovieGeneral getSelectedMovie() {
        return selectedMovie;
    }

    public View getView() {
        return view;
    }

    public int getMovieListSortType() {
        return movieListSortType;
    }
}
