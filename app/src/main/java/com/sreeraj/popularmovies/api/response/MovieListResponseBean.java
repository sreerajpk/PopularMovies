package com.sreeraj.popularmovies.api.response;

import com.google.gson.annotations.SerializedName;
import com.sreeraj.popularmovies.models.MovieBasicDetails;

import java.util.List;

/**
 * Created by Sreeraj on 2/17/16.
 */
public class MovieListResponseBean {
    private int page;
    private List<MovieBasicDetails> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public List<MovieBasicDetails> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
