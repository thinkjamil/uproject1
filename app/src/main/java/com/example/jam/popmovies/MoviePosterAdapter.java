package com.example.jam.popmovies;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jam on 06-Mar-16 0006.
 */
public class MoviePosterAdapter extends ArrayAdapter<MoviePoster> {
    private static final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();

    public MoviePosterAdapter(Activity context, List<MoviePoster> moviePosters) {
        super(context, 0, moviePosters);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoviePoster moviePoster = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_list_item, parent, false);
        }
        ImageView posterView = (ImageView) convertView.findViewById(R.id.poster_image_view);
        String posterAddress="http://image.tmdb.org/t/p/w185/"+moviePoster.poster;
        Log.v(LOG_TAG, "Poster Address::"+ posterAddress);
        posterView.setTag(moviePoster.id);
        Picasso.with(getContext()).load(posterAddress).into(posterView);
        return convertView;
    }

}
