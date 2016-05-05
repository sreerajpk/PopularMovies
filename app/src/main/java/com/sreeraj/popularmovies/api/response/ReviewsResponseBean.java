package com.sreeraj.popularmovies.api.response;

import com.google.gson.annotations.SerializedName;
import com.sreeraj.popularmovies.models.Review;

import org.parceler.Parcel;

import java.util.List;

/**
 * Model object for reviews list of a movie.
 */
@Parcel
public class ReviewsResponseBean {

    public int id;
    public List<Review> results;
    @SerializedName("total_results")
    public int totalResults;
    @SerializedName("total_pages")
    public int totalPages;
    public int page;

    public int getId() {
        return id;
    }

    public List<Review> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPage() {
        return page;
    }
}
