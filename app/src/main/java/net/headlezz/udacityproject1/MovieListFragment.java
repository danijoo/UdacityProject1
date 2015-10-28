package net.headlezz.udacityproject1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.headlezz.udacityproject1.tmdbapi.Movie;
import net.headlezz.udacityproject1.tmdbapi.MovieList;
import net.headlezz.udacityproject1.tmdbapi.MovieListCallback;
import net.headlezz.udacityproject1.tmdbapi.TMDBApi;

import java.util.List;

import retrofit.Call;

/**
 * A fragment showing a list of movies
 */
public class MovieListFragment extends Fragment {

    // TODO fix restore

    public static final String TAG = MovieListFragment.class.getSimpleName();

    /**
     * The number of columns our movie grid shows
     */
    private static final int NUM_COLUMNS = 2;

    /**
     * The currently selected sorting order of the list
     * @see TMDBApi
     */
    private int mSortingOrder = TMDBApi.SORT_ORDER_MOST_POPULAR;

    RecyclerView mMovieGridView;

    /**
     * Holding a reference to running api calls. This makes it possible to cancel them if
     * the user leaves the app, resorts the list or opens the detail view.
     */
    Call<MovieList> mMovieListCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        mMovieGridView = (RecyclerView) view.findViewById(R.id.movie_list_grid);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieGridView.setLayoutManager(new GridLayoutManager(getActivity(), NUM_COLUMNS));
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMovieList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                mSortingOrder = TMDBApi.SORT_ORDER_MOST_POPULAR;
                loadMovieList();
                return true;
            case R.id.action_sort_rating:
                mSortingOrder = TMDBApi.SORT_ORDER_HIGHEST_RATED;
                loadMovieList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Loads a list of movies into the movie grid.
     * mSortingOrder will be used to determine the sorting
     */
    private void loadMovieList() {
        stopLoadingMovies(); // stop any running query before starting a new one
        MovieListCallback cb = new MovieListCallback() {
            @Override
            protected void onResponse(MovieList movies) {
                setListToGrid(movies.getList());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, t.getMessage());
                Toast.makeText(getActivity(), "Somethig went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        mMovieListCall = TMDBApi.discoverMovies(mSortingOrder, getString(R.string.api_key));
        mMovieListCall.enqueue(cb);
    }

    private void setListToGrid(List<Movie> movies) {
        mMovieGridView.setAdapter(new MovieListAdapter(getActivity(), movies));
    }

    /**
     * Stops the current api query
     */
    private void stopLoadingMovies() {
        if(mMovieListCall != null) {
            mMovieListCall.cancel();
            mMovieListCall = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLoadingMovies();
    }
}
