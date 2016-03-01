package com.sreeraj.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Model object for a video details of a movie.
 */
@Parcel
public class Video {

    public String id;
    @SerializedName("iso_639_1")
    public String iso;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;

    public String getId() {
        return id;
    }

    public String getIso() {
        return iso;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
