package com.sreeraj.popularmovies.api;

import com.sreeraj.popularmovies.app.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by neeraj on 20/1/16.
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
