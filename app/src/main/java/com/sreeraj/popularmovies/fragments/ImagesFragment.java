package com.sreeraj.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.app.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to show images in a viewpager
 */
public class ImagesFragment extends Fragment {

    @Bind(R.id.image)
    ImageView image;

    public ImagesFragment() {
        // Required empty public constructor
    }

    public static ImagesFragment newInstance(String url) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putString(Constants.IMAGE_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        String url = "";
        if (getArguments() != null) {
            url = getArguments().getString(Constants.IMAGE_URL);
        }
        Glide.with(this).load(url).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(image);
        super.onStart();
    }
}
