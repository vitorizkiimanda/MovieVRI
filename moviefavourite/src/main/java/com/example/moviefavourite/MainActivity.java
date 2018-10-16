package com.example.moviefavourite;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fragment
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        MovieList mMovieList = new MovieList();

        Bundle data = new Bundle();//create bundle instance
        data.putString("chosen_tab", "favourite");//put string to pass with a key value
        mMovieList.setArguments(data);

        mFragmentTransaction.replace(R.id.frame_container, mMovieList, MovieList.class.getSimpleName());
        mFragmentTransaction.commit();
        //fragment ^^^
    }
}
