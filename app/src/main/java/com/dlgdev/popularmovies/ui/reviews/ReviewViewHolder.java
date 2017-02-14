package com.dlgdev.popularmovies.ui.reviews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.reviews.Review;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReviewViewHolder extends RecyclerView.ViewHolder {

	@BindView(R.id.author) TextView author;
	@BindView(R.id.content) TextView content;

	public ReviewViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public void review(Review review) {
		author.setText(review.author);
		content.setText(review.content);
	}
}
