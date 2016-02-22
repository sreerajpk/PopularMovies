package com.sreeraj.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sreeraj on 2/17/16.
 */
public class Movie implements Parcelable {
    @SerializedName("poster_path")
    private String posterPath;
    private boolean adult;
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    private int id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    private double popularity;
    @SerializedName("vote_count")
    private int voteCount;
    private boolean video;
    @SerializedName("vote_average")
    private String voteAverage;

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public String getVoteAverage() {
        return voteAverage;
    }


    public Movie() {

    }

    public Movie(Parcel source) {
        posterPath = source.readString();
        adult = (source.readInt() != 0);
        overview = source.readString();
        releaseDate = source.readString();
        genreIds = new ArrayList<>();
        source.readList(genreIds, Integer.class.getClassLoader());
        id = source.readInt();
        originalTitle = source.readString();
        originalLanguage = source.readString();
        title = source.readString();
        backdropPath = source.readString();
        voteCount = source.readInt();
        popularity = source.readDouble();
        video = (source.readInt() != 0);
        voteAverage = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeInt(adult ? 1 : 0);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeList(genreIds);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeInt(voteCount);
        dest.writeDouble(popularity);
        dest.writeInt(video ? 1 : 0);
        dest.writeString(voteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
