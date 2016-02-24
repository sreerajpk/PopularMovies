package com.sreeraj.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.SpacesItemDecoration;
import com.sreeraj.popularmovies.activities.MovieDetailsActivity;
import com.sreeraj.popularmovies.adapters.MoviesGridAdapter;
import com.sreeraj.popularmovies.api.MoviesApi;
import com.sreeraj.popularmovies.app.Constants;
import com.sreeraj.popularmovies.events.FailureEvent;
import com.sreeraj.popularmovies.events.MoviesEvent;
import com.sreeraj.popularmovies.events.MoviesSelectionEvent;
import com.sreeraj.popularmovies.models.MovieInList;
import com.sreeraj.popularmovies.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Fragment which handles movielist.
 */
public class MovieListFragment extends BaseFragment {

    private static final String THUMB_IMAGE_TRANSITION_NAME = "thumb_image";

    private Context context;
    private MoviesGridAdapter adapter;
    private GridLayoutManager layoutManager;
    private List<MovieInList> movieList;
    private int position;
    private boolean refresh;

    @Bind(R.id.movies_grid)
    RecyclerView moviesGrid;
    @Bind(R.id.empty_view)
    LinearLayout emptyView;
    @Bind(R.id.retry_button)
    Button retryButton;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

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
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(getActivity());
        initViews();
        fetchMovieList();
    }

    private void initViews() {
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
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchMovieList();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                fetchMovieList();
            }
        });
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
            if (!refresh) {
                showProgressDialog(context);
            }
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
            emptyView.setVisibility(View.VISIBLE);
            stopRefreshing();
        }
    }

    private void stopRefreshing() {
        if (refresh) {
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
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
            stopRefreshing();
            if (apiCallsInProgress == 0) { //If all api call results are delivered as sticky
                EventBus.getDefault().removeStickyEvent(event);
            }
        }
    }

    public void onEvent(FailureEvent failureEvent) {
        hideProgressDialog();
        stopRefreshing();
        Utils.showToast(failureEvent.getFailureMessageId(), context);
        if (apiCallsInProgress == 0) { //If all api call results are delivered as sticky
            EventBus.getDefault().removeStickyEvent(failureEvent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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
