package com.example.vitorizkiimanda.movievri.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public DatabaseContract(){}

    public static final String TABLE_NAME = "favourite_movie";

    public static final class MovieColumns implements BaseColumns {

        public static  String TITLE = "title";
        public static  String YEAR = "year";
        public static  String DESCRIPTION = "description";
        public static  String MOVIEPOSTER = "movie_poster";
        public static  String FAVSTATUS = "favstatus";
    }


    public static final String AUTHORITY = "com.example.vitorizkiimanda.movievri";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }

}