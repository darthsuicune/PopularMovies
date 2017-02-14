package com.dlgdev.popularmovies.ui.movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.ImagePosterProvider;
import com.dlgdev.popularmovies.data.movies.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesViewHolder extends RecyclerView.ViewHolder {

	private final          ImagePosterProvider provider;
	@BindView(R.id.poster) ImageView           imageView;

	public MoviesViewHolder(View itemView, ImagePosterProvider provider) {
		super(itemView);
		this.provider = provider;
		ButterKnife.bind(this, itemView);
	}

	public void setMovie(final Movie movie, final OnMovieClickListener listener) {
		int width = 500;
		Picasso.with(imageView.getContext())
				.load(provider.posterUrl(movie, width))
				.into(imageView);
		imageView.setContentDescription(movie.title);
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				listener.openMovieDetails(movie);
			}
		});
	}

	public interface OnMovieClickListener {
		void openMovieDetails(Movie movie);
	}
}
