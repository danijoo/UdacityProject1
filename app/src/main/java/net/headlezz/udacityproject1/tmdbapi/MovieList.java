package net.headlezz.udacityproject1.tmdbapi;

import java.util.List;

public class MovieList {

    public MovieList(List<Movie> movieList) {
        this.movies = movieList;
    }

    List<Movie> movies;

    public List<Movie> getList() { return movies; }

}
