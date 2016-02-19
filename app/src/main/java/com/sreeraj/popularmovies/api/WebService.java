package com.sreeraj.popularmovies.api;


import com.sreeraj.popularmovies.api.response.MovieListResponseBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Web service class with all api calls.
 */
public interface WebService {

    @GET("popular")
    Call<MovieListResponseBean> getPopularMovies(@QueryMap Map<String, String> options);//@Query("api_key") String apiKey, @Query("page") int pageToBeFetched);

    @GET("top_rated")
    Call<MovieListResponseBean> getTopRatedMovies(@QueryMap Map<String, String> options);//@Query("api_key") String apiKey, @Query("page") int pageToBeFetched);

}