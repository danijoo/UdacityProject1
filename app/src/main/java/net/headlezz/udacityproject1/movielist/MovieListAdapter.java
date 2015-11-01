package net.headlezz.udacityproject1.movielist;

import net.headlezz.udacityproject1.MovieNavigation;
import net.headlezz.udacityproject1.tmdbapi.Movie;
import net.headlezz.udacityproject1.tmdbapi.MovieList;

import java.util.List;

/**
 * MovieListAdapter implementation for showing movies from a list of movies
 */
public class MovieListAdapter extends AbstractMovieListAdapter {

    List<Movie> mMovies;

    public MovieListAdapter(MovieNavigation mn, MovieList movieList) {
        super(mn);
        mMovies = movieList.getList();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    protected Movie getMovieForPosition(int position) {
        return mMovies.get(position);
    }
}
