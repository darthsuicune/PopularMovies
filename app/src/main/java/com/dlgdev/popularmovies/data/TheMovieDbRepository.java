package com.dlgdev.popularmovies.data;

import com.dlgdev.popularmovies.data.movies.MovieList;
import com.dlgdev.popularmovies.data.reviews.Review;
import com.dlgdev.popularmovies.data.trailers.Trailer;

import java.io.IOException;
import java.util.List;

public interface TheMovieDbRepository {
	MovieList fetchMovies(String movieType) throws IOException;

	List<Review> fetchReviews(int movieId) throws IOException;

	List<Trailer> fetchTrailers(int movieId) throws Exception;
}
