package com.sreeraj.popularmovies.fragments;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sreeraj.popularmovies.PMSpacesItemDecoration;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.activities.PMMovieDetailsActivity;
import com.sreeraj.popularmovies.adapters.PMMoviesGridAdapter;
import com.sreeraj.popularmovies.api.PMMoviesApi;
import com.sreeraj.popularmovies.api.response.MovieListResponseBean;
import com.sreeraj.popularmovies.app.PMConstants;
import com.sreeraj.popularmovies.events.FailureEvent;
import com.sreeraj.popularmovies.events.MoviesSelectionEvent;
import com.sreeraj.popularmovies.events.PopularMoviesEvent;
import com.sreeraj.popularmovies.events.TopRatedMoviesEvent;
import com.sreeraj.popularmovies.models.MovieGeneral;
import com.sreeraj.popularmovies.utils.Utils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Fragment which handles movielist.
 */
public class PMMovieListFragment extends Fragment {

    private static final String THUMB_IMAGE_TRANSITION_NAME = "thumb_image";
    private static final String POSITION = "position";
    private static final String MOVIE_LIST = "movie_list";
    private static final String CURRENT_PAGE = "current_page";
    private static final String TOTAL_PAGES = "total_pages";
    private static final int MOVIE_ITEM_SIZE = 1;

    @Bind(R.id.movies_grid)
    RecyclerView moviesGrid;
    @Bind(R.id.empty_view)
    LinearLayout emptyView;
    @Bind(R.id.retry_button)
    Button retryButton;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private Context context;
    private PMMoviesGridAdapter adapter;
    private GridLayoutManager layoutManager;
    private List<MovieGeneral> movieList;
    private int position;
    private boolean restoredState;
    private boolean isLoading;
    private int currentPage = 0;
    private int totalPages;
    private RecyclerView.OnScrollListener
            mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,
                                         int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading && currentPage < totalPages) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PMConstants.PAGE_SIZE) {
                    if (Utils.isNetworkAvailable(getActivity())) {
                        loadMoreItems();
                    }
                }
            }
        }
    };

    public PMMovieListFragment() {
        //Required empty constructor
    }

    public static PMMovieListFragment newInstance(int position) {
        PMMovieListFragment fragment = new PMMovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            position = bundle.getInt(POSITION);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            List<MovieGeneral> list = Parcels.unwrap(savedInstanceState.getParcelable(MOVIE_LIST));
            if (list != null && list.size() > 0) {
                movieList = new ArrayList<>();
                movieList.addAll(list);
                restoredState = true;
            }
            currentPage = savedInstanceState.getInt(CURRENT_PAGE);
            totalPages = savedInstanceState.getInt(TOTAL_PAGES);
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
        if (!restoredState) {
            fetchMovieList();
        } else {
            if (movieList != null && movieList.size() > 0) {
                List<MovieGeneral> list = new ArrayList<>();
                list.addAll(movieList);
                adapter.setList(list);
            }
            restoredState = false;
        }

    }

    private void initViews() {
        layoutManager = new GridLayoutManager(context, getResources().getInteger(R.integer.number_of_columns));
        adapter = new PMMoviesGridAdapter(context, position);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case PMMoviesGridAdapter.VIEW_ITEM:
                        return MOVIE_ITEM_SIZE;
                    case PMMoviesGridAdapter.VIEW_PROG:
                        return getResources().getInteger(R.integer.size_of_progress_pagination);
                    default:
                        return -1;
                }
            }
        });
        moviesGrid.setLayoutManager(layoutManager);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkAdapterIsEmpty();
            }
        });
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        moviesGrid.addItemDecoration(new PMSpacesItemDecoration(spacingInPixels));
        moviesGrid.setHasFixedSize(true);
        moviesGrid.setAdapter(adapter);
        moviesGrid.addOnScrollListener(mRecyclerViewOnScrollListener);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchMovieList();
            }
        });
    }

    private void loadMoreItems() {
        isLoading = true;
        PMMoviesApi moviesApi = new PMMoviesApi();
        Map<String, String> options = new HashMap<>();
        options.put(PMConstants.API_KEY, getString(R.string.api_key));
        options.put(PMConstants.PAGE, String.valueOf(currentPage + 1));
        if (position == PMConstants.POPULAR) {
            moviesApi.getPopularMovies(options);
        } else {
            moviesApi.getTopRatedMovies(options);
        }
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
            emptyView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            PMMoviesApi moviesApi = new PMMoviesApi();
            Map<String, String> options = new HashMap<>();
            options.put(PMConstants.API_KEY, getString(R.string.api_key));
            if (position == PMConstants.POPULAR) {
                moviesApi.getPopularMovies(options);
            } else {
                moviesApi.getTopRatedMovies(options);
            }
        } else {
            showNetworkAlertSnackBar();
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(MoviesSelectionEvent event) {
        if (position == event.getMovieListSortType()) {
            Intent intent = new Intent(getActivity(), PMMovieDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(PMConstants.MOVIE_GENERAL, Parcels.wrap(event.getSelectedMovie()));
            intent.putExtra(PMConstants.BUNDLE, bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), event.getView(), THUMB_IMAGE_TRANSITION_NAME);
                getActivity().startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    public void onEvent(PopularMoviesEvent event) {
        if (position == PMConstants.POPULAR) {
            MovieListResponseBean bean = event.getResponseBean();
            setMovieList(bean);
            //if (apiCallsInProgress == 0) { //If all api call results are delivered as sticky
            EventBus.getDefault().removeStickyEvent(event);
            //}
        }
    }

    private void setMovieList(MovieListResponseBean bean) {
        List<MovieGeneral> list = bean.getResults();
        if (adapter.getItemCount() == 0) {
            movieList = list;
            adapter.setList(list);
        } else {
            movieList.addAll(list);
            adapter.addMoviesToList(list);
        }
        currentPage = bean.getPage();
        Toast.makeText(context, String.valueOf(currentPage), Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        totalPages = bean.getTotalPages();
        isLoading = false;
    }

    public void onEvent(TopRatedMoviesEvent event) {
        if (position == PMConstants.TOP_RATED) {
            MovieListResponseBean bean = event.getResponseBean();
            setMovieList(bean);
            //if (apiCallsInProgress == 0) { //If all api call results are delivered as sticky
            EventBus.getDefault().removeStickyEvent(event);
            //}
        }
    }

    public void onEvent(FailureEvent failureEvent) {
        progressBar.setVisibility(View.GONE);
        Utils.showToast(failureEvent.getFailureMessageId(), context);
        EventBus.getDefault().removeStickyEvent(failureEvent);
        isLoading = false;
        checkAdapterIsEmpty();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (movieList != null && movieList.size() > 0) {
            outState.putParcelable(MOVIE_LIST, Parcels.wrap(new ArrayList<>(movieList)));
            outState.putInt(TOTAL_PAGES, totalPages);
            outState.putInt(CURRENT_PAGE, currentPage);
        }
        super.onSaveInstanceState(outState);
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
