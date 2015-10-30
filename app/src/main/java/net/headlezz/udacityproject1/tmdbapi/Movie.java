package net.headlezz.udacityproject1.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Data object to represent a single movie
 * Only required data from the api is stored to save memory
 */
public class Movie implements Parcelable {

    // empty constructor for gson/retrofit
    public Movie() {}

    long id;

    String title;

    @SerializedName("poster_path")
    String posterPath;

    String overview;

    @SerializedName("vote_average")
    double avRating;

    @SerializedName("release_date")
    Date releaseDate;

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getAvRating() {
        return avRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public long getId() {
        return id;
    }

    /**** Parcelable stuff *****/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(avRating);
        dest.writeLong(releaseDate.getTime());
        dest.writeLong(id);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel src) {
            return new Movie(src);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel src) {
        title = src.readString();
        posterPath = src.readString();
        overview = src.readString();
        avRating = src.readDouble();
        releaseDate = new Date(src.readLong());
        id = src.readLong();
    }


}
