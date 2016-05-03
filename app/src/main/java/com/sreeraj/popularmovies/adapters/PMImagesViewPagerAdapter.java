package com.sreeraj.popularmovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sreeraj.popularmovies.app.PMConstants;
import com.sreeraj.popularmovies.fragments.PMImagesFragment;

import java.util.List;

/**
 * Created by Sreeraj on 3/3/16.
 */
public class PMImagesViewPagerAdapter extends FragmentStatePagerAdapter {

    private String type;
    private List<String> imageUrls;
    private List<String> videoUrls;

    public PMImagesViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setImageUrls(List<String> imageUrls, String type) {
        this.imageUrls = imageUrls;
        this.type = type;
    }

    public void setVideoAndThumbImagesUrls(List<String> imageUrls, List<String> videoUrls, String type) {
        this.imageUrls = imageUrls;
        this.videoUrls = videoUrls;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        if (type.equals(PMConstants.IMAGES)) {
            return PMImagesFragment.newInstance(imageUrls.get(position), position, type);
        } else {
            return PMImagesFragment.newInstance(imageUrls.get(position), videoUrls.get(position), position, type);
        }
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public float getPageWidth(int position) {
        if (imageUrls.size() == 1) {
            return 1f;
        } else {
            return 0.6f;
        }
    }
}
