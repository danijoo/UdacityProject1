package net.headlezz.udacityproject1.tmdbapi;

import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * This class provides all the methods to interact with the tmdb api
 * Methods are all static and the underlying retrofit service will be reused.
 */
public class TMDBApi {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185"; // TODO select size depending on device screen ratios

    /**
     * These are the possible sorting orders
     */
    public static final int SORT_ORDER_MOST_POPULAR = 1;
    public static final int SORT_ORDER_HIGHEST_RATED = 2;

    private static TMDBApi.TMDBApiService apiService = buildApiService();

    /**
     * The initialization steps for the retrofit api
     * @return TMDBApiService service to make api calls
     */
    private static TMDBApiService buildApiService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MovieList.class, new MovieListDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(TMDBApiService.class);
    }

    public static void loadPoster(ImageView imageView, Movie movie) {
        Picasso.with(imageView.getContext()).load(IMAGE_BASE_URL + movie.getPosterPath()).into(imageView);
    }

    public static Call<MovieList> discoverMovies(int sortOrder, String apiKey) {
        String sort;
        switch (sortOrder) {
            case SORT_ORDER_MOST_POPULAR:
                sort = "popularity.desc";
                break;
            default:
                sort = "vote_average.desc";
        }
        return apiService.discoverMovies(sort, apiKey);
    }

    /**
     * Api interface for retrofit
     */
    public interface TMDBApiService {
        @Headers("Accept: application/json")
        @GET("discover/movie")
        Call<MovieList> discoverMovies(@Query("sort_by") String sortOrder, @Query("api_key") String apiKey);
    }

}
