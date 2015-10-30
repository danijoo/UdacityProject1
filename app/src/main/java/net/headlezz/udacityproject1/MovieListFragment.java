package net.headlezz.udacityproject1;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import net.headlezz.udacityproject1.tmdbapi.MovieList;
import net.headlezz.udacityproject1.tmdbapi.TMDBApi;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A fragment showing a list of movies
 */
public class MovieListFragment extends Fragment implements Callback<MovieList> {

    public static final String TAG = MovieListFragment.class.getSimpleName();

    /**
     * The number of columns our movie grid shows
     */
    private static final int NUM_COLUMNS_PORTRAIT = 3;
    private static final int NUM_COLUMNS_LAND = 5;
    /**
     * The currently selected sorting order of the list
     * @see TMDBApi
     */
    private int mSortingOrder = TMDBApi.SORT_ORDER_MOST_POPULAR;

    private MovieList mMovieList;

    @Bind(R.id.movie_list_grid) RecyclerView mMovieGridView;
    @Bind(R.id.movie_list_progressBar) ProgressBar mProgressBar;

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
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieGridView.setLayoutManager(new GridLayoutManager(getActivity(), getNumColumns()));

        if(savedInstanceState != null) {
            mSortingOrder = savedInstanceState.getInt("sort_order");
            if(savedInstanceState.containsKey("movielist"))
                mMovieList = savedInstanceState.getParcelable("movielist");
        }
    }

    /**
     * @return the number of columns the app should show
     * The result depends on screen orientation
     */
    private int getNumColumns() {
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE)
            return NUM_COLUMNS_LAND;
        else
            return NUM_COLUMNS_PORTRAIT;

    }

    @Override
    public void onStart() {
        super.onStart();
        initMovieList();
    }

    /**
     * sets the adapter from the restored movie list if the instance state is restored
     * starts the download of the new movie list if no restored one is present
     */
    private void initMovieList() {
        if(mMovieList == null)
            loadMovieList();
        else
            mMovieGridView.setAdapter(new MovieListAdapter( (MovieNavigation) getActivity(), mMovieList));
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
        mProgressBar.setVisibility(View.VISIBLE);
        mMovieListCall = TMDBApi.discoverMovies(mSortingOrder, getString(R.string.api_key));
        mMovieListCall.enqueue(this);
    }

    @Override
    public void onResponse(Response<MovieList> response, Retrofit retrofit) {
        if(response.isSuccess()) {
            mMovieList = response.body();
            setListToGrid(mMovieList);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, t.getClass().getSimpleName() + " " + t.getMessage());
        mProgressBar.setVisibility(View.GONE);
        if(!t.getMessage().equals("Canceled") && getActivity() != null)
            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void setListToGrid(MovieList movieList) {
        mMovieGridView.setAdapter(new MovieListAdapter((MovieNavigation) getActivity(), movieList));
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(context instanceof MovieNavigation))
            throw new RuntimeException("Activity must implement " + MovieNavigation.class.getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("sort_order", mSortingOrder);
        if(mMovieList != null)
            outState.putParcelable("movielist", mMovieList);
        super.onSaveInstanceState(outState);
    }
}
