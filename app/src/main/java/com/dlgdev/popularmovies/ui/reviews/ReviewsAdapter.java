package com.dlgdev.popularmovies.ui.reviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.reviews.Review;

import java.util.List;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
	private List<Review> reviews;

	public ReviewsAdapter(List<Review> reviews) {
		this.reviews = reviews;
	}

	@Override public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		ReviewViewHolder vh = new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(
				R.layout.reviews, parent, false));
		return vh;
	}

	@Override public void onBindViewHolder(ReviewViewHolder holder, int position) {
		holder.review(reviews.get(position));
	}

	@Override public int getItemCount() {
		return reviews.size();
	}
}
