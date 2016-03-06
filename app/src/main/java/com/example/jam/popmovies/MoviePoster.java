package com.example.jam.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jam on 06-Mar-16 0006.
 */
public class MoviePoster implements Parcelable {
    public String movieTitle;
    public String id;
    public String poster;
    public String overview;
    public String releasedDate;
    public String rating;

    public MoviePoster(String title, String poster, String id) {
        this.movieTitle = title;
        this.poster = poster;
        this.id = id;
    }

    public MoviePoster(String title, String poster, String id, String overview, String releasedDate, String rating) {
        this.movieTitle = title;
        this.id = id;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
        this.releasedDate = releasedDate;
    }
    public MoviePoster(Parcel in) {
        this.movieTitle = in.readString();
        this.poster = in.readString();
        this.id = in.readString();
        this.overview = in.readString();
        this.rating = in.readString();
        this.releasedDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(id);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(releasedDate);

    }

    public static final Parcelable.Creator<MoviePoster> CREATOR
            = new Parcelable.Creator<MoviePoster>() {
        public MoviePoster createFromParcel(Parcel in) {
            return new MoviePoster(in);
        }
        public MoviePoster[] newArray(int size) {
            return new MoviePoster[size];
        }
    };
}
