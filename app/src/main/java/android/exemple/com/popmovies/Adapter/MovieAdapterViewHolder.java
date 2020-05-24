package android.exemple.com.popmovies.Adapter;

import android.exemple.com.popmovies.R;
import android.exemple.com.popmovies.model.Movie;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Cache of the children views for a movie list item.
 */
public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final ImageView mMovieImageView;

    private final Movie[] mMovies;
    private final MovieAdapter.ListItemClickListener mOnClickListener;

    public MovieAdapterViewHolder(View itemView, Movie[] movies, MovieAdapter.ListItemClickListener onClickListener) {
        super(itemView);
        mMovieImageView = itemView.findViewById(R.id.iv_movie);
        itemView.setOnClickListener(this);

        mMovies = movies;
        mOnClickListener = onClickListener;
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