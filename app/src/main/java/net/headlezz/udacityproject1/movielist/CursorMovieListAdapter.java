package net.headlezz.udacityproject1.movielist;


import android.database.Cursor;
import android.database.DataSetObserver;

import net.headlezz.udacityproject1.MovieNavigation;
import net.headlezz.udacityproject1.favorites.FavoriteColumns;
import net.headlezz.udacityproject1.tmdbapi.Movie;

/**
 * MovieListAdapter implementation for showing movies from a cursor
 * Modified version of https://gist.github.com/skyfishjy/443b7448f59be978bc59
 */
public class CursorMovieListAdapter extends AbstractMovieListAdapter {

    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIdColumn;
    private DataSetObserver mDataSetObserver;

    public CursorMovieListAdapter(Cursor cursor, MovieNavigation mn) {
        super(mn);
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(FavoriteColumns._ID) : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    protected Movie getMovieForPosition(int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        return Movie.fromCurrentCursorPosition(mCursor);
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }
}
