package net.headlezz.udacityproject1.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Wrapper class for list of movies
 */
public class MovieList implements Parcelable {

    public MovieList(List<Movie> movieList) {
        this.movies = movieList;
    }

    List<Movie> movies;

    public List<Movie> getList() { return movies; }

    /***** Parcelable implementation ******/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(movies);
    }

    public static final Parcelable.Creator<MovieList> CREATOR = new Parcelable.Creator<MovieList>() {
        public MovieList createFromParcel(Parcel src) {
            return new MovieList(src);
        }

        public MovieList[] newArray(int size) {
            return new MovieList[size];
        }
    };

    private MovieList(Parcel src) {
        movies = src.readArrayList(Movie.class.getClassLoader());
    }
}
