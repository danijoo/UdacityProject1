package net.headlezz.udacityproject1.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ReviewList implements Parcelable {

    List<Review> reviews;

    public ReviewList(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(reviews);
    }

    public static final Parcelable.Creator<ReviewList> CREATOR = new Parcelable.Creator<ReviewList>() {
        public ReviewList createFromParcel(Parcel src) {
            return new ReviewList(src);
        }

        public ReviewList[] newArray(int size) {
            return new ReviewList[size];
        }
    };

    private ReviewList(Parcel src) {
        this.reviews = src.readArrayList(Review.class.getClassLoader());
    }
}
