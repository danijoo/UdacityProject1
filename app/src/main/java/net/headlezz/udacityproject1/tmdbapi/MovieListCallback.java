package net.headlezz.udacityproject1.tmdbapi;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public abstract class MovieListCallback implements Callback<MovieList> {

    @Override
     public void onResponse(Response<MovieList> response, Retrofit retrofit) {
        if(response.isSuccess())
            onResponse(response.body());
        else // we dont want want res
            onFailure(new MovieDownloadException("Something went wrong" + response.code()));
    }

    protected abstract void onResponse(MovieList movies);

    public static class MovieDownloadException extends Exception {
        public MovieDownloadException(String detailMessage) {
            super(detailMessage);
        }
    }
}
