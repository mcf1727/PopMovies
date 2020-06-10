/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies;

import android.content.Intent;
import android.exemple.com.popmovies.model.AppDatabase;
import android.exemple.com.popmovies.model.Movie;
import android.exemple.com.popmovies.reviewItem.ReviewAdapter;
import android.exemple.com.popmovies.trailerItem.TrailerAdapter;
import android.exemple.com.popmovies.utilities.NetworkUtils;
import android.exemple.com.popmovies.utilities.TheMovieDbJsonUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener{

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final String MOVIE_SHARE_HASHTAG = " #The Movie Database";
    private static final String FULL_VOTE_CORE = "/10";
    private static String originalTitle;
    private static String releaseDate;
    private static String voteAverage;
    private static String overview;
    private static final String CRITERIA_TRAILER = "/videos";
    private static final String CRITERIA_REVIEW = "/reviews";
    private static final String TRAILER_KEY = "key";
    private static final String REVIEW_KEY = "content";
    private static final String IS_NOT_FAVORITE = "MARK AS FAVORITE";
    private static final String IS_FAVORITE = "FAVORITE MOVIE";
    private static final int IS_FAVORITE_COLOR = R.color.favorite;
    private static final int IS_NOT_FAVORITE_COLOR = R.color.colorAccent;
//    /* Define the ID when the movie is not found in the database */
//    private static final int ID_NOT_IN_DB = 0;

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    int mMovieId;
    Movie mMovie;
    LiveData<Movie> movieToFindInDb;
    Button mFavoriteButton;

    private AppDatabase mDb;

    public static final String EXTRA_MOVIE = "android.exemple.com.popmovies.extra.MOVIE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDb = AppDatabase.getInstance(getApplicationContext());

        TextView mOriginalTitle = findViewById(R.id.tv_detail_original_title);
        ImageView mPoster = findViewById(R.id.iv_detail_poster);
        TextView mReleaseDate = findViewById(R.id.tv_detail_release_date);
        TextView mVoteAverage = findViewById(R.id.tv_detail_vote_average);
        TextView mOverview = findViewById(R.id.tv_detail_overview);
        mFavoriteButton = findViewById(R.id.button_favorite);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_MOVIE)) {
                 mMovie = intentThatStartedThisActivity.getParcelableExtra(EXTRA_MOVIE);

                if (mMovie != null) {
                    originalTitle = mMovie.getOriginalTitle();
                    if (originalTitle != null) {
                        mOriginalTitle.setText(originalTitle);
                    }

                    String posterPath = mMovie.getPosterPath();
                    if (posterPath != null) {
                        Picasso.get()
                                .load(NetworkUtils.buildPosterUrlString(mMovie.getPosterPath()))
                                .into(mPoster);
                    }

                    releaseDate = mMovie.getReleaseDate();
                    if (releaseDate != null) {
                        releaseDate = releaseDate.substring(0, releaseDate.indexOf("-"));
                        mReleaseDate.setText(releaseDate);
                    }

                    voteAverage = mMovie.getVoteAverage() + FULL_VOTE_CORE;
                    mVoteAverage.setText(voteAverage);

                    overview = mMovie.getOverview();
                    if (overview != null) {
                        mOverview.setText(overview);
                    }

                    mMovieId = mMovie.getId();
                }
            }
        }

        /**
         * Populate the recycler view of the trailers of a detail movie
         */
        DividerItemDecoration mDivider;
        mDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        mTrailerRecyclerView = findViewById(R.id.recyclerview_trailer);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.addItemDecoration(mDivider);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mReviewRecyclerView = findViewById(R.id.recyclerview_review);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.addItemDecoration(mDivider);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        loadMovieDetailData(String.valueOf(mMovieId));

        setUpMovieDetailViewModel(mMovieId);

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFavoriteButton.getText() == IS_NOT_FAVORITE) {
                    setIsFavoriteButton(mFavoriteButton);

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.movieDao().insertMovie(mMovie);
                            movieToFindInDb = mDb.movieDao().loadMovieById(mMovieId);
                        }
                    });
                } else if (mFavoriteButton.getText() == IS_FAVORITE) {
                    setNotFavoriteButton(mFavoriteButton);

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.movieDao().deleteTask(mMovie);
                        }
                    });
                    movieToFindInDb = null;
                }
            }
        });
    }

    private void setUpMovieDetailViewModel(int mMovieId) {
        DetailViewModelFactory factory = new DetailViewModelFactory(mDb, mMovieId);
        final DetailViewModel viewModel = new ViewModelProvider(this, factory).get(DetailViewModel.class);
        final Observer<Movie> movieObserver = new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                viewModel.getMovieToFindInDb().removeObserver(this);
                Log.d(TAG, "Receiving database update from LiveData in ViewModel");
                setIsOrNotFavoriteButton(movie, mFavoriteButton);
            }
        };
        viewModel.getMovieToFindInDb().observe(this, movieObserver);
    }

    private void setIsOrNotFavoriteButton(Movie movie, Button button) {
        if (movie == null) {
            setNotFavoriteButton(button);
        } else {
            setIsFavoriteButton(button);
        }
    }
    private void setIsFavoriteButton(Button button) {
        button.setText(IS_FAVORITE);
        button.setBackgroundColor(getResources().getColor(IS_FAVORITE_COLOR));
    }
    private void setNotFavoriteButton(Button button) {
        button.setText(IS_NOT_FAVORITE);
        button.setBackgroundColor(getResources().getColor(IS_NOT_FAVORITE_COLOR));
    }

    private void loadMovieDetailData(String movieId) {
        new FetchMovieDetailTask().execute(movieId, CRITERIA_TRAILER, TRAILER_KEY);
        new FetchMovieDetailTask().execute(movieId, CRITERIA_REVIEW, REVIEW_KEY);
    }

    @Override
    public void onListItemClick(String trailerYoutobeLink) {
        Uri trailerUri = NetworkUtils.buildTrailerUri(trailerYoutobeLink);
        Intent intentToStartTrailer = new Intent(Intent.ACTION_VIEW, trailerUri);
        startActivity(intentToStartTrailer);
    }

    public class FetchMovieDetailTask extends AsyncTask<String, Void, String[]> {

        String criteria;

        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String movieId = params[0];
            criteria = params[1];
            String key = params[2];
            URL movieDetailRequestUrl = NetworkUtils.buildUrl(movieId + criteria);

            try {
                String jsonMovieDetailResponse = NetworkUtils.getResponseFromHttpUrl(movieDetailRequestUrl);
                Log.v(TAG, "Get the movie detail trailer/review list" + jsonMovieDetailResponse);

                String[] simpleJsonMovieDetail = TheMovieDbJsonUtils.getSimpleMoviesDetailFromJson(jsonMovieDetailResponse, key);
                return simpleJsonMovieDetail;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] simpleJsonMovieDetail) {
            if (simpleJsonMovieDetail != null) {
                if (criteria == CRITERIA_TRAILER) {
                    mTrailerAdapter.setTrailerKeys(simpleJsonMovieDetail);
                    firstTrailerUrl = NetworkUtils.buildTrailerUri(simpleJsonMovieDetail[0]);
                } else if (criteria == CRITERIA_REVIEW) {
                    mReviewAdapter.setReviewContents(simpleJsonMovieDetail);
                }
            }
        }
    }
    Uri firstTrailerUrl;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The information about the movie to share is the title, release year, average vote, overview and the first trailer’s YouTube URL
        String movieToShare = originalTitle + "\n" + releaseDate + "\n" + voteAverage + "\n" + overview + "\n" + firstTrailerUrl+ "\n" + MOVIE_SHARE_HASHTAG;

        if (item.getItemId() == R.id.action_share) shareText(movieToShare);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function to share Movie information
     *
     * @param movieToShare The information to share about the movie
     */
    private void shareText(String movieToShare) {
        String mimeType = "text/plain";
        String title = "Share detail movie";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(movieToShare)
                .startChooser();
    }
}
