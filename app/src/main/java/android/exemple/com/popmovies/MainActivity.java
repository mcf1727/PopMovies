/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies;

import android.content.Context;
import android.content.Intent;
import android.exemple.com.popmovies.model.AppDatabase;
import android.exemple.com.popmovies.movieItem.MovieAdapter;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
    private static final String FAVORITE = "favorite";
    private static final String INSTANCE_CRITERIA = "instanceCriteria";
    private static final String DEFAULT_CRITERIA = POPULAR;
    /* The criteria for the sort order */
    private String criteria = DEFAULT_CRITERIA;

    private static MovieAdapter mMovieAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getInstance(getApplicationContext());

        RecyclerView mRecyclerView = findViewById(R.id.recyclerview_main);
        int numberOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_CRITERIA)) {
            criteria = savedInstanceState.getString(INSTANCE_CRITERIA, DEFAULT_CRITERIA);
        }

        if (criteria.equals(POPULAR) || criteria.equals(TOP_RATED)) {
            fetchMovieListOnCriteria(criteria);
        } else if ( criteria.equals(FAVORITE)) {
            setUpFavoriteMainViewModel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(INSTANCE_CRITERIA, criteria);
        super.onSaveInstanceState(outState);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     * @param clickedMovieData The Movie for the movie that was clicked
     */
    @Override
    public void onListItemClick(Movie clickedMovieData) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra(DetailActivity.EXTRA_MOVIE, clickedMovieData);

        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortorder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_most_popular:
                if (isOnline()) {
                    fetchMovieListOnCriteria(POPULAR);
                } else {
                    Toast.makeText(this, NO_INTERNET_TOAST, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.sort_by_top_rated:
                if (isOnline()) {
                    fetchMovieListOnCriteria(TOP_RATED);
                } else {
                    Toast.makeText(this, NO_INTERNET_TOAST, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.favorite_movie_list:
                setUpFavoriteMainViewModel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method will get the the criteria of sort, and then tell some background method to get
     * the movie data in the background.
     *
     * @param criteria The criteria for the sort order
     */
    private void fetchMovieListOnCriteria(String criteria) {
        new FetchMovieTask().execute(criteria);
        this.criteria = criteria;

    }

    public static class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String criteria = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(criteria);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                Log.v(TAG, "Get the movie data list" + jsonMovieResponse);

                return TheMovieDbJsonUtils.getSimpleMoviesFromJson(jsonMovieResponse);
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

    private void setUpFavoriteMainViewModel() {
        MainViewModelFactory factory = new MainViewModelFactory(getApplication());
        final MainViewModel viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
        final Observer<Movie[]> movieObserver = new Observer<Movie[]>() {
            @Override
            public void onChanged(Movie[] movies) {
                Log.d(TAG, "Receiving database update from LiveData in ViewModel");
                mMovieAdapter.setMovieData(movies);
            }
        };
        viewModel.getMovies().observe(this, movieObserver);

        criteria = FAVORITE;
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
}
