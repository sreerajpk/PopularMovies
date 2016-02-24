package com.sreeraj.popularmovies.activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.api.MoviesApi;
import com.sreeraj.popularmovies.app.Constants;
import com.sreeraj.popularmovies.events.FailureEvent;
import com.sreeraj.popularmovies.fragments.MoviePosterDialogFragment;
import com.sreeraj.popularmovies.models.Genre;
import com.sreeraj.popularmovies.models.Movie;
import com.sreeraj.popularmovies.models.MovieInList;
import com.sreeraj.popularmovies.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * The activity which shows the details of a particular movie.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    private static final double COLOR_DARKENING_FRACTION = 0.85;
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private static final String RATING_OUT_OF = "/10";
    private static final String MOVIE_POSTER = "movie_poster";
    private static final int SHORT_ANIMATION_DURATION = 200;
    private static final String DOUBLE_QUOTES = "\"";

    private MovieInList movie;
    private Dialog progressDialog;

    @Bind(R.id.poster_image)
    ImageView posterImage;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.thumb_image)
    ImageView thumbImage;
    @Bind(R.id.synopsis)
    TextView synopsis;
    @Bind(R.id.user_rating)
    TextView userRating;
    @Bind(R.id.vote_count)
    TextView voteCount;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.genre)
    TextView genre;
    @Bind(R.id.genre_label)
    TextView genreLabel;
    @Bind(R.id.tagline)
    TextView tagline;
    @Bind(R.id.tagline_layout)
    RelativeLayout taglineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        movie = new MovieInList();
        if (getIntent() != null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
            movie = bundle.getParcelable(Constants.MOVIE);
        }
        setData(movie);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementTransition();
        } else {
            posterImage.setVisibility(View.VISIBLE);
            fetchMovieDetails();
        }
    }

    private void setData(final MovieInList movie) {
        if (movie != null) {
            if (movie.getBackdropPath() != null && !movie.getBackdropPath().isEmpty()) {
                Glide.with(this).load(IMAGE_BASE_URL + movie.getBackdropPath())
                        .asBitmap()
                        .placeholder(R.color.lighter_gray)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {

                                    @Override
                                    public void onGenerated(Palette palette) {
                                        setToolbarAndStatusBarColors(palette);
                                    }
                                });
                                return false;
                            }
                        })
                        .into(posterImage);
            }
            if (movie.getOriginalTitle() != null && getSupportActionBar() != null) {
                getSupportActionBar().setTitle(movie.getOriginalTitle());
            }
            if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
                final String posterPath = movie.getPosterPath();
                Glide.with(this).load(IMAGE_BASE_URL
                        + posterPath).placeholder(R.color.lighter_gray).into(thumbImage);
                thumbImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DialogFragment moviePosterDialog = MoviePosterDialogFragment.newInstance(IMAGE_BASE_URL
                                + posterPath, movie.getOriginalTitle());
                        moviePosterDialog.show(getSupportFragmentManager(), MOVIE_POSTER);
                    }
                });
            }
            if (movie.getOverview() != null && !movie.getOverview().isEmpty()) {
                synopsis.setText(movie.getOverview());
            }
            userRating.setText(movie.getVoteAverage() + RATING_OUT_OF);
            voteCount.setText(String.valueOf(movie.getVoteCount()));
            if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
                releaseDate.setText(movie.getReleaseDate());
            }
        }
    }

    private void setToolbarAndStatusBarColors(Palette palette) {
        int colorPrimary = palette.getMutedColor(R.attr.colorPrimary);
        collapsingToolbar.setContentScrimColor(colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int r = Color.red(colorPrimary);
            int b = Color.blue(colorPrimary);
            int g = Color.green(colorPrimary);
            int colorPrimaryDark = Color.rgb((int) (r * COLOR_DARKENING_FRACTION),
                    (int) (g * COLOR_DARKENING_FRACTION), (int) (b * COLOR_DARKENING_FRACTION));
            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(colorPrimaryDark);
        }
    }

    private void setSharedElementTransition() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.change_bounds_with_arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    private void animateRevealShow() {
        int cx = (posterImage.getLeft() + posterImage.getRight()) / 2;
        int cy = (posterImage.getTop() + posterImage.getBottom()) / 2;
        int finalRadius = Math.max(posterImage.getWidth(), posterImage.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(posterImage, cx, cy, 0, finalRadius);
        posterImage.setVisibility(View.VISIBLE);
        anim.setDuration(SHORT_ANIMATION_DURATION);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
        fetchMovieDetails();
    }

    private void fetchMovieDetails() {
        if (Utils.isNetworkAvailable(this)) {
            MoviesApi moviesApi = new MoviesApi();
            moviesApi.getMovieDetails(movie.getId(), getString(R.string.api_key));
        }
    }

    public void onEvent(Movie movie) {
        if (this.movie.getId() == movie.getId()) {
            setMovieDetails(movie);
        }
    }

    private void setMovieDetails(Movie movie) {
        if (movie.getGenres() != null) {
            StringBuilder builder = new StringBuilder();
            for (Genre genre : movie.getGenres()) {
                if (!(builder.toString().isEmpty())) {
                    builder.append(", ");
                }
                builder.append(genre.getName());
            }
            genreLabel.setVisibility(View.VISIBLE);
            genre.setText(builder.toString());
        }
        if (movie.getTagline() != null && !movie.getTagline().isEmpty()) {
            tagline.setVisibility(View.VISIBLE);
            String tag = "";
            tag = tag.concat(DOUBLE_QUOTES).concat(movie.getTagline()).concat(DOUBLE_QUOTES);
            tagline.setText(tag);
            taglineLayout.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(FailureEvent failureEvent) {
        Utils.showToast(failureEvent.getFailureMessageId(), this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
