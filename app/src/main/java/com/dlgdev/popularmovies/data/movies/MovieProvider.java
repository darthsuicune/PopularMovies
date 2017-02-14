package com.dlgdev.popularmovies.data.movies;

import android.database.Cursor;

public interface MovieProvider {
	void favMovie(Movie movie);
	Movie loadMovie(Cursor data);
}
