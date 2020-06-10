/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies;

import android.exemple.com.popmovies.model.AppDatabase;
import android.exemple.com.popmovies.model.Movie;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {

    private static final String TAG = DetailViewModel.class.getSimpleName();

    private LiveData<Movie> movieToFindInDb;

    public DetailViewModel(AppDatabase db, int movieId){
        Log.d(TAG, "Actively retrieving the movie by Id from the Database");
        movieToFindInDb = db.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovieToFindInDb() {return movieToFindInDb;}
}
