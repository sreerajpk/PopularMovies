package com.sreeraj.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.app.Constants;
import com.sreeraj.popularmovies.events.MoviesSelectionEvent;
import com.sreeraj.popularmovies.models.MovieGeneral;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Adapter that populates movie list.
 */
public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {

    private List<MovieGeneral> movieList = new ArrayList<>();
    private Context context;
    private int movieListSortType;

    public MoviesGridAdapter(Context context, int movieListSortType) {
        this.context = context;
        this.movieListSortType = movieListSortType;
    }

    public void setList(List<MovieGeneral> movieList) {
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    public void addMoviesToList(List<MovieGeneral> movieList) {
        int size = getItemCount();
        this.movieList.addAll(movieList);
        notifyItemRangeInserted(size, size + movieList.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(Constants.IMAGE_BASE_URL
                + movieList.get(position).getPosterPath()).placeholder(R.color.lighter_gray).into(holder.image);
        holder.name.setText(movieList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.movie_image);
            name = (TextView) itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MoviesSelectionEvent event = new MoviesSelectionEvent(movieList.get(getLayoutPosition()),
                    v.findViewById(R.id.movie_image), movieListSortType);
            EventBus.getDefault().post(event);
        }
    }
}