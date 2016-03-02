package com.sreeraj.popularmovies.api;


import com.sreeraj.popularmovies.api.response.ImagesResponseBean;
import com.sreeraj.popularmovies.api.response.MovieListResponseBean;
import com.sreeraj.popularmovies.api.response.VideoResponseBean;
import com.sreeraj.popularmovies.models.Movie;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Web service class with all api calls.
 */
public interface WebService {

    @GET("popular")
    Call<MovieListResponseBean> getPopularMovies(@QueryMap Map<String, String> options);

    @GET("top_rated")
    Call<MovieListResponseBean> getTopRatedMovies(@QueryMap Map<String, String> options);

    @GET("{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<VideoResponseBean> getVideoDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("{id}/images")
    Call<ImagesResponseBean> getImages(@Path("id") int id, @Query("api_key") String apiKey);
}
