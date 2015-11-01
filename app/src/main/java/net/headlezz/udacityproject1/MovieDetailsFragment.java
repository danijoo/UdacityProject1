package net.headlezz.udacityproject1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.headlezz.udacityproject1.favorites.FavoriteProviderHelper;
import net.headlezz.udacityproject1.tmdbapi.Movie;
import net.headlezz.udacityproject1.tmdbapi.TMDBApi;
import net.headlezz.udacityproject1.tmdbapi.Video;
import net.headlezz.udacityproject1.tmdbapi.VideoList;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Fragment to show the details of a movie
 */
public class MovieDetailsFragment extends Fragment implements Callback<VideoList> {

    public static final String TAG = MovieDetailsFragment.class.getSimpleName();
    public static final String BUNDLE_ARG_MOVIE = "movie";

    @Bind(R.id.movie_details_ivPoster) ImageView ivPoster;
    @Bind(R.id.movie_details_tvTitle) TextView tvTitle;
    @Bind(R.id.movie_details_tvOverview) TextView tvOverview;
    @Bind(R.id.movie_details_tvReleaseDate) TextView tvReleaseDate;
    @Bind(R.id.movie_details_tvRating) TextView tvRating;
    @Bind(R.id.movie_details_trailerHolder) ViewGroup trailerHolder;

    Movie mMovie;
    VideoList mVideoList;

    FavoriteProviderHelper mProvderHelper;
    boolean isFavorite;

    /**
     * Hold a reference to the videolist download call so
     * it can be canceled when stopping
     */
    Call<VideoList> mVideoListCall;

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
        setHasOptionsMenu(true);
        mProvderHelper = new FavoriteProviderHelper(getContext().getContentResolver());
        if(getArguments().containsKey(BUNDLE_ARG_MOVIE)) {
            mMovie = getArguments().getParcelable(BUNDLE_ARG_MOVIE);
            Log.d(TAG, "Bundled movie: " + mMovie.getTitle());
            isFavorite = mProvderHelper.isFavorite(mMovie); // TODO async?
        } else
            throw new RuntimeException(TAG + " opened without bundled movie!");

        if(savedInstanceState != null && savedInstanceState.containsKey("videolist"))
            mVideoList = savedInstanceState.getParcelable("videolist");
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

        if(mVideoList == null)
            loadVideoList();
        else
            showVideoList(mVideoList);
    }

    private void loadVideoList() {
        if(mVideoListCall != null)
            mVideoListCall.cancel();
        mVideoListCall = TMDBApi.getVideosForMovie(mMovie, getString(R.string.api_key));
        mVideoListCall.enqueue(this);
    }

    @Override
    public void onResponse(Response<VideoList> response, Retrofit retrofit) {
        if(response.isSuccess()) {
            mVideoList = response.body();
            if(getActivity() != null)
                showVideoList(mVideoList);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if(getActivity() != null)
            Toast.makeText(getActivity(), "Failed to load videos: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    // TODO high res image for video link

    /**
     * shows a list of links to youtube videos for the selected movie
     * if the videolist has no videos, the user sees a message that there are no.
     * @param videoList VideoList to show
     */
    private void showVideoList(@NonNull VideoList videoList) {
        List<Video> videos = videoList.getVideos();
        if(videos.size() > 0) {
            for (Video video : videoList.getVideos()) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.movie_details_trailer_item, trailerHolder, false);
                TextView tv = (TextView) view.findViewById(R.id.trailer_item_tvName);
                tv.setText(video.getName());
                view.setTag(video);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String videoId = ((Video) view.getTag()).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                        intent.putExtra("VIDEO_ID", videoId);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "Youtube app not found.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                trailerHolder.addView(view);
            }
        } else {
            trailerHolder.getChildAt(0).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_details_menu, menu);
        MenuItem favItem = menu.findItem(R.id.action_favorite);
        favItem.setIcon(isFavorite ? R.drawable.ic_action_star_10 : R.drawable.ic_action_star_0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share) {
            shareTrailer();
            return true;
        } else if(item.getItemId() == R.id.action_favorite) {
            switchFavorite();
            item.setIcon(isFavorite ? R.drawable.ic_action_star_10 : R.drawable.ic_action_star_0);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    /**
     * removes or adds the movie to stored favorites
     */
    private void switchFavorite() { // TODO async?
        if(isFavorite)
            mProvderHelper.removeFavorite(mMovie);
        else
            mProvderHelper.addFavorite(mMovie);
        isFavorite = !isFavorite;
    }

    /**
     * Fires an intent to share the trailer url of this movie
     * Code from http://www.techrepublic.com/blog/software-engineer/get-social-using-android-intents-to-share-a-link/
     */
    private void shareTrailer() {
        if(mVideoList == null || mVideoList.getVideos().size() == 0)
            Toast.makeText(getActivity(), "Nothing to share. :(", Toast.LENGTH_SHORT).show();
        else {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "https://youtu.be/" + mVideoList.getVideos().get(0).getKey());
            i.putExtra(Intent.EXTRA_SUBJECT, "Check out this movie!");
            startActivity(Intent.createChooser(i, "Share"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mVideoList != null)
            outState.putParcelable("videolist", mVideoList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        if(mVideoListCall != null)
            mVideoListCall.cancel();
        super.onStop();
    }
}
