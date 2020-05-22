/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies;

import android.content.Context;
import android.exemple.com.popmovies.model.Movie;
import android.exemple.com.popmovies.utilities.NetworkUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private String[] mImageUris;
    private Movie[] mMovies;


    /**
     * An on-click listener that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final ListItemClickListener mOnClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(Movie clickedMovieData);
    }

    /**
     * Create a MovieAdapter.
     * @param clickListener The on-click listener for this adapter. This single listener is called
     *                      when an item is clicked.
     */
    public MovieAdapter(ListItemClickListener clickListener) {
        mOnClickListener = clickListener;
    }

    /**
     *This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType To be used when the RecyclerView has more than one type of item
     * @return A new MovieAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdForListItem = R.layout.movie_list_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new MovieAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                               contents of the item at the given position in the data set.
     * @param position               The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String imageUri = mImageUris[position];

        Picasso.get()
                .load(imageUri)
                .into(movieAdapterViewHolder.mMovieImageView);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movie list
     */
    @Override
    public int getItemCount() {
        if (mImageUris == null) return 0;
        return mImageUris.length;
    }

    /**
     * This method is used to set the movie list on a MovieAdapter if we've already created one.
     * This is handy when we get new data from the web but don't want to create a new
     * ForecastAdapter to display it.
     *
     * @param movies list of movies data gotten from the Http request
     */
    public void setMovieData(Movie[] movies) {
        String[] posterPath = new String[movies.length];
        mImageUris = new String[movies.length];
        //String[] imageUris = new String[movies.length];
        for (int i = 0; i < movies.length; i++) {
            posterPath[i] = movies[i].getPosterPath();
            mImageUris[i] = NetworkUtils.buildPosterUrlString(posterPath[i]);
        }
        mMovies = movies;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a movie list item.
     */
    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie clickedMovieData = mMovies[adapterPosition];
            mOnClickListener.onListItemClick(clickedMovieData);
        }
    }
}
