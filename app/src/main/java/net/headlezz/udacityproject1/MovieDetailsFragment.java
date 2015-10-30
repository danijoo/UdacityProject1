package net.headlezz.udacityproject1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.headlezz.udacityproject1.tmdbapi.Movie;
import net.headlezz.udacityproject1.tmdbapi.TMDBApi;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to show the details of a movie
 */
public class MovieDetailsFragment extends Fragment {

    public static final String TAG = MovieDetailsFragment.class.getSimpleName();
    public static final String BUNDLE_ARG_MOVIE = "movie";

    @Bind(R.id.movie_details_ivPoster) ImageView ivPoster;
    @Bind(R.id.movie_details_tvTitle) TextView tvTitle;
    @Bind(R.id.movie_details_tvOverview) TextView tvOverview;
    @Bind(R.id.movie_details_tvReleaseDate) TextView tvReleaseDate;
    @Bind(R.id.movie_details_tvRating) TextView tvRating;

    Movie mMovie;

    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment frag = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_ARG_MOVIE, movie);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().containsKey(BUNDLE_ARG_MOVIE)) {
            mMovie = getArguments().getParcelable(BUNDLE_ARG_MOVIE);
            Log.d(TAG, "Bundled movie: " + mMovie.getTitle());
        } else
            throw new RuntimeException(TAG + " opened without bundled movie!");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TMDBApi.loadPoster(ivPoster, mMovie);
        tvTitle.setText(mMovie.getTitle());
        tvOverview.setText(mMovie.getOverview());
        String formatedDate = DateUtils.formatDateTime(tvReleaseDate.getContext(), mMovie.getReleaseDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
        tvReleaseDate.setText(getString(R.string.movie_release_date, formatedDate));
        tvRating.setText(getString(R.string.movie_rating, mMovie.getAvRating()));
    }
}
