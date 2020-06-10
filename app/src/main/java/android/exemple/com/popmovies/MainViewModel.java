/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies;

import android.app.Application;
import android.exemple.com.popmovies.model.AppDatabase;
import android.exemple.com.popmovies.model.Movie;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<Movie[]> mMovies;

    public MainViewModel(Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the Database");
        mMovies =  database.movieDao().loadAllMovies();
    }

    public LiveData<Movie[]> getMovies() {return mMovies;}
}
