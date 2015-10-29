package net.headlezz.udacityproject1.tmdbapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Gson deserializer for movie lists
 */
public class MovieListDeserializer implements JsonDeserializer<MovieList> {

    @Override
    public MovieList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD").create();
        List<Movie> movieList = new ArrayList<>();
        for(JsonElement elem : json.getAsJsonObject().getAsJsonArray("results")) {
            movieList.add(gson.fromJson(elem, Movie.class));
        }
        return new MovieList(movieList);
    }
}
