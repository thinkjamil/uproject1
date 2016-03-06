package com.example.jam.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailedActivityFragment extends Fragment {
    View frag_view;
    public DetailedActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent =getActivity().getIntent();
        frag_view = inflater.inflate(R.layout.fragment_detailed, container, false);
        FetchMovieTask fetch_movie=new FetchMovieTask();
        fetch_movie.execute(intent.getStringExtra("id"));
        return frag_view;
    }
    class FetchMovieTask extends AsyncTask<String,Void,MoviePoster> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        @Override
        protected MoviePoster doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/"+params[0];
                final String APPID_PARAM = "api_key";
                Uri uri=null;
                uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "d87e98c38b95468aed61edb5584a2402").build();
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
                Log.d("WE GOT Movie Info", movieJsonStr);
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
        private MoviePoster getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            final String RESULTS = "results";
            JSONObject movieJson = new JSONObject(movieJsonStr);
            String poster_path = movieJson.getString("poster_path");
            String id = movieJson.getString("id");
            String title = movieJson.getString("title");
            String overview = movieJson.getString("overview");
            String releaseDate = movieJson.getString("release_date");
            String rating =movieJson.getString("vote_average");
            MoviePoster newMovie =new MoviePoster(title, poster_path, id,overview,releaseDate,rating);
            Log.e(LOG_TAG,poster_path);
            return newMovie;

        }
        protected void onPostExecute(MoviePoster newMovie) {
            if(newMovie!=null){
                TextView original_title =(TextView)frag_view.findViewById(R.id.original_title_textView_detail);
                TextView rating =(TextView)frag_view.findViewById(R.id.user_rating_textView_detail);
                TextView overview =(TextView)frag_view.findViewById(R.id.plot_textView_detail);
                TextView releaseDate =(TextView)frag_view.findViewById(R.id.releae_date_textView_detail);
                ImageView poster =(ImageView)frag_view.findViewById(R.id.poster_imageView_detail);
                original_title.setText(newMovie.movieTitle);
                rating.setText(newMovie.rating);
                releaseDate.setText(newMovie.releasedDate);
                overview.setText(newMovie.overview);
                String posterAddress="http://image.tmdb.org/t/p/w185/"+newMovie.poster;
                Picasso.with(getContext()).load(posterAddress).into(poster);
            }
        }
    }
}
