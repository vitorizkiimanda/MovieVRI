package com.example.vitorizkiimanda.movievri.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.vitorizkiimanda.movievri.model.MovieItems;
import com.example.vitorizkiimanda.movievri.provider.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieSearch extends Fragment{
    private String TAG = "MovieSearch Fragment";

    private EditText searchInputBar;
    private Button searchButton;
    private RecyclerView recyclerView;

    private String resultString;
    private ArrayList<MovieItems> movieList;

    private RequestQueue requestQueue;

    private MovieAdapter movieAdapter;

    public MovieSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //binding
        View view =  inflater.inflate(R.layout.fragment_movie_search, container, false);
        searchInputBar = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.rvMovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //instantiate
        this.movieList = new ArrayList<>();

        if(savedInstanceState!=null){
//            Log.d("saved","ambil dari saved Search");
            this.movieList = savedInstanceState.getParcelableArrayList("data_saved");
            movieAdapter = new MovieAdapter(getActivity(), this.movieList);
            recyclerView.setAdapter(movieAdapter);
        }else {

//            Log.d("terpanggil", "terpanggil search");

            //trigger search button
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resultString = String.valueOf(searchInputBar.getText());

                    //instantiate
                    movieList = new ArrayList<>();
                    movieAdapter = new MovieAdapter(getActivity(), movieList);
                    recyclerView.setAdapter(movieAdapter);

                    GetMovieTask getDataAsync = new GetMovieTask();
                    getDataAsync.execute(resultString);
//                Log.d(TAG, resultString);
                }
            });

            //volley
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        return view;
    }

    //search api
    public void getData(String result){
        String url = "https://api.themoviedb.org/3/search/movie?api_key=d6ea2204529c6bffd564fdcee6792a22&language=en-US&query=";
        url = url+result;
//        Log.d(TAG,"url: "+url);
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

                        movieList.add(movieItem);
                    }
//                    Log.d("data","dataBARUU:"+movieList);
                    movieAdapter = new MovieAdapter(getActivity(), movieList);
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
    //search api ^^^


    public class GetMovieTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {
            getData(strings[0]);
            return null;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            this.movieList = savedInstanceState.getParcelableArrayList("data_saved");
//            Log.d("data","datanyaaaa:"+list);
            movieAdapter = new MovieAdapter(getActivity(), this.movieList);
            recyclerView.setAdapter(movieAdapter);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putParcelableArrayList("data_saved", this.movieList);
    }


}
