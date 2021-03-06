package com.sreeraj.popularmovies.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.activities.PMMainActivity;
import com.sreeraj.popularmovies.app.PMConstants;
import com.sreeraj.popularmovies.app.PopularMoviesApplication;
import com.sreeraj.popularmovies.events.MoviesSelectionEvent;
import com.sreeraj.popularmovies.fragments.PMMovieDetailsFragment;
import com.sreeraj.popularmovies.models.MovieBasicDetails;
import com.sreeraj.popularmovies.views.PMImageView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Adapter that populates movie list.
 */
public class PMMoviesGridAdapter extends RecyclerView.Adapter {

    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROG = 0;

    private List<MovieBasicDetails> movieList = new ArrayList<>();
    private Context context;
    private int movieListSortType;

    public PMMoviesGridAdapter(Context context, int movieListSortType) {
        this.context = context;
        this.movieListSortType = movieListSortType;
    }

    public void setList(List<MovieBasicDetails> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    public void addMoviesToList(List<MovieBasicDetails> movieList) {
        int size = getItemCount();
        this.movieList.addAll(movieList);
        notifyItemRangeInserted(size, size + movieList.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.movie_grid_cell, parent, false);
            holder = new MovieViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.dialog_borderless, parent, false);
            holder = new ProgressViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MovieViewHolder) {
            Glide.with(context)
                    .load(PMConstants.IMAGE_BASE_URL + movieList.get(position).getPosterPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.lighter_gray)
                    .error(android.R.drawable.alert_dark_frame)
                    .into(((MovieViewHolder) holder).image);
            ((MovieViewHolder) holder).name.setText(movieList.get(position).getTitle());
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (movieList.size() == 0) {
            return 0;
        }
        if (movieListSortType == PMConstants.FAVOURITES) {
            return movieList.size();
        } else {
            return movieList.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (movieList.size() > 0) {
            if (position < movieList.size()) {
                return VIEW_ITEM;
            } else {
                return VIEW_PROG;
            }
        }
        return 1;
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PMImageView image;
        private TextView name;

        public MovieViewHolder(View itemView) {
            super(itemView);
            image = (PMImageView) itemView.findViewById(R.id.movie_image);
            name = (TextView) itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (PopularMoviesApplication.isTwoPane()) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(PMConstants.MOVIE_GENERAL, Parcels.wrap(movieList.get(getLayoutPosition())));
                PMMovieDetailsFragment fragment = new PMMovieDetailsFragment();
                fragment.setArguments(arguments);
                ((PMMainActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment)
                        .commit();
            } else {
                MoviesSelectionEvent event = new MoviesSelectionEvent(movieList.get(getLayoutPosition()),
                        v.findViewById(R.id.movie_image), movieListSortType);
                EventBus.getDefault().post(event);
            }
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}