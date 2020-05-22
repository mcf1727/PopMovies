/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies;

import android.content.Intent;
import android.exemple.com.popmovies.model.Movie;
import android.exemple.com.popmovies.utilities.NetworkUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE_SHARE_HASHTAG = " #The Movie Database";
    private static String originalTitle;
    private static String releaseDate;
    private static String voteAverage;
    private static String overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView mOriginalTitle = findViewById(R.id.tv_detail_original_title);
        ImageView mPoster = findViewById(R.id.iv_detail_poster);
        TextView mReleaseDate = findViewById(R.id.tv_detail_release_date);
        TextView mVoteAverage = findViewById(R.id.tv_detail_vote_average);
        TextView mOverview = findViewById(R.id.tv_detail_overview);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                Movie mMovie = intentThatStartedThisActivity.getParcelableExtra("movie");

                originalTitle = mMovie.getOriginalTitle();
                mOriginalTitle.setText(originalTitle);

                Picasso.get()
                        .load(NetworkUtils.buildPosterUrlString(mMovie.getPosterPath()))
                        .into(mPoster);

                releaseDate = mMovie.getReleaseDate();
                releaseDate = releaseDate.substring(0, releaseDate.indexOf("-"));
                mReleaseDate.setText(releaseDate);

                voteAverage = mMovie.getVoteAverage() + "/10";
                mVoteAverage.setText(voteAverage);

                overview = mMovie.getOverview();
                mOverview.setText(overview);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The information about the movie to share is the title, release year, average vote and overview
        String movieToShare = originalTitle + "\n" + releaseDate + "\n" + voteAverage + "\n" + overview;

        if (item.getItemId() == R.id.action_share) shareText(movieToShare);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function to share Movie information
     * @param movieToShare The information to share about the movie
     */
    private void shareText(String movieToShare) {
        String mimeType = "text/plain";
        String title = "Share detail movie";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(movieToShare + "\n" + MOVIE_SHARE_HASHTAG)
                .startChooser();
    }
}
