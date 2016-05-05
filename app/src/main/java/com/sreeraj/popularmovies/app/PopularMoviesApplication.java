package com.sreeraj.popularmovies.app;

import android.app.Application;

/**
 * Created by Sreeraj on 5/5/16.
 */
public class PopularMoviesApplication extends Application {

    public static boolean isTwoPane;

    public static boolean isTwoPane() {
        return isTwoPane;
    }

    public static void setIsTwoPane(boolean isTwoPane) {
        PopularMoviesApplication.isTwoPane = isTwoPane;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
