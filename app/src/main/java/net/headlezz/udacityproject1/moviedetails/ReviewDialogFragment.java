package net.headlezz.udacityproject1.moviedetails;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import net.headlezz.udacityproject1.R;
import net.headlezz.udacityproject1.tmdbapi.Review;
import net.headlezz.udacityproject1.tmdbapi.ReviewList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewDialogFragment extends DialogFragment {

    public static final String TAG = ReviewDialogFragment.class.getSimpleName();
    public static final String BUNDLE_ARG_REVIEWS = "reviews";

    ReviewList mReviews;
    int mCurrentPosition;

    @Bind(R.id.review_tvReview)
    TextView tvReview;

    @Bind(R.id.review_tvReviewUser)
    TextView tvReviewUser;

    @Bind(R.id.review_btNext)
    ImageButton btNext;

    @Bind(R.id.review_btPrevious)
    ImageButton btPrev;

    @Bind(R.id.review_tvPosition)
    TextView tvPosition;


    public static ReviewDialogFragment newInstance(ReviewList reviews) {
        ReviewDialogFragment frag = new ReviewDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_ARG_REVIEWS, reviews);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(BUNDLE_ARG_REVIEWS))
            mReviews = getArguments().getParcelable(BUNDLE_ARG_REVIEWS);
        else
            throw new RuntimeException("No arguments found");

        if(savedInstanceState != null)
            mCurrentPosition = savedInstanceState.getInt("position", 0);
        else
            mCurrentPosition = 0;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.movie_details_review_dialog, container, false);
        ButterKnife.bind(this, dialogView);
        updateReview();
        checkButtonState();
        return dialogView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @OnClick(R.id.review_btNext)
    public void onNextClick(View view) {
        mCurrentPosition += 1;
        updateReview();
        checkButtonState();
    }

    @OnClick(R.id.review_btPrevious)
    public void onPreviousClick(View view) {
        mCurrentPosition -= 1;
        updateReview();
        checkButtonState();
    }

    private void updateReview() {
        Review rv = mReviews.getReviews().get(mCurrentPosition);
        tvReview.setText(rv.getContent());
        tvReviewUser.setText(rv.getAuthor());
        tvPosition.setText(String.format("%1$s/%2$s", mCurrentPosition + 1, mReviews.getReviews().size()));
    }

    private void checkButtonState() {
        if(mCurrentPosition < 1)
            btPrev.setVisibility(View.GONE);
        else
            btPrev.setVisibility(View.VISIBLE);

        if(mCurrentPosition >= mReviews.getReviews().size() - 1)
            btNext.setVisibility(View.GONE);
        else
            btNext.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("position", mCurrentPosition);
        super.onSaveInstanceState(outState);
    }
}
