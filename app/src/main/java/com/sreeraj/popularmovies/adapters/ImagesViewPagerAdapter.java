package com.sreeraj.popularmovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sreeraj.popularmovies.fragments.ImagesFragment;

import java.util.List;

/**
 * Created by Sreeraj on 3/3/16.
 */
public class ImagesViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> imageUrls;

    public ImagesViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                return ImagesFragment.newInstance();
//            case 1:
//                return ImagesFragment.newInstance();
//            default:
//                break;
//        }
//        return null;
        return ImagesFragment.newInstance(imageUrls.get(position));
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public float getPageWidth(int position) {
        return 0.6f;
    }
}
