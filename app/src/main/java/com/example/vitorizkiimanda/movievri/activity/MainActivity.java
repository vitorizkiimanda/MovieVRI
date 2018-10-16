package com.example.vitorizkiimanda.movievri.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vitorizkiimanda.movievri.R;
import com.example.vitorizkiimanda.movievri.database.MovieHelper;
import com.example.vitorizkiimanda.movievri.fragment.MovieList;
import com.example.vitorizkiimanda.movievri.fragment.MovieSearch;
import com.example.vitorizkiimanda.movievri.notification.MovieDailyReceiver;
import com.example.vitorizkiimanda.movievri.utils.SettingsPrefActivity;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MainActivity";
    private MovieHelper movieHelper;

    private String currentFragment;
    private Fragment mContent;

    public static final int NOTIFICAITION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //movieHelper db instantiate
        movieHelper = new MovieHelper(this);
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");

        }
        else {

//            default saat apps pertama dibuka
            doNowPlaying();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nowPlaying) {
            doNowPlaying();
        } else if (id == R.id.nav_upComing) {
            doUpComing();
        } else if (id == R.id.nav_search) {
            doSearch();
        } else if (id == R.id.nav_favourite) {
            doFavourite();
        } else if (id == R.id.nav_settings) {
//            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
//            startActivity(mIntent);


            Intent mIntent = new Intent(this, SettingsPrefActivity.class);
            startActivity(mIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void doNowPlaying(){
        this.currentFragment = "movieList";
//        Log.d(TAG, "Now Playing");

        //fragment
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        MovieList mMovieList = new MovieList();

        Bundle data = new Bundle();//create bundle instance
        data.putString("chosen_tab", "nowplaying");//put string to pass with a key value
        mMovieList.setArguments(data);

        mFragmentTransaction.replace(R.id.frame_container, mMovieList, MovieList.class.getSimpleName());
        mFragmentTransaction.commit();
        //fragment ^^^
    }

    public void doUpComing(){
        this.currentFragment = "movieList";
//        Log.d(TAG, "Up Coming");

        //fragment
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        MovieList mMovieList = new MovieList();

        Bundle data = new Bundle();//create bundle instance
        data.putString("chosen_tab", "upcoming");//put string to pass with a key value
        mMovieList.setArguments(data);

        mFragmentTransaction.replace(R.id.frame_container, mMovieList, MovieList.class.getSimpleName());
        mFragmentTransaction.commit();
        //fragment ^^^
    }

    public void doFavourite(){
        this.currentFragment = "movieList";
//        Log.d(TAG, "Favourite");

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

    public void doSearch(){
        this.currentFragment = "search";
//        Log.d(TAG, "Search");

        //fragment
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        MovieSearch mMovieSearch = new MovieSearch();
        mFragmentTransaction.replace(R.id.frame_container, mMovieSearch, MovieSearch.class.getSimpleName());
        mFragmentTransaction.commit();
        //fragment ^^^
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
//        Log.d(TAG, "resumeeeeeeeeeeeeee");
        // Reload current fragment
        Fragment frg = null;

        //refresh after detail page except for serachFragment
        if(this.currentFragment == "movieList"){
            frg = getSupportFragmentManager().findFragmentByTag("MovieList");
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance

        if(this.currentFragment=="search") {
            MovieSearch mMovieSearch = new MovieSearch();
            if (mMovieSearch.isAdded()) {
                getSupportFragmentManager().putFragment(outState, MovieSearch.class.getSimpleName(), mMovieSearch);
            }
        }
        else if(this.currentFragment=="movieList") {
            MovieList mMovieList = new MovieList();
            if (mMovieList.isAdded()) {
                getSupportFragmentManager().putFragment(outState, MovieList.class.getSimpleName(), mMovieList);
            }
        }
    }


    private void sendNotification(Context context, String title, String desc, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("11001", "NOTIFICATION_CHANNEL_NAME",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId("11001");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(id, builder.build());

    }


}
