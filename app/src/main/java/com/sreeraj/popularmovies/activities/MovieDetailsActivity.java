package com.sreeraj.popularmovies.activities;

import android.animation.Animator;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.app.Constants;
import com.sreeraj.popularmovies.fragments.MoviePosterDialogFragment;
import com.sreeraj.popularmovies.models.MovieInList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final double COLOR_DARKENING_FRACTION = 0.85;
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private static final String RATING_OUT_OF = "/10";
    private static final String MOVIE_POSTER = "movie_poster";
    private static final int SHORT_ANIMATION_DURATION = 200;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        MovieInList movie = new MovieInList();
        if (getIntent() != null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
            movie = bundle.getParcelable(Constants.MOVIE);
        }
        setData(movie);
        setSharedElementTransition();
    }

    private void setData(final MovieInList movie) {
        if (movie != null) {
            if (movie.getBackdropPath() != null) {
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
            if (movie.getPosterPath() != null) {
                final String posterPath = movie.getPosterPath();
                Glide.with(this).load(IMAGE_BASE_URL
                        + posterPath).placeholder(R.color.lighter_gray).into(thumbImage);
                thumbImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DialogFragment moviePosterDialog = MoviePosterDialogFragment.newInstance(IMAGE_BASE_URL
                                + posterPath, movie.getOriginalTitle());
                        moviePosterDialog.show(getSupportFragmentManager(), MOVIE_POSTER);
                        //getWindow().setExitTransition(new Explode());
                    }
                });
            }
            if (movie.getOverview() != null) {
                synopsis.setText(movie.getOverview());
            }
            if (movie.getVoteAverage() != null) {
                userRating.setText(movie.getVoteAverage() + RATING_OUT_OF);
            }
            voteCount.setText(String.valueOf(movie.getVoteCount()));
            if (movie.getReleaseDate() != null) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            supportFinishAfterTransition();
            //onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
