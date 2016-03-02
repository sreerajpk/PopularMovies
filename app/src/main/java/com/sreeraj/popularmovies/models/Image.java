package com.sreeraj.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Sreeraj on 3/2/16.
 */
@Parcel
public class Image {

    @SerializedName("aspect_ratio")
    public double aspectRatio;
    @SerializedName("file_path")
    public String filePath;
    public int height;
    @SerializedName("iso_639_1")
    public String iso;
    @SerializedName("vote_average")
    public double voteAverage;
    @SerializedName("vote_count")
    public int voteCount;
    public int width;

    public double getAspectRatio() {
        return aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getHeight() {
        return height;
    }

    public String getIso() {
        return iso;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public int getWidth() {
        return width;
    }
}
