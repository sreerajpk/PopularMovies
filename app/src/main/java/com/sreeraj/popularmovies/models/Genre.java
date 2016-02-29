package com.sreeraj.popularmovies.models;

import org.parceler.Parcel;

/**
 * Created by Sreeraj on 2/24/16.
 */
@Parcel
public class Genre {

    public int id;
    public String name;

    public Genre() {
        //Empty constructor
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
