package net.headlezz.udacityproject1.tmdbapi;

/**
 * Data object to represent a single movie
 * Only required data from the api is stored to save memory
 */
public class Movie {

    String title;
    String posterPath;
    String overview;
    double avRating;

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
}
