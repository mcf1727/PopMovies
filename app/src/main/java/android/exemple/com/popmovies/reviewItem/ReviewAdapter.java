/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies.reviewItem;

import android.content.Context;
import android.exemple.com.popmovies.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapterViewHolder> {

    private String[] mReviewContents;

    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.review_list_item, viewGroup, false);
        ReviewAdapterViewHolder reviewAdapterViewHolder = new ReviewAdapterViewHolder(view);
        return reviewAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        reviewAdapterViewHolder.mReviewTextView.setText(mReviewContents[position]);
    }

    @Override
    public int getItemCount() {
        if (mReviewContents == null) return 0;
        return mReviewContents.length;
    }

    public void setReviewContents(String[] reviewContents) {
        mReviewContents = reviewContents;
        notifyDataSetChanged();
    }
}
