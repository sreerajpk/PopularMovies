package com.sreeraj.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView posterImage;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView thumbImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        posterImage = (ImageView) findViewById(R.id.poster_image);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        thumbImage = (ImageView) findViewById(R.id.thumb_image);

        Movie movie = new Movie();
        if (getIntent() != null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("bundle");
            movie = bundle.getParcelable("movie");
        }
        if (movie != null) {
            if (movie.getBackdropPath() != null) {
                Glide.with(this).load("http://image.tmdb.org/t/p/w500" +
                        movie.getBackdropPath()).placeholder(R.color.lighter_gray).into(posterImage);
//                Bitmap bitmap = ((BitmapDrawable) posterImage.getDrawable()).getBitmap();
//                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//                        int mutedColor = palette.getMutedColor(ContextCompat.getColor(MovieDetailsActivity.this, R.attr.colorPrimary));
//                        collapsingToolbar.setContentScrimColor(mutedColor);
//                    }
//                });
            }
            if (movie.getOriginalTitle() != null) {
                getSupportActionBar().setTitle(movie.getOriginalTitle());
            }
            if (movie.getPosterPath() != null) {
                Glide.with(this).load("http://image.tmdb.org/t/p/w500" +
                        movie.getPosterPath()).placeholder(R.color.lighter_gray).into(thumbImage);
            }
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
