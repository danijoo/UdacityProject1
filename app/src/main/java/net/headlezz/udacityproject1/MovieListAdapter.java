package net.headlezz.udacityproject1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import net.headlezz.udacityproject1.tmdbapi.Movie;
import net.headlezz.udacityproject1.tmdbapi.TMDBApi;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>{

    Context context;
    List<Movie> mMovies;

    public MovieListAdapter(Context context, List<Movie> movies) {
        this.context = context;
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setMovie(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    /**
     * Viewholder for the movie items
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoster;
        Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView;
            ivPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), movie.getTitle() + " clicked.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setMovie(Movie movie) {
            this.movie = movie;
            TMDBApi.loadPoster(ivPoster, movie);
        }
    }

}
