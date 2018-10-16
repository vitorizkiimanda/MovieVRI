package com.example.vitorizkiimanda.movievri.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.vitorizkiimanda.movievri.R;
import com.example.vitorizkiimanda.movievri.database.DatabaseContract;
import com.example.vitorizkiimanda.movievri.model.MovieItems;
import com.example.vitorizkiimanda.movievri.model.MovieFavorite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class StackRemoteViewsFactory implements
        RemoteViewsService.RemoteViewsFactory {

    private List<Bitmap>mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private MovieFavorite getFav(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }

        return new MovieFavorite(
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MOVIEPOSTER)));
    }


    public void onCreate() {
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),
//                R.drawable.darth_vader));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),
//                R.drawable.star_wars_logo));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),
//                R.drawable.storm_trooper));
//
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.starwars));
//
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.falcon));

        cursor = mContext.getContentResolver().query(
                DatabaseContract.CONTENT_URI,
                null,
                null,
                null,
                null
        );

//        for(int i=0 ; i<getCount();i++){
//
//            if (!cursor.moveToPosition(i)) {
//                throw new IllegalStateException("Position invalid!");
//            }
//
//            MovieItems movieItems = new MovieItems(cursor);
//
//            Log.d("url :","url movie :"+movieItems.getPosterUrl()+"\n\n");


//            Bitmap bmp = null;
//
//            try {
//                Log.d("url poto", "url fotoku :"+ movieItems.getPosterUrl());
//                bmp = Glide.with(mContext)
//                    .asBitmap()
//                    .load("http://image.tmdb.org/t/p/w92/"+movieItems.getPosterUrl())
//                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get();
//                mWidgetItems.add(bmp);
//            }catch (InterruptedException | ExecutionException e){
//                Log.d("Widget Load Error","error");
//            }

//        }


    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        cursor = mContext.getContentResolver().query(
                DatabaseContract.CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
//        return mWidgetItems.size();
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        MovieFavorite movieFavorite = getFav(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);




//        rv.setImageViewBitmap(R.id.imageView,mWidgetItems.get(position));

        MovieItems movieItems = new MovieItems(cursor);
        if (!cursor.moveToPosition(position)) {
                throw new IllegalStateException("Position invalid!");
        }

//        Log.d("url :","url movie :"+movieItems.getPosterUrl()+"\n\n");

        Bitmap bmp = null;

        try {
            bmp = Glide.with(mContext)
                .asBitmap()
                .load("http://image.tmdb.org/t/p/w185/"+movieItems.getPosterUrl())
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
            rv.setImageViewBitmap(R.id.imageView,bmp);
        }catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error","error");
        }




        Bundle extras = new Bundle();
        extras.putInt(FavouriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return cursor.moveToPosition(i) ? cursor.getLong(0) : i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}