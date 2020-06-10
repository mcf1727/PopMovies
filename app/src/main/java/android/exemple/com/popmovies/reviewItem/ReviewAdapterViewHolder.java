/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies.reviewItem;

import android.exemple.com.popmovies.R;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

    public final TextView mReviewTextView;

    public ReviewAdapterViewHolder(View itemView) {
        super(itemView);

        mReviewTextView = itemView.findViewById(R.id.tv_review);
    }
}
