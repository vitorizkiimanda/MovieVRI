package com.example.moviefavourite;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;

import static com.example.moviefavourite.DatabaseContract.CONTENT_URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieList extends Fragment {


    private String TAG = "MovieList Fragment";
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private MovieAdapter movieAdapter;
    private Cursor list;
    private Cursor movieList;

    public MovieList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding
        View view =  inflater.inflate(R.layout.fragment_movie_list, container, false);
        recyclerView = view.findViewById(R.id.rvMovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        movieAdapter = new MovieAdapter(getActivity(), movieList);
        movieAdapter.setListMovies(list);
        recyclerView.setAdapter(movieAdapter);

        new LoadNoteAsync().execute();


        return view;

    }


    private class LoadNoteAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Cursor doInBackground(Void... voids) {
//            return getContentResolver().query(CONTENT_URI,null,null,null,null);
            return getContext().getContentResolver().query(CONTENT_URI,null,null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor  movieItems) {
            super.onPostExecute(movieItems);

            list = movieItems;


            movieAdapter = new MovieAdapter(getActivity(), list);
//            movieAdapter.setListMovies(list);
            recyclerView.setAdapter(movieAdapter);

        }
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
    }

}
