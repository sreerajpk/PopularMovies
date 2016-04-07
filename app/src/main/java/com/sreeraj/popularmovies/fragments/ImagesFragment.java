package com.sreeraj.popularmovies.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.activities.ImagesViewPagerActivity;
import com.sreeraj.popularmovies.app.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Fragment to show images in a viewpager
 */
public class ImagesFragment extends Fragment {

    private static final String POSITION = "position";
    @Bind(R.id.image)
    ImageView image;
    private String url;
    private int position;

    public ImagesFragment() {
        // Required empty public constructor
    }

    public static ImagesFragment newInstance(String url, int position) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putString(Constants.IMAGE_URL, url);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        if (getArguments() != null) {
            url = getArguments().getString(Constants.IMAGE_URL);
            position = getArguments().getInt(POSITION);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(position);
            }
        });
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                Intent intent = new Intent(getActivity(), ImagesViewPagerActivity.class);
                getActivity().startActivityFromFragment(ImagesFragment.this, intent, 100);
            }
        });
    }

    @Override
    public void onStart() {
        Glide.with(this).load(url).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(image);
        super.onStart();
    }
}
