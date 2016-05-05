package com.sreeraj.popularmovies.models;

import org.parceler.Parcel;

/**
 * Model object for review details of a movie.
 */
@Parcel
public class Review {

    public String id;
    public String author;
    public String content;
    public String url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
