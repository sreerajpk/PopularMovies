package com.sreeraj.popularmovies.api;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.api.response.MovieListResponseBean;
import com.sreeraj.popularmovies.events.FailureEvent;
import com.sreeraj.popularmovies.events.PopularMoviesEvent;
import com.sreeraj.popularmovies.events.TopRatedMoviesEvent;
import com.sreeraj.popularmovies.models.Movie;

import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PMMoviesApi which handles api calls to get list of movies.
 */
public class PMMoviesApi extends PMBaseApi {

    public void getPopularMovies(Map<String, String> options) {
        Call<MovieListResponseBean> call = service.getPopularMovies(options);
        call.enqueue(new Callback<MovieListResponseBean>() {
            @Override
            public void onResponse(Call<MovieListResponseBean> call, Response<MovieListResponseBean> response) {
                EventBus.getDefault().postSticky(new PopularMoviesEvent(response.body()));
            }

            @Override
            public void onFailure(Call<MovieListResponseBean> call, Throwable t) {
                EventBus.getDefault().postSticky(new FailureEvent(R.string.some_unknown_error_has_occurred));
            }
        });
    }

    public void getTopRatedMovies(Map<String, String> options) {
        Call<MovieListResponseBean> call = service.getTopRatedMovies(options);
        call.enqueue(new Callback<MovieListResponseBean>() {
            @Override
            public void onResponse(Call<MovieListResponseBean> call, Response<MovieListResponseBean> response) {
                EventBus.getDefault().postSticky(new TopRatedMoviesEvent(response.body()));
            }

            @Override
            public void onFailure(Call<MovieListResponseBean> call, Throwable t) {
                EventBus.getDefault().postSticky(new FailureEvent(R.string.some_unknown_error_has_occurred));
            }
        });
    }

    public void getMovieDetails(long id, String apiKey) {
        Call<Movie> call = service.getMovieDetails(id, apiKey);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                EventBus.getDefault().postSticky(response.body());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                EventBus.getDefault().postSticky(new FailureEvent(R.string.some_unknown_error_has_occurred));
            }
        });
    }
}
