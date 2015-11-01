package net.headlezz.udacityproject1.favorites;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import net.headlezz.udacityproject1.tmdbapi.Movie;

public class FavoriteProviderHelper {

    private ContentResolver mResolver;

    public FavoriteProviderHelper(ContentResolver resolver) {
        mResolver = resolver;
    }

    public Cursor getAllFavorites() {
        String[] projection = {
                FavoriteColumns._ID,
                FavoriteColumns.TITLE,
                FavoriteColumns.POSTERPATH,
                FavoriteColumns.AVRATING,
                FavoriteColumns.RELEASEDATE,
                FavoriteColumns.OVERVIEW
        };
        return mResolver.query(FavoriteProvider.Favorites.FAVORITES_URI, projection, null, null, null);
    }

    public boolean isFavorite(Movie movie) {
        String[] projection = {FavoriteColumns._ID};
        String selection = FavoriteColumns.TITLE + "=?";
        String[] selectionArgs = new String[]{movie.getTitle()};
        Cursor cursor = mResolver.query(FavoriteProvider.Favorites.FAVORITES_URI, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.getCount() > 0)
            return true;
        return false;
    }

    public void addFavorite(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(FavoriteColumns._ID, movie.getId());
        values.put(FavoriteColumns.TITLE, movie.getTitle());
        values.put(FavoriteColumns.OVERVIEW, movie.getOverview());
        values.put(FavoriteColumns.POSTERPATH, movie.getPosterPath());
        values.put(FavoriteColumns.AVRATING, movie.getAvRating());
        values.put(FavoriteColumns.RELEASEDATE, movie.getReleaseDate().getTime());
        mResolver.insert(FavoriteProvider.Favorites.FAVORITES_URI, values);
    }

    public void removeFavorite(Movie movie) {
        mResolver.delete(FavoriteProvider.Favorites.FAVORITES_URI, FavoriteColumns._ID + "=?", new String[]{String.valueOf(movie.getId())});
    }

}
