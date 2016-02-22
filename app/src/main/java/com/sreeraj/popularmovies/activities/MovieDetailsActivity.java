package com.sreeraj.popularmovies.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.app.Constants;
import com.sreeraj.popularmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final double COLOR_DARKENING_FRACTION = 0.85;
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private static final String RATING_OUT_OF = "/10";

    private ImageView posterImage;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView thumbImage;
    private TextView synopsis;
    private TextView userRating;
    private TextView voteCount;
    private TextView releaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();

        Movie movie = new Movie();
        if (getIntent() != null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
            movie = bundle.getParcelable(Constants.MOVIE);
        }
        setData(movie);
    }

    private void initViews() {
        posterImage = (ImageView) findViewById(R.id.poster_image);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        thumbImage = (ImageView) findViewById(R.id.thumb_image);
        synopsis = (TextView) findViewById(R.id.synopsis);
        userRating = (TextView) findViewById(R.id.user_rating);
        voteCount = (TextView) findViewById(R.id.vote_count);
        releaseDate = (TextView) findViewById(R.id.release_date);
    }

    private void setData(Movie movie) {
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
                Glide.with(this).load(IMAGE_BASE_URL
                        + movie.getPosterPath()).placeholder(R.color.lighter_gray).into(thumbImage);
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

        int r = Color.red(colorPrimary);
        int b = Color.blue(colorPrimary);
        int g = Color.green(colorPrimary);
        int colorPrimaryDark = Color.rgb((int) (r * COLOR_DARKENING_FRACTION),
                (int) (g * COLOR_DARKENING_FRACTION), (int) (b * COLOR_DARKENING_FRACTION));

        collapsingToolbar.setContentScrimColor(colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(colorPrimaryDark);
        }
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
