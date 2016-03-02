package com.sreeraj.popularmovies.activities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.api.ImagesApi;
import com.sreeraj.popularmovies.api.MoviesApi;
import com.sreeraj.popularmovies.api.VideoApi;
import com.sreeraj.popularmovies.api.response.ImagesResponseBean;
import com.sreeraj.popularmovies.api.response.VideoResponseBean;
import com.sreeraj.popularmovies.app.Constants;
import com.sreeraj.popularmovies.events.FailureEvent;
import com.sreeraj.popularmovies.fragments.MoviePosterDialogFragment;
import com.sreeraj.popularmovies.models.Genre;
import com.sreeraj.popularmovies.models.Image;
import com.sreeraj.popularmovies.models.Movie;
import com.sreeraj.popularmovies.models.MovieGeneral;
import com.sreeraj.popularmovies.models.Video;
import com.sreeraj.popularmovies.utils.Utils;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * The activity which shows the details of a particular movieGeneral.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    private static final double COLOR_DARKENING_FRACTION = 0.85;
    private static final String RATING_OUT_OF = "/10";
    private static final String MOVIE_POSTER = "movie_poster";
    private static final int SHORT_ANIMATION_DURATION = 200;
    private static final String DOUBLE_QUOTES = "\"";
    private static final String MOVIE = "movie";
    @Bind(R.id.poster_image)
    ImageView posterImage;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.thumb_image)
    ImageView thumbImage;
    @Bind(R.id.synopsis)
    TextView synopsis;
    @Bind(R.id.user_rating)
    TextView userRating;
    @Bind(R.id.vote_count)
    TextView voteCount;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.genre)
    TextView genre;
    @Bind(R.id.genre_label)
    TextView genreLabel;
    @Bind(R.id.tagline)
    TextView tagline;
    @Bind(R.id.tagline_layout)
    CardView taglineLayout;
    @Bind(R.id.overview_card)
    CardView overviewCard;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private MovieGeneral movieGeneral;
    private Movie movie;
    private VideoResponseBean videoResponseBean;
    private ImagesResponseBean imagesResponseBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        movieGeneral = new MovieGeneral();
        if (getIntent() != null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
            movieGeneral = Parcels.unwrap(bundle.getParcelable(Constants.MOVIE_GENERAL));
        }
        if (savedInstanceState != null) {
            movieGeneral = Parcels.unwrap(savedInstanceState.getParcelable(Constants.MOVIE_GENERAL));
            movie = Parcels.unwrap(savedInstanceState.getParcelable(MOVIE));
            if (movie != null) {
                setMovieDetails(movie);
            }
        }
        setData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementTransition();
        } else {
            posterImage.setVisibility(View.VISIBLE);
            fetchMovieDetails();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setSelected(!fab.isSelected());
                movieGeneral.setIsFavourite(fab.isSelected());
                // Code to store the favourite movies in db
            }
        });
    }

    private void setData() {
        if (movieGeneral != null) {
            if (movieGeneral.getBackdropPath() != null && !movieGeneral.getBackdropPath().isEmpty()) {
                Glide.with(this).load(Constants.IMAGE_BASE_URL + movieGeneral.getBackdropPath())
                        .asBitmap()
                        .placeholder(R.color.lighter_gray)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    animateRevealShow();
                                }
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {

                                    @Override
                                    public void onGenerated(Palette palette) {
                                        setToolbarAndStatusBarColors(palette);
                                    }
                                });
                                return false;
                            }
                        })
                        .into(posterImage);
            }
            if (movieGeneral.getOriginalTitle() != null && getSupportActionBar() != null) {
                getSupportActionBar().setTitle(movieGeneral.getOriginalTitle());
            }
            if (movieGeneral.getPosterPath() != null && !movieGeneral.getPosterPath().isEmpty()) {
                final String posterPath = movieGeneral.getPosterPath();
                Glide.with(this).load(Constants.IMAGE_BASE_URL
                        + posterPath).placeholder(R.color.lighter_gray).into(thumbImage);
                thumbImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DialogFragment moviePosterDialog = MoviePosterDialogFragment.newInstance(Constants.IMAGE_BASE_URL
                                + posterPath, movieGeneral.getOriginalTitle());
                        moviePosterDialog.show(getSupportFragmentManager(), MOVIE_POSTER);
                    }
                });
            }
            if (movieGeneral.getOverview() != null && !movieGeneral.getOverview().isEmpty()) {
                synopsis.setText(movieGeneral.getOverview());
                overviewCard.setVisibility(View.VISIBLE);
            }
            String ratingString = "<b>" + movieGeneral.getVoteAverage() + "</b> " + RATING_OUT_OF;
            userRating.setText(Html.fromHtml(ratingString));
            voteCount.setText(String.valueOf(movieGeneral.getVoteCount()));
            if (movieGeneral.getReleaseDate() != null && !movieGeneral.getReleaseDate().isEmpty()) {
                releaseDate.setText(movieGeneral.getReleaseDate());
            }
            fab.setSelected(movieGeneral.isFavourite());
        }
    }

    private void setToolbarAndStatusBarColors(Palette palette) {
        int colorPrimary = palette.getMutedColor(R.attr.colorPrimary);
        collapsingToolbar.setContentScrimColor(colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int r = Color.red(colorPrimary);
            int b = Color.blue(colorPrimary);
            int g = Color.green(colorPrimary);
            int colorPrimaryDark = Color.rgb((int) (r * COLOR_DARKENING_FRACTION),
                    (int) (g * COLOR_DARKENING_FRACTION), (int) (b * COLOR_DARKENING_FRACTION));
            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(colorPrimaryDark);
        }
    }

    private void setSharedElementTransition() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.change_bounds_with_arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                //animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    private void animateRevealShow() {
        int cx = (posterImage.getLeft() + posterImage.getRight()) / 2;
        int cy = (posterImage.getTop() + posterImage.getBottom()) / 2;
        int finalRadius = Math.max(posterImage.getWidth(), posterImage.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(posterImage, cx, cy, 0, finalRadius);
        posterImage.setVisibility(View.VISIBLE);
        anim.setDuration(SHORT_ANIMATION_DURATION);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
        fetchMovieDetails();
    }

    private void fetchMovieDetails() {
        if (Utils.isNetworkAvailable(this)) {
            MoviesApi moviesApi = new MoviesApi();
            moviesApi.getMovieDetails(movieGeneral.getId(), getString(R.string.api_key));

            VideoApi videoApi = new VideoApi();
            videoApi.getVideoDetails(movieGeneral.getId(), getString(R.string.api_key));

            ImagesApi imagesApi = new ImagesApi();
            imagesApi.getImages(movieGeneral.getId(), getString(R.string.api_key));
        }
    }

    public void onEvent(Movie movie) {
        if (this.movieGeneral.getId() == movie.getId()) {
            setMovieDetails(movie);
        }
        EventBus.getDefault().removeStickyEvent(movie);
    }

    private void setMovieDetails(Movie movie) {
        this.movie = movie;
        if (movie.getGenres() != null) {
            StringBuilder builder = new StringBuilder();
            for (Genre genre : movie.getGenres()) {
                if (!(builder.toString().isEmpty())) {
                    builder.append(", ");
                }
                builder.append(genre.getName());
            }
            genreLabel.setVisibility(View.VISIBLE);
            genre.setText(builder.toString());
        }
        if (movie.getTagline() != null && !movie.getTagline().isEmpty()) {
            tagline.setVisibility(View.VISIBLE);
            String tag = "";
            tag = tag.concat(DOUBLE_QUOTES).concat(movie.getTagline()).concat(DOUBLE_QUOTES);
            tagline.setText(tag);
            taglineLayout.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(FailureEvent failureEvent) {
        //Utils.showToast(failureEvent.getFailureMessageId(), this);
        EventBus.getDefault().removeStickyEvent(failureEvent);
    }

    public void onEvent(VideoResponseBean bean) {
        if (movieGeneral.getId() == bean.getId()) {
            setVideoDetails(bean);
        }
        EventBus.getDefault().removeStickyEvent(bean);
    }

    public void setVideoDetails(VideoResponseBean videoResponseBean) {
        this.videoResponseBean = videoResponseBean;
        for (Video video : videoResponseBean.getResults()) {
            video.getSite();

        }
        //http://img.youtube.com/vi/GDFUdMvacI0/0.jpg
    }

    public void onEvent(ImagesResponseBean bean) {
        if (movieGeneral.getId() == bean.getId()) {
            setImagesDisplay(bean);
        }
        EventBus.getDefault().removeStickyEvent(bean);
    }

    public void setImagesDisplay(ImagesResponseBean imagesResponseBean) {
        this.imagesResponseBean = imagesResponseBean;
        for (Image image : imagesResponseBean.getBackdrops()) {
            image.getFilePath();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.MOVIE_GENERAL, Parcels.wrap(movieGeneral));
        if (movie != null) {
            outState.putParcelable(MOVIE, Parcels.wrap(movie));
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
