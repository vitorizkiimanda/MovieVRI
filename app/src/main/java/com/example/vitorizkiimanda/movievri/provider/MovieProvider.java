package com.example.vitorizkiimanda.movievri.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.vitorizkiimanda.movievri.database.DatabaseContract;
import com.example.vitorizkiimanda.movievri.database.MovieHelper;

import java.sql.SQLException;

import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.AUTHORITY;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.CONTENT_URI;

public class MovieProvider  extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        // content://com.dicoding.mynotesapp/note
        sUriMatcher.addURI(AUTHORITY, DatabaseContract.TABLE_NAME, MOVIE);

        // content://com.dicoding.mynotesapp/note/id
        sUriMatcher.addURI(AUTHORITY,
                DatabaseContract.TABLE_NAME+ "/#",
                MOVIE_ID);
    }

    private MovieHelper movieHelper;

    @Override
    public boolean onCreate() {
        movieHelper = new MovieHelper(getContext());
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch(sUriMatcher.match(uri)){
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }

        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        long added ;

        switch (sUriMatcher.match(uri)){
            case MOVIE:
                added = movieHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated ;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated =  movieHelper.updateProvider(uri.getLastPathSegment(),contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted =  movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

}