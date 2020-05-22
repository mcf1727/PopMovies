/*
 * Copyright (C) 2020 The Android Open Source Project
 */
package android.exemple.com.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String originalTitle;
    private String posterPath;
    private String overview;
    private int voteAverage;
    private String releaseDate;
    private int id;

    public Movie() {}

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview;}

    public int getVoteAverage() { return voteAverage; }
    public void setVoteAverage(int voteAverage) { this.voteAverage = voteAverage; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeInt(voteAverage);
        parcel.writeString(releaseDate);
        parcel.writeInt(id);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            Movie movie = new Movie();
            movie.originalTitle = parcel.readString();
            movie.posterPath = parcel.readString();
            movie.overview = parcel.readString();
            movie.voteAverage = parcel.readInt();
            movie.releaseDate = parcel.readString();
            movie.id = parcel.readInt();

            return movie;
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[0];
        }
    };
}
