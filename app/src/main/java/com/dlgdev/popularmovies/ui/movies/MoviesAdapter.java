package com.dlgdev.popularmovies.ui.movies;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.ImagePosterProvider;
import com.dlgdev.popularmovies.data.movies.MovieProvider;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesViewHolder> {
	public  Cursor                                movies;
	private MovieProvider                         db;
	private MoviesViewHolder.OnMovieClickListener listener;
	private ImagePosterProvider                   posterProvider;

	public MoviesAdapter(MovieProvider db, ImagePosterProvider posterProvider,
	                     MoviesViewHolder.OnMovieClickListener listener) {
		this.db = db;
		this.listener = listener;
		this.posterProvider = posterProvider;
	}

	@Override public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MoviesViewHolder(LayoutInflater.from(parent.getContext())
		                                          .inflate(R.layout.movie_view_holder, parent,
		                                                   false), posterProvider);

	}

	@Override public void onBindViewHolder(MoviesViewHolder holder, int position) {
		if (movies != null && movies.moveToPosition(position)) {
			holder.setMovie(db.loadMovie(movies), listener);
		}
	}

	@Override public int getItemCount() {
		return (movies != null) ? movies.getCount() : 0;
	}

	public void showMovies(Cursor movies) {
		this.movies = movies;
		notifyDataSetChanged();
	}
}
