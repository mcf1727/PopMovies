/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies.utilities;

import android.exemple.com.popmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Utility functions to handle The Movie Db JSON data.
 */
public final class TheMovieDbJsonUtils {


    /**
     * This method parses JSON from a web response and returns an array of Object
     * describing the movies
     * @param movieJsonStr JSON response from the server
     * @return Array of Object Movie
     * @throws JSONException If JSON data can't be properly parsed
     */
    public static Movie[] getSimpleMovieMoviesFromJson(String movieJsonStr) throws JSONException {

        final String STATUS_CODE = "status_code";
        final String RESULTS = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String ID = "id";


        /* MOVIE array to hold each movie's data */
        Movie[] parsedMovieData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(STATUS_CODE)) {
            int errorCode = movieJson.getInt(STATUS_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    break;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(RESULTS);
        parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieData = movieArray.getJSONObject(i);
            String originalTitle = movieData.getString(ORIGINAL_TITLE);
            String posterPath = movieData.getString(POSTER_PATH);
            String overView = movieData.getString(OVERVIEW);
            int voteAverage = movieData.getInt(VOTE_AVERAGE);
            String releaseDate = movieData.getString(RELEASE_DATE);
            int id = movieData.getInt(ID);

            parsedMovieData[i] = new Movie();
            parsedMovieData[i].setOriginalTitle(originalTitle);
            parsedMovieData[i].setPosterPath(posterPath);
            parsedMovieData[i].setOverview(overView);
            parsedMovieData[i].setVoteAverage(voteAverage);
            parsedMovieData[i].setReleaseDate(releaseDate);
            parsedMovieData[i].setId(id);
        }

        return parsedMovieData;
    }
}
