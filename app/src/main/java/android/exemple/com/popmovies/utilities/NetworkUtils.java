/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies.utilities;

import android.exemple.com.popmovies.BuildConfig;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";

    private static final String KEY_PARAM = "api_key";
    private static final String YOUTUBE_PARAM = "v";

    public static URL buildUrl(String criteria) {
        Uri  builtUri = Uri.parse(MOVIE_BASE_URL + criteria).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Build movies URI by the critical popular/top_rated" + url);
        return url;
    }

    public static String buildPosterUrlString(String posterPath) {
        return POSTER_BASE_URL + POSTER_SIZE + posterPath;
    }

    public static Uri buildTrailerUri(String trailerYoutobeKey) {
        Uri builtUri = Uri.parse(TRAILER_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_PARAM, trailerYoutobeKey)
                .build();

        Log.v(TAG, "Build Youtube trailer link with the key" + builtUri);
        return builtUri;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
