package com.sreeraj.popularmovies.api;

import com.sreeraj.popularmovies.app.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Base Api.
 */
public class BaseApi {

    protected WebService service;

    public BaseApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WebService.class);
    }

}
