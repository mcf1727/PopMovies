/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies.trailerItem;

import android.content.Context;
import android.exemple.com.popmovies.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapterViewHolder> {

    private final static String DISPLAY_TRAILER_BASE = "Trailer ";
    private String[] mTrailerKeys;

    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(String trailerYoutobeLink);
    }

    public TrailerAdapter(ListItemClickListener clickListener) {
        mOnClickListener = clickListener;
    }


    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.trailer_list_item, viewGroup, false);
        TrailerAdapterViewHolder trailerAdapterViewHolder = new TrailerAdapterViewHolder(view, mTrailerKeys, mOnClickListener);

        return trailerAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        trailerAdapterViewHolder.mTrailerTextView.setText(DISPLAY_TRAILER_BASE + (position + 1));
    }

    @Override
    public int getItemCount() {
        if (mTrailerKeys == null) return 0;
        return mTrailerKeys.length;
    }

    public void setTrailerKeys(String[] trailerKeys) {
        mTrailerKeys = trailerKeys;
        notifyDataSetChanged();
    }

}
