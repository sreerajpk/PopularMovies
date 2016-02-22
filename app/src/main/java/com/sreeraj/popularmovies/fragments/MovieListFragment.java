package com.sreeraj.popularmovies.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.SpacesItemDecoration;
import com.sreeraj.popularmovies.activities.MovieDetailsActivity;
import com.sreeraj.popularmovies.adapters.MoviesGridAdapter;
import com.sreeraj.popularmovies.api.MoviesApi;
import com.sreeraj.popularmovies.app.Constants;
import com.sreeraj.popularmovies.events.FailureEvent;
import com.sreeraj.popularmovies.events.MoviesEvent;
import com.sreeraj.popularmovies.events.MoviesSelectionEvent;
import com.sreeraj.popularmovies.models.Movie;
import com.sreeraj.popularmovies.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Fragment which handles popular movielist.
 */
public class MovieListFragment extends Fragment {

    private static final String THUMB_IMAGE_TRANSITION_NAME = "thumb_image";

    private Context context;
    private RecyclerView moviesGrid;
    private MoviesGridAdapter adapter;
    private GridLayoutManager layoutManager;
    private View emptyView;
    private List<Movie> movieList;
    private Dialog progressDialog;
    private int position;

    public static MovieListFragment newInstance(int position) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }


    public MovieListFragment() {
        //Required empty constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            position = bundle.getInt("position");
        }
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
        adapter = new MoviesGridAdapter(context, position);
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
            progressDialog = Utils.showProgressDialog(context);
            MoviesApi moviesApi = new MoviesApi();
            Map<String, String> options = new HashMap<>();
            options.put(Constants.API_KEY, getString(R.string.api_key));
            if (position == Constants.POPULAR) {
                moviesApi.getPopularMovies(options);
            } else {
                moviesApi.getTopRatedMovies(options);
            }
        } else {
            showNetworkAlertSnackBar();
        }
    }

    public void onEvent(MoviesSelectionEvent event) {
        if (position == event.getMovieListSortType()) {
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.MOVIE, event.getSelectedMovie());
            intent.putExtra(Constants.BUNDLE, bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), event.getView(), THUMB_IMAGE_TRANSITION_NAME);
                getActivity().startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    public void onEvent(MoviesEvent event) {
        if (position == event.getSortType()) {
            movieList = event.getResponseBean().getResults();
            adapter.setList(movieList);
            hideProgressDialog();
        }
    }

    public void onEvent(FailureEvent failureEvent) {
        hideProgressDialog();
        Utils.showToast(failureEvent.getFailureMessageId(), context);
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
