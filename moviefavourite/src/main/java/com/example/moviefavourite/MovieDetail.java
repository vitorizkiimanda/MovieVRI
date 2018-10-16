package com.example.moviefavourite;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviefavourite.entity.MovieItems;

public class MovieDetail extends AppCompatActivity{
    private String TAG = "MovieDetail";

    private ImageView imagePoster;
    private TextView movieTitle;
    private TextView movieDate;
    private TextView moviePopularity;
    private TextView movieDescription;

    private Bundle bundle;
    private MovieItems model;



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


        bundle = getIntent().getExtras();
        model = bundle.getParcelable("model");

        movieTitle.setText(model.getTitle());
        movieDescription.setText(model.getDescription());
        movieDate.setText(" ("+model.getYear()+")");
        Glide.with(this).load("http://image.tmdb.org/t/p/w92/"+model.getPosterUrl()).into(imagePoster);
    }


}
