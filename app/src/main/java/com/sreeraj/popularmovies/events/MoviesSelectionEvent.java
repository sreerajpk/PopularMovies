package com.sreeraj.popularmovies.events;

import android.view.View;

import com.sreeraj.popularmovies.models.Movie;

/**
 * Created by Sreeraj on 2/19/16.
 */
public class MoviesSelectionEvent {

    private Movie selectedMovie;
    private View view;

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public View getView() {
        return view;
    }

    public MoviesSelectionEvent(Movie selectedMovie, View view) {
        this.selectedMovie = selectedMovie;
        this.view = view;
    }
}
