/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies;

import android.content.Context;
import android.content.Intent;
import android.exemple.com.popmovies.model.Movie;
import android.exemple.com.popmovies.utilities.NetworkUtils;
import android.exemple.com.popmovies.utilities.TheMovieDbJsonUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String NO_INTERNET_TOAST = "No Internet connexion";
    /* The choice of sort order by the most popular */
    private static final String POPULAR = "popular";
    /* The choice of sort order by the top rated */
    private static final String TOP_RATED = "top_rated";
    /* The critical for the sort order */
    private String criteria;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isOnline()) {
            RecyclerView mRecyclerView = findViewById(R.id.recyclerview_main);
            int numberOfColumns = 2;
            GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mMovieAdapter = new MovieAdapter(this);
            mRecyclerView.setAdapter(mMovieAdapter);

            loadMovieDate();

        } else {
            Toast.makeText(this, NO_INTERNET_TOAST, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method will get the the criteria of sort, and then tell some background method to get
     * the movie data in the background.
     */
    private void loadMovieDate() {
        criteria = POPULAR;
        new FetchMovieTask().execute(criteria);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     * @param clickedMovieData The Movie for the movie that was clicked
     */
    @Override
    public void onListItemClick(Movie clickedMovieData) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("movie", clickedMovieData);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isOnline()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.sortorder, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort_by_most_popular:
                criteria = POPULAR;
                new FetchMovieTask().execute(criteria);
                return true;
            case R.id.sort_by_top_rated:
                criteria = TOP_RATED;
                new FetchMovieTask().execute(criteria);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Check if the internet connection is available.
     * @return Boolean (true when there is internet connection, others return false)
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String critical = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(critical);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                Log.v(TAG, "Get the movie data list" + jsonMovieResponse);

                return TheMovieDbJsonUtils.getSimpleMovieMoviesFromJson(jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {

            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
            }
        }
    }
}
