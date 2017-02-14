package com.dlgdev.popularmovies;

import android.content.Context;
import android.content.Intent;

import com.dlgdev.popularmovies.data.movies.Movie;
import com.dlgdev.popularmovies.settings.SettingsActivity;
import com.dlgdev.popularmovies.ui.details.MovieDetailsActivity;

import static com.dlgdev.popularmovies.ui.details.MovieDetailsFragment.KEY_MOVIE;

public class AppNavigation {
	Context context;

	public AppNavigation(Context source) {
		this.context = source;
	}

	public void openMovieDetails(Movie movie) {
		Intent intent = new Intent(context, MovieDetailsActivity.class);
		intent.putExtra(KEY_MOVIE, movie.rowId);
		context.startActivity(intent);
	}

	public void openSettings() {
		Intent intent = new Intent(context, SettingsActivity.class);
		context.startActivity(intent);
	}
}
