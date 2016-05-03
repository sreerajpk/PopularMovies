package com.sreeraj.popularmovies.adapters;

/**
 * Created by Sreeraj on 2/17/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sreeraj.popularmovies.app.PMConstants;
import com.sreeraj.popularmovies.fragments.PMMovieListFragment;

/**
 * A FragmentStatePagerAdapter that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PMSectionsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"POPULAR", "TOP RATED"};

    public PMSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return PMMovieListFragment.newInstance(PMConstants.POPULAR);
            case 1:
                return PMMovieListFragment.newInstance(PMConstants.TOP_RATED);
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
