package com.example.vitorizkiimanda.movievri.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vitorizkiimanda.movievri.model.MovieItems;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.MovieColumns.DESCRIPTION;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.MovieColumns.FAVSTATUS;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.MovieColumns.MOVIEPOSTER;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.MovieColumns.TITLE;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.MovieColumns.YEAR;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.TABLE_NAME;

public class MovieHelper {
    private static String DATABASE_TABLE = TABLE_NAME;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public MovieHelper(Context context){
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public ArrayList<MovieItems> query(){
        ArrayList<MovieItems> arrayList = new ArrayList<MovieItems>();
        Cursor cursor = database.query(DATABASE_TABLE,null,null,null,null,null,_ID +" DESC",null);
        cursor.moveToFirst();
        MovieItems movieItems;
        if (cursor.getCount()>0) {
            do {

                movieItems = new MovieItems();
                movieItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movieItems.setYear(cursor.getString(cursor.getColumnIndexOrThrow(YEAR)));
                movieItems.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                movieItems.setFavouriteStatus(cursor.getString(cursor.getColumnIndexOrThrow(FAVSTATUS)));
                movieItems.setPosterUrl(cursor.getString(cursor.getColumnIndexOrThrow(MOVIEPOSTER)));

                arrayList.add(movieItems);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(MovieItems movieItems){
        ContentValues initialValues =  new ContentValues();
        initialValues.put(TITLE, movieItems.getTitle());
        initialValues.put(YEAR, movieItems.getYear());
        initialValues.put(DESCRIPTION, movieItems.getDescription());
        initialValues.put(FAVSTATUS, movieItems.getFavouriteStatus());
        initialValues.put(MOVIEPOSTER, movieItems.getPosterUrl());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(MovieItems movieItems){
        ContentValues args = new ContentValues();
        args.put(TITLE, movieItems.getTitle());
        args.put(YEAR, movieItems.getYear());
        args.put(DESCRIPTION, movieItems.getDescription());
        args.put(FAVSTATUS, movieItems.getFavouriteStatus());
        args.put(MOVIEPOSTER, movieItems.getPosterUrl());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + movieItems.getId() + "'", null);
    }

    public int delete(String title){
        return database.delete(TABLE_NAME, TITLE + " = '"+title+"'", null);
    }


    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }
    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }
    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }
    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values,_ID +" = ?",new String[]{id} );
    }
    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,_ID + " = ?", new String[]{id});
    }

}