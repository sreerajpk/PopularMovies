package com.sreeraj.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sreeraj.popularmovies.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment which shows a large poster image and title of the movie.
 */
public class MoviePosterDialogFragment extends DialogFragment {

    private static final String URL = "url";
    private static final String TITLE = "title";

    @Bind(R.id.dialog_poster_image)
    ImageView posterImage;
    @Bind(R.id.movie_title)
    TextView movieTitle;

    @NonNull
    public static MoviePosterDialogFragment newInstance(String url, String title) {
        MoviePosterDialogFragment fragment = new MoviePosterDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        String url = "";
        String title = "";
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            url = bundle.getString(URL);
            title = bundle.getString(TITLE);
        }
        View view = inflater.inflate(R.layout.movie_poster_dialog_fragment, container, false);
        ButterKnife.bind(this, view);

        if (url != null && !url.isEmpty()) {
            Glide.with(getActivity()).load(url).thumbnail(0.3f).into(posterImage);
        }
        if (title != null && !title.isEmpty()) {
            movieTitle.setText(title);
        }
        return view;
    }
}

