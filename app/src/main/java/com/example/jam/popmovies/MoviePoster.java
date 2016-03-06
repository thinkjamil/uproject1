package com.example.jam.popmovies;

/**
 * Created by jam on 06-Mar-16 0006.
 */
public class MoviePoster {
    public String movieTitle;
    public String id;
    public String poster;
    public String overview;
    public String releasedDate;
    public String rating;

    public MoviePoster(String title, String poster,String id)
    {
        this.movieTitle=title;
        this.poster=poster;
        this.id=id;
    }
    public MoviePoster(String title, String poster,String id,String overview,String releasedDate,String rating)
    {
        this.movieTitle=title;
        this.poster=poster;
        this.id=id;
        this.overview=overview;
        this.rating=rating;
        this.releasedDate=releasedDate;
    }
}
