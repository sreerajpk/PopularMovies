package com.sreeraj.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.app.PMConstants;
import com.sreeraj.popularmovies.fragments.PMMovieDetailsFragment;

/**
 * The activity which shows the details of a particular movieGeneral.
 */

public class PMMovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        loadDetailsFragment(savedInstanceState);
    }

    private void loadDetailsFragment(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (getIntent() != null) {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra(PMConstants.BUNDLE);
                PMMovieDetailsFragment fragment = new PMMovieDetailsFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, fragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
