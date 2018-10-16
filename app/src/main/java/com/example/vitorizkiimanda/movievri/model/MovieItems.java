package com.example.vitorizkiimanda.movievri.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vitorizkiimanda.movievri.database.DatabaseContract;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.getColumnInt;
import static com.example.vitorizkiimanda.movievri.database.DatabaseContract.getColumnString;

public class MovieItems implements Parcelable {

    private int id;
    private String idmovie;
    private String title;
    private String year;
    private Double ratingValue;
    private Integer voteValue;
    private String description;
    private String posterUrl;
    private String imageUrl;
    private Integer popularityValue;
    private String favouriteStatus;

    public MovieItems() {
        this.idmovie="";
        this.title="";
        this.year="";
        this.ratingValue=0.0;
        this.voteValue=0;
        this.description="";
        this.posterUrl="";
        this.imageUrl="";
        this.popularityValue=0;
        this.favouriteStatus="false";
    }

    public String getIdmovie() {
        return idmovie;
    }

    public void setIdmovie(String idmovie) {
        this.idmovie = idmovie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Integer getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(Integer voteValue) {
        this.voteValue = voteValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPopularityValue() {
        return popularityValue;
    }

    public void setPopularityValue(Integer popularityValue) {
        this.popularityValue = popularityValue;
    }

    public String getFavouriteStatus() {
        return favouriteStatus;
    }

    public void setFavouriteStatus(String favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.idmovie);
        dest.writeString(this.title);
        dest.writeString(this.year);
        dest.writeValue(this.ratingValue);
        dest.writeValue(this.voteValue);
        dest.writeString(this.description);
        dest.writeString(this.posterUrl);
        dest.writeString(this.imageUrl);
        dest.writeValue(this.popularityValue);
        dest.writeString(this.favouriteStatus);
    }

    public MovieItems(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.description = getColumnString(cursor, DatabaseContract.MovieColumns.DESCRIPTION);
        this.year = getColumnString(cursor, DatabaseContract.MovieColumns.YEAR);
        this.favouriteStatus = getColumnString(cursor, DatabaseContract.MovieColumns.FAVSTATUS);
        this.posterUrl = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIEPOSTER);
    }

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.idmovie = in.readString();
        this.title = in.readString();
        this.year = in.readString();
        this.ratingValue = (Double) in.readValue(Double.class.getClassLoader());
        this.voteValue = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.posterUrl = in.readString();
        this.imageUrl = in.readString();
        this.popularityValue = (Integer) in.readValue(Integer.class.getClassLoader());
        this.favouriteStatus = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}
