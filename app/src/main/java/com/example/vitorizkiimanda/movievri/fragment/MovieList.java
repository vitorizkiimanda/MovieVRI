package com.example.vitorizkiimanda.movievri.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vitorizkiimanda.movievri.R;
import com.example.vitorizkiimanda.movievri.database.DatabaseContract;
import com.example.vitorizkiimanda.movievri.database.MovieHelper;
import com.example.vitorizkiimanda.movievri.model.MovieItems;
import com.example.vitorizkiimanda.movievri.provider.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.CONTENT_URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieList extends Fragment {


    private String TAG = "MovieList Fragment";

    private EditText searchInputBar;
    private Button searchButton;
    private RecyclerView recyclerView;

    private String resultString;
    private Cursor movieList;
    private ArrayList<MovieItems> movieListArrayList;

    private RequestQueue requestQueue;

    private MovieAdapter movieAdapter;
    private MovieHelper movieHelper;
    private Cursor list;

    public MovieList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding
        View view =  inflater.inflate(R.layout.fragment_movie_list, container, false);
        searchInputBar = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.rvMovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //instantiate
        this.movieListArrayList = new ArrayList<>();

        //volley
        requestQueue = Volley.newRequestQueue(getActivity());

        String chosenTab = getArguments().getString("chosen_tab");
        if(savedInstanceState!=null && chosenTab!="favourite"){
            Log.d("saved","ambil dari saved");
            this.movieListArrayList = savedInstanceState.getParcelableArrayList("data_saved");
            movieAdapter = new MovieAdapter(getActivity(), this.movieListArrayList);
            recyclerView.setAdapter(movieAdapter);
        }else {

            Log.d("terpanggil","terpanggil");

            //Get Argument passed from previous
            if (getArguments() != null) {
                if (chosenTab == "upcoming") {
                    MovieList.GetMovieTask getDataAsync = new MovieList.GetMovieTask();
                    getDataAsync.execute("https://api.themoviedb.org/3/movie/upcoming?api_key=d6ea2204529c6bffd564fdcee6792a22&language=en-US");
                } else if (chosenTab == "nowplaying") {
                    MovieList.GetMovieTask getDataAsync = new MovieList.GetMovieTask();
                    getDataAsync.execute("https://api.themoviedb.org/3/movie/now_playing?api_key=d6ea2204529c6bffd564fdcee6792a22&language=en-US");
                } else if (chosenTab == "favourite") {
                    //movieHelper db instantiate
                    movieHelper = new MovieHelper(getActivity());
                    try {
                        movieHelper.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //
                    movieAdapter = new MovieAdapter(getActivity(), movieList);
                    movieAdapter.setListMovies(list);
                    recyclerView.setAdapter(movieAdapter);
                    new LoadNoteAsync().execute();
                }
            }
        }


        return view;

    }
    //api
    public void getData(String url){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,  null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0 ; i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);

                        //bikin botol kosong
                        MovieItems movieItem = new MovieItems();

                        //inject data ke botol
                        movieItem.setTitle(data.getString("title"));
                        movieItem.setYear(data.getString("release_date"));
                        movieItem.setRatingValue(data.getDouble("vote_average"));
                        movieItem.setTitle(data.getString("title"));
                        movieItem.setVoteValue(data.getInt("vote_count"));
                        movieItem.setDescription(data.getString("overview"));
                        movieItem.setPosterUrl(data.getString("poster_path"));
                        movieItem.setImageUrl(data.getString("backdrop_path"));
                        movieItem.setPopularityValue(data.getInt("popularity"));

//                        Log.d(TAG, "judul pilem "+String.valueOf(movieItem.getTitle()));

                        movieListArrayList.add(movieItem); //ini kan tadinya ArrayList<MovieItems>
                    }
                    movieAdapter = new MovieAdapter(getActivity(), movieListArrayList);
                    recyclerView.setAdapter(movieAdapter);
//                    movieAdapter.setOnItemClickListener((MovieAdapter.OnItemClickListener) getActivity());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
    // api ^^^


    public class GetMovieTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {
            getData(strings[0]);
            return null;
        }
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            this.movieListArrayList = savedInstanceState.getParcelableArrayList("data_saved");
            movieAdapter = new MovieAdapter(getActivity(), this.movieListArrayList);
            recyclerView.setAdapter(movieAdapter);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putParcelableArrayList("data_saved", this.movieListArrayList);
    }


}
