/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY id")
    LiveData<Movie[]> loadAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Movie movie);

    @Delete
    void deleteTask(Movie movie);

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> loadMovieById(int id);
}
