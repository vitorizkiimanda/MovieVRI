package com.example.vitorizkiimanda.movievri.utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vitorizkiimanda.movievri.R;
import com.example.vitorizkiimanda.movievri.model.MovieItems;
import com.example.vitorizkiimanda.movievri.notification.MovieDailyReceiver;
import com.example.vitorizkiimanda.movievri.notification.MovieUpcomingReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SettingsPrefActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        SwitchPreference switchReminder;
        SwitchPreference switchToday;

        MovieDailyReceiver movieDailyReceiver = new MovieDailyReceiver();
        MovieUpcomingReceiver movieUpcomingReceiver = new MovieUpcomingReceiver();

        List<MovieItems> notifMovieList;
        private RequestQueue requestQueue;


        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            notifMovieList = new ArrayList<>();

            //volley
            requestQueue = Volley.newRequestQueue(getActivity());

            switchReminder = (SwitchPreference) findPreference(getString(R.string.key_today_reminder));
            switchReminder.setOnPreferenceChangeListener(this);
            switchToday = (SwitchPreference) findPreference(getString(R.string.key_release_reminder));
            switchToday.setOnPreferenceChangeListener(this);


            Preference myPref = findPreference(getString(R.string.key_lang));
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));

                    return true;
                }
            });

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            boolean b = (boolean) newValue;

            if(key.equals(getString(R.string.key_today_reminder))){
                if(b){
                    movieDailyReceiver.setAlarm(getActivity());
                }else{
                    movieDailyReceiver.cancelAlarm(getActivity());
                }
            }else{
                if(b){
                    setReleaseAlarm();
//                    movieUpcomingReceiver.setAlarm(getActivity());
                }else{
                    movieUpcomingReceiver.cancelAlarm(getActivity());
                }
            }

            return true;
        }

        private void setReleaseAlarm(){
            MainPreferenceFragment.GetMovieTask getDataAsync = new MainPreferenceFragment.GetMovieTask();
            getDataAsync.execute("https://api.themoviedb.org/3/movie/upcoming?api_key=d6ea2204529c6bffd564fdcee6792a22&language=en-US");

//            movieCall.enqueue(new Callback<Movie>() {
//                @Override
//                public void onResponse(Call<Movie> call, Response<Movie> response) {
//                    movieList = response.body().getResults();
//                    for(MovieResult movieResult : movieList){
//                        if(movieResult.getReleaseDate().equals(today)){
//                            sameMovieList.add(movieResult);
//                            Log.v("adakah", ""+sameMovieList.size());
//                        }
//                    }
//                    movieUpcomingReceiver.setAlarm(getActivity(),
// sameMovieList);
//
//                }
//
//                @Override
//                public void onFailure(Call<Movie> call, Throwable t) {
//                    Toast.makeText(getActivity(), "Something went wrong"
//                            , Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        public class GetMovieTask extends AsyncTask<String,Void,Void> {

            @Override
            protected Void doInBackground(String... strings) {
                getData(strings[0]);
                return null;
            }
        }


        //api
        public void getData(String url){

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();
            final String today = dateFormat.format(date);

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
                            movieItem.setTitle(data.getString("title"));
                            movieItem.setDescription(data.getString("overview"));
                            movieItem.setPosterUrl(data.getString("poster_path"));

//                          Log.d(TAG, "judul pilem "+String.valueOf(movieItem.getTitle()));
                            if (data.getString("release_date").equals(today)) {
                                notifMovieList.add(movieItem); //ini kan tadinya ArrayList<MovieItems>
                            }
                        }
                        Log.d("notifMovieList", "notifUpocoming\n\n"+String.valueOf(notifMovieList));
                        movieUpcomingReceiver.setAlarm(getActivity(), notifMovieList);

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
        //api ^^^
    }
}
