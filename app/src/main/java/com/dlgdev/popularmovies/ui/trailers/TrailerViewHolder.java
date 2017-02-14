package com.dlgdev.popularmovies.ui.trailers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.trailers.Trailer;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailerViewHolder extends RecyclerView.ViewHolder {

	@BindView(R.id.trailerTitle) TextView trailerTitle;
	@BindView(R.id.trailerPreview) ImageView trailerPreview;

	public TrailerViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public void trailer(final Trailer trailer, final OnTrailerClickListener listener) {
		trailerTitle.setText(trailer.name);
		Picasso.with(itemView.getContext()).load(String
				.format("https://img.youtube.com/vi/%s/default.jpg", trailer.key))
				.into(trailerPreview);
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				listener.onTrailerClick(trailer);
			}
		});
	}

	public interface OnTrailerClickListener {
		void onTrailerClick(Trailer trailer);
	}
}
