package net.headlezz.udacityproject1.tmdbapi;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MovieListDeserializer implements JsonDeserializer<MovieList> {

    @Override
    public MovieList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Movie> movieList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        for(JsonElement elem : json.getAsJsonObject().getAsJsonArray("results")) {
            try { // TODO fix this and remove try/catch
                JsonObject jsonObj = elem.getAsJsonObject();
                Movie m = new Movie();
                m.title = jsonObj.get("title").getAsString();
                m.overview = jsonObj.get("overview").getAsString();
                m.avRating = jsonObj.get("vote_average").getAsDouble();
                m.posterPath  = jsonObj.get("poster_path").getAsString();
                m.releaseDate = sdf.parse(jsonObj.get("release_date").getAsString()).getTime();
                movieList.add(m);
            } catch (Exception e) {
                Log.e(MovieListDeserializer.class.getSimpleName(), e.toString());
            }
        }
        return new MovieList(movieList);
    }
}
