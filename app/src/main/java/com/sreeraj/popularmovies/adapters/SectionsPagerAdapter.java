package com.sreeraj.popularmovies.adapters;

/**
 * Created by Sreeraj on 2/17/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sreeraj.popularmovies.fragments.PopularMovieListFragment;
import com.sreeraj.popularmovies.fragments.TopRatedMovieListFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"POPULAR", "TOP RATED"};

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return PopularMovieListFragment.newInstance();
            case 1:
                return TopRatedMovieListFragment.newInstance();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
