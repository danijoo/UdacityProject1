package net.headlezz.udacityproject1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.headlezz.udacityproject1.tmdbapi.ReviewList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewDialogFragment extends DialogFragment {

    public static final String TAG = ReviewDialogFragment.class.getSimpleName();
    public static final String BUNDLE_ARG_REVIEWLIST = "reviewlist";

    ReviewList mReviewList;

    @Bind(R.id.review_tvReview)
    TextView tvReview;

    public static ReviewDialogFragment newInstance(ReviewList reviews) {
        ReviewDialogFragment frag = new ReviewDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_ARG_REVIEWLIST, reviews);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(BUNDLE_ARG_REVIEWLIST))
            mReviewList = getArguments().getParcelable(BUNDLE_ARG_REVIEWLIST);
        else
            throw new RuntimeException("No arguments found");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.movie_details_review_dialog, container, false);
        ButterKnife.bind(this, dialogView);
        tvReview.setText(mReviewList.getReviews().get(0).getContent());
        return dialogView;
    }

}
