package net.headlezz.udacityproject1.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    private String name;
    private String key;

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(key);
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        public Video createFromParcel(Parcel src) {
            return new Video(src);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    private Video(Parcel src) {
        name = src.readString();
        key = src.readString();
    }

}
