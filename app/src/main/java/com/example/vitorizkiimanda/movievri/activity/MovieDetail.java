package com.example.vitorizkiimanda.movievri.activity;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vitorizkiimanda.movievri.R;
import com.example.vitorizkiimanda.movievri.database.DatabaseContract;
import com.example.vitorizkiimanda.movievri.database.MovieHelper;
import com.example.vitorizkiimanda.movievri.model.MovieItems;
import com.example.vitorizkiimanda.movievri.widget.FavouriteWidget;

import java.sql.SQLException;

import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.CONTENT_URI;

public class MovieDetail extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "MovieDetail";

    private ImageView imagePoster;
    private TextView movieTitle;
    private TextView movieDate;
    private TextView moviePopularity;
    private TextView movieDescription;

    private Intent intent;
    private Bundle bundle;
    private MovieItems model;

    private ImageView btn_fav;

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    private MovieItems movieItems;
    private int position;
    private MovieHelper movieHelper;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //binding
        imagePoster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
        movieDate = findViewById(R.id.movie_date);
//        moviePopularity = findViewById(R.id.movie_popularity);
        movieDescription = findViewById(R.id.movie_description);
        btn_fav = findViewById(R.id.btn_fav);
        btn_fav.setOnClickListener(this);

        //movieHelper db instantiate
        movieHelper = new MovieHelper(this);
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bundle = getIntent().getExtras();
        model = bundle.getParcelable("model");

        movieTitle.setText(model.getTitle());
        movieDescription.setText(model.getDescription());
        movieDate.setText(" ("+model.getYear()+")");
        Glide.with(this).load("http://image.tmdb.org/t/p/w92/"+model.getPosterUrl()).into(imagePoster);
        this.id = model.getId();

        String temp = model.getFavouriteStatus();

        if(temp.equals("true") || isRecordExists(model.getTitle())){
            btn_fav.setImageResource(R.drawable.ic_star_black_24dp);
        }
        else if(temp.equals("false")){
            btn_fav.setImageResource(R.drawable.ic_star_border_black_24dp);
//            Log.d("btn_fav","falseeeee");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_fav){

            String temp = model.getFavouriteStatus();

            if (temp.equals("true") || isRecordExists(model.getTitle())) {
                btn_fav.setImageResource(R.drawable.ic_star_border_black_24dp);
                movieHelper.delete(model.getTitle());
                setResult(RESULT_DELETE);

                showAlertDialog(301);
            }
            else{
                btn_fav.setImageResource(R.drawable.ic_star_black_24dp);
                MovieItems newMovie = new MovieItems();
                newMovie.setTitle(model.getTitle());
                newMovie.setDescription(model.getDescription());
                newMovie.setYear(model.getYear());
                newMovie.setPosterUrl(model.getPosterUrl());
                newMovie.setFavouriteStatus("true");


                movieHelper.insert(newMovie);
                setResult(RESULT_ADD);

                showAlertDialog(101);
            }

            //update widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ComponentName thisWidget = new ComponentName(this, FavouriteWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            //update widget^^
        }
    }

    public void showAlertDialog(int type){
        String dialogTitle = null, dialogMessage = null;

        if (type == 101){
            dialogTitle = "Ditambahkan ke Favorit";
//            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        }else{
            dialogMessage = "Dihapus dari Favorit";
//            dialogTitle = "Hapus Note";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean isRecordExists(String title) {
        Boolean exist = false;

        Cursor cursor = getContentResolver().query(CONTENT_URI,null,null,null,null,null);
        //loop check title
        while (cursor.moveToNext()){
            Log.d("status", "position : "+ cursor.getPosition());
            if(model.getTitle().equals(cursor.getString(cursor.getColumnIndex("title")))){
                exist = true;
            }
        }
        return exist;
    }

}
