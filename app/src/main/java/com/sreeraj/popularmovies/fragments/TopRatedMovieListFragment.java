package com.sreeraj.popularmovies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.SpacesItemDecoration;
import com.sreeraj.popularmovies.adapters.MoviesGridAdapter;
import com.sreeraj.popularmovies.api.MoviesApi;
import com.sreeraj.popularmovies.app.Constants;
import com.sreeraj.popularmovies.events.FailureEvent;
import com.sreeraj.popularmovies.events.TopRatedMoviesEvent;
import com.sreeraj.popularmovies.models.Movie;
import com.sreeraj.popularmovies.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Fragment which handles top rated movielist.
 */
public class TopRatedMovieListFragment extends Fragment {

    private Context context;
    private RecyclerView moviesGrid;
    private MoviesGridAdapter adapter;
    private GridLayoutManager layoutManager;
    private View emptyView;
    private List<Movie> movieList;

    public static TopRatedMovieListFragment newInstance() {
        return new TopRatedMovieListFragment();
    }


    public TopRatedMovieListFragment() {
        //Required empty constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        fetchMovieList();
    }

    private void initViews(View view) {
        moviesGrid = (RecyclerView) view.findViewById(R.id.movies_grid);
        layoutManager = new GridLayoutManager(context, 2);
        moviesGrid.setLayoutManager(layoutManager);
        adapter = new MoviesGridAdapter(context);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkAdapterIsEmpty();
            }
        });
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        moviesGrid.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        moviesGrid.setHasFixedSize(true);
        moviesGrid.setAdapter(adapter);
        emptyView = view.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void checkAdapterIsEmpty() {
        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    private void fetchMovieList() {
        if (Utils.isNetworkAvailable(context)) {
            MoviesApi moviesApi = new MoviesApi();
            Map<String, String> options = new HashMap<>();
            options.put(Constants.API_KEY, getString(R.string.api_key));
            moviesApi.getTopRatedMovies(options);
        } else {
            showNetworkAlertSnackBar();
        }
    }

    public void onEvent(Integer gridViewSelection) {
        // Show details page
    }

    public void onEvent(TopRatedMoviesEvent event) {
        movieList = event.getResponseBean().getResults();
        adapter.setList(movieList);
    }

    public void onEvent(FailureEvent failureEvent) {
        Utils.showToast(failureEvent.getFailureMessageId(), context);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void showNetworkAlertSnackBar() {
        CoordinatorLayout snackBarHolder = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        Snackbar snackbar = Snackbar.make(snackBarHolder, R.string.no_network_connection, Snackbar.LENGTH_LONG);
        snackbar.
                setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchMovieList();
                    }
                })
                .setActionTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
        ViewGroup.LayoutParams layoutParams = snackbar.getView().getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        snackbar.getView().setLayoutParams(layoutParams);
        snackbar.show();
    }
}

