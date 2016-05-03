package com.sreeraj.popularmovies.api;

import com.sreeraj.popularmovies.app.PMConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Base Api.
 */
public class PMBaseApi {

    protected PMWebService service;

    public PMBaseApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PMConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PMWebService.class);
    }

}
