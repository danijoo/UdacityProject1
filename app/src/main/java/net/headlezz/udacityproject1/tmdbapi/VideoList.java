package net.headlezz.udacityproject1.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * A list of trailers for a movie
 */
public class VideoList implements Parcelable {

    private long id;
    private List<Video> videos;

    public long getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public VideoList(long id, List<Video> videos) {
        this.id = id;
        this.videos = videos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeList(videos);
    }

    public static final Parcelable.Creator<VideoList> CREATOR = new Parcelable.Creator<VideoList>() {
        public VideoList createFromParcel(Parcel src) {
            return new VideoList(src);
        }

        public VideoList[] newArray(int size) {
            return new VideoList[size];
        }
    };

    private VideoList(Parcel src) {
        id = src.readLong();
        videos = src.readArrayList(Video.class.getClassLoader());
    }

    
}
