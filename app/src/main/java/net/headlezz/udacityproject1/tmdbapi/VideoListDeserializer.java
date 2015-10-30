package net.headlezz.udacityproject1.tmdbapi;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoListDeserializer implements JsonDeserializer<VideoList> {
    @Override
    public VideoList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        List<Video> videos = new ArrayList<>();
        for(JsonElement elem : json.getAsJsonObject().getAsJsonArray("results")) {
            if(elem.getAsJsonObject().get("site").getAsString().equals("YouTube")) // filter non youtube videos out
                videos.add(gson.fromJson(elem, Video.class));
        }
        long id = json.getAsJsonObject().get("id").getAsLong();
        return new VideoList(id, videos);
    }
}
