package com.example.jam.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainFragment extends Fragment {
    private MoviePosterAdapter moviePosterAdapter;
    MoviePoster[] sampleMovies = {
            new MoviePoster("Deadpool","/inVq3FRqcYIRl2la8iZikYYxFNR.jpg","293660"),
            new MoviePoster("Mad Max: Fury Road","/kqjL17yufvn9OVLyXYpvtyrFfak.jpg","76341")
    };

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag_root_view = inflater.inflate(R.layout.fragment_main, container, false);
        moviePosterAdapter = new MoviePosterAdapter(getActivity(), new ArrayList<>(Arrays.asList(sampleMovies)));
        GridView gridView = (GridView) frag_root_view.findViewById(R.id.poster_grid);
        gridView.setAdapter(moviePosterAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent show_detail = new Intent(getContext(), DetailedActivity.class);
                show_detail.putExtra("id", view.getTag().toString());
                startActivity(show_detail);
            }
        });
        updateMoviesUI();
        return frag_root_view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            FetchMoviesTask fetchMoviesTaskTask = new FetchMoviesTask();
            fetchMoviesTaskTask.execute("popular");
        return super.onOptionsItemSelected(item);
    }

    class FetchMoviesTask extends AsyncTask<String,Void,ArrayList<MoviePoster>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        @Override
        protected ArrayList<MoviePoster> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"/";
                final String APPID_PARAM = "api_key";
                Uri uri= Uri.parse(BASE_URL).buildUpon()
                     .appendQueryParameter(APPID_PARAM, "api key here").build();
                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.d("WE GOT Movie List", movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error in fetching info", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(movieJsonStr);
            }catch (JSONException je){
                Log.e(LOG_TAG, je.getMessage(), je);
            }
            return null;
        }
        private ArrayList<MoviePoster> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            final String RESULTS = "results";
            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesJsonArray = moviesJson.getJSONArray(RESULTS);
            ArrayList<MoviePoster> newMovies =new ArrayList<>();
            for(int i=0;i<moviesJsonArray.length();i++) {
                JSONObject movieJsonObject=moviesJsonArray.getJSONObject(i);
                String poster_path = movieJsonObject.getString("poster_path");
                String id = movieJsonObject.getString("id");
                String title = movieJsonObject.getString("title");
                newMovies.add(new MoviePoster(title, poster_path, id));
                i++;
            }
            return newMovies;

        }
        protected void onPostExecute(ArrayList<MoviePoster> newMovies) {
            if(newMovies!=null){
                moviePosterAdapter.clear();
                for(MoviePoster newMovie:newMovies) {
                    moviePosterAdapter.add(newMovie);
                    moviePosterAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    protected void updateMoviesUI(){
        FetchMoviesTask fetch_some_movies=new FetchMoviesTask();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort_by = SP.getString("sort","");
        fetch_some_movies.execute(sort_by);

    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesUI();
    }
}


