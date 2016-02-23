package com.sreeraj.popularmovies.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.adapters.SectionsPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * The main activity
 */

public class MainActivity extends AppCompatActivity {

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
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the t
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Setup the Tabs
        tabLayout.setupWithViewPager(mViewPager);
    }
}
