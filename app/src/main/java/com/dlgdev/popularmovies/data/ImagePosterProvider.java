package com.dlgdev.popularmovies.data;

import com.dlgdev.popularmovies.data.movies.Movie;

public interface ImagePosterProvider {
	String posterUrl(Movie movie, int width);
}
