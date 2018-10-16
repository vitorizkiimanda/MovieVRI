package com.example.moviefavourite;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviefavourite.entity.MovieItems;

import java.util.ArrayList;

public class MovieAdapter<T> extends RecyclerView.Adapter<MovieAdapter<T>.MovieViewHolder>{
    private String TAG = "ADAPTER";
    private Context context;
    private T movieList;
    private OnItemClickListener mListener;

    public T getMovieList() {
        return movieList;
    }

    //interface untuk listener, agara bisa di override di class yang menggunakan
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setListMovies(T  movieList) {
        this.movieList = movieList;
    }

    public MovieAdapter(Context context, T  movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_item_list_viewholder, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {

        MovieItems currentMovie = getItem(position);
        String movieTitle = currentMovie.getTitle();
        String movieDescription = currentMovie.getDescription();
        String movieDate = currentMovie.getYear();
        String moviePoster = currentMovie.getPosterUrl();


        Log.d("item pilem", "judulllll :"+movieTitle);
        Log.d("item pilem", "taunn :"+movieDate);
        Log.d("item pilem", "desc :"+movieDescription);
        Log.d("item pilem", "POSTER APps 2 :"+moviePoster);


        holder.movieTitle.setText(movieTitle);
        holder.movieDescription.setText(movieDescription);
        holder.movieDate.setText(movieDate);
        Glide.with(context).load("http://image.tmdb.org/t/p/w92/"+moviePoster).into(holder.moviePoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CabangOlahragaTabsActivity.start(context, item);
//                Log.d(TAG, "TERCLICKKKKKKKK" + position);

                MovieItems clickedItem = getItem(position);
                context.startActivity(new Intent(context, MovieDetail.class).putExtra("model", clickedItem));
            }
        });

    }

    private MovieItems getItem(int position) {
        if(movieList.getClass() == ArrayList.class){
            return  ((ArrayList<MovieItems>)movieList).get(position);
        }
        else {
            if (!((Cursor) movieList).moveToPosition(position)) {
                throw new IllegalStateException("Position invalid");
            }
            return new MovieItems((Cursor) movieList);
        }
    }

    @Override
    public int getItemCount() {
        if (movieList == null)
            return 0;
        else if(movieList.getClass() == ArrayList.class)
            return ((ArrayList) this.movieList).size();
        else
            return ((Cursor) movieList).getCount();

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView movieTitle;
        public TextView movieDescription;
        public  TextView movieDate;
        public ImageView moviePoster;

        public MovieViewHolder(View itemView){
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movie_title);
            movieDescription = itemView.findViewById(R.id.movie_description);
            movieDate = itemView.findViewById(R.id.movie_date);
            moviePoster = itemView.findViewById(R.id.movie_poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}
