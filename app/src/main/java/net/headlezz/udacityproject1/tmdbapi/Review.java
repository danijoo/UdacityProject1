package net.headlezz.udacityproject1.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    String author;
    String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel src) {
            return new Review(src);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    
    Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
    }
}
