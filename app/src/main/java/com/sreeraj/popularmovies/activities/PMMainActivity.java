package com.sreeraj.popularmovies.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.adapters.PMSectionsPagerAdapter;
import com.sreeraj.popularmovies.app.PopularMoviesApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * The main activity.
 */

public class PMMainActivity extends AppCompatActivity {

    @Bind(R.id.container)
    ViewPager mViewPager;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setViews();
    }

    private void setViews() {
        if (findViewById(R.id.movie_detail_container) != null) {
            PopularMoviesApplication.setIsTwoPane(true);
        } else {
            PopularMoviesApplication.setIsTwoPane(false);
        }
        setSupportActionBar(toolbar);
        PMSectionsPagerAdapter mSectionsPagerAdapter = new PMSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
