/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies.trailerItem;

import android.exemple.com.popmovies.R;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView mTrailerTextView;
    private final String[] mTrailerKeys;
    private final TrailerAdapter.ListItemClickListener mOnClickListener;

    public TrailerAdapterViewHolder(View itemView, String[] trailerKeys, TrailerAdapter.ListItemClickListener onClickListener) {
        super(itemView);
        mTrailerTextView = itemView.findViewById(R.id.tv_trailer_text);
        mOnClickListener = onClickListener;
        mTrailerKeys = trailerKeys;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int adapterPosition = getAdapterPosition();
        String trailerYoutobeLink = mTrailerKeys[adapterPosition];

        mOnClickListener.onListItemClick(trailerYoutobeLink);
    }
}
