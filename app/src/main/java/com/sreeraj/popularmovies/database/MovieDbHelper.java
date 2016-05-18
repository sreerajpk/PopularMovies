package com.sreeraj.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

/**
 * DBHelper class
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    public static final String TEXT_NOT_NULL = " TEXT NOT NULL, ";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.Movie.TABLE_NAME + " (" +
            MovieContract.Movie._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.Movie.COLUMN_TITLE + TEXT_NOT_NULL +
            MovieContract.Movie.COLUMN_POSTER_URL + TEXT_NOT_NULL +
            MovieContract.Movie.COLUMN_BACK_DROP_URL + TEXT_NOT_NULL +
            MovieContract.Movie.COLUMN_ORIGINAL_TITLE + TEXT_NOT_NULL +
            MovieContract.Movie.COLUMN_PLOT + TEXT_NOT_NULL +
            MovieContract.Movie.COLUMN_RATING + TEXT_NOT_NULL +
            MovieContract.Movie.COLUMN_RELEASE_DATE + TEXT_NOT_NULL +
            MovieContract.Movie.COLUMN_MOVIE_ID + TEXT_NOT_NULL +
            MovieContract.Movie.COLUMN_MOVIE_VOTE_COUNT + TEXT_NOT_NULL +
            "UNIQUE (" + MovieContract.Movie.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.Movie.TABLE_NAME);
        onCreate(db);
    }
}
