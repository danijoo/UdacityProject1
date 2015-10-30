package net.headlezz.udacityproject1.tmdbapi;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReviewListDeserializer implements JsonDeserializer<ReviewList> {
    @Override
    public ReviewList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        List<Review> reviews = new ArrayList<>();
        for(JsonElement elem : json.getAsJsonObject().getAsJsonArray("results")) {
            reviews.add(gson.fromJson(elem, Review.class));
        }
        return new ReviewList(reviews);
    }
}
