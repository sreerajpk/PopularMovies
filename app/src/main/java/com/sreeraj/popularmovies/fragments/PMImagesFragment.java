package com.sreeraj.popularmovies.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.activities.PMImagesViewPagerActivity;
import com.sreeraj.popularmovies.app.PMConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to show images in a viewpager
 */
public class PMImagesFragment extends Fragment {

    private static final String POSITION = "position";
    private static final String TYPE = "type";
    private static final String IMAGE_URL = "image_url";
    private static final String VIDEO_URL = "video_url";
    private static final int REQUEST_CODE = 100;
    @Bind(R.id.image)
    ImageView image;
    private String imageUrl;
    private String videoUrl;
    private String type;
    private int position;

    public PMImagesFragment() {
        // Required empty public constructor
    }

    public static PMImagesFragment newInstance(String url, int position, String type) {
        PMImagesFragment fragment = new PMImagesFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, url);
        args.putInt(POSITION, position);
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static PMImagesFragment newInstance(String thumbUrl, String videoUrl, int position, String type) {
        PMImagesFragment fragment = new PMImagesFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, thumbUrl);
        args.putString(VIDEO_URL, videoUrl);
        args.putInt(POSITION, position);
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            imageUrl = bundle.getString(IMAGE_URL);
            position = bundle.getInt(POSITION);
            type = bundle.getString(TYPE);
            videoUrl = bundle.getString(VIDEO_URL);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals(PMConstants.IMAGES)) {
                    Bundle bundle = null;
                    Intent intent = new Intent(getActivity(), PMImagesViewPagerActivity.class);
                    getActivity().startActivityFromFragment(PMImagesFragment.this, intent, REQUEST_CODE);
                } else {
                    //Redirect to Youtube to play video
                    Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    startActivity(playVideoIntent);
                }
            }
        });
        if (type.equals(PMConstants.VIDEOS)) {

        }
    }

    @Override
    public void onStart() {
        Glide.with(this).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.lighter_gray).error(R.drawable.ic_launcher).into(image);
        super.onStart();
    }
}
