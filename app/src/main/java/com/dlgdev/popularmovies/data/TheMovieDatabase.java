package com.dlgdev.popularmovies.data;

import com.dlgdev.popularmovies.data.movies.MovieList;
import com.dlgdev.popularmovies.data.reviews.Review;
import com.dlgdev.popularmovies.data.reviews.Reviews;
import com.dlgdev.popularmovies.data.trailers.Trailer;
import com.dlgdev.popularmovies.data.trailers.Trailers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.dlgdev.popularmovies.settings.Settings.POPULAR;
import static com.dlgdev.popularmovies.settings.Settings.TOP_RATED;

public class TheMovieDatabase implements TheMovieDbRepository {

	private String apikey;
	private TheMovieDatabaseApi api;

	public TheMovieDatabase(String apikey, TheMovieDatabaseApi api) {
		this.apikey = apikey;
		this.api = api;
	}

	@Override public MovieList fetchMovies(final String movieType) throws IOException {
		Call<MovieList> call;
		switch (movieType) {
			case TOP_RATED:
				call = api.topRatedMovies(apikey);
				break;
			case POPULAR:
				call = api.popularMovies(apikey);
				break;
			default:
				return null;
		}
		return call.execute().body();
	}

	@Override public List<Review> fetchReviews(final int movieId) throws IOException {
		List<Review> result = new ArrayList<>();
		Call<Reviews> call = api.reviewsForMovie(movieId, apikey);
		Reviews list = call.execute().body();
		if(list != null) {
			result = list.results();
		}
		return result;
	}
	
	@Override public List<Trailer> fetchTrailers(final int movieId) throws IOException {
		List<Trailer> result = new ArrayList<>();
		Call<Trailers> call = api.trailersForMovie(movieId, apikey);
		Trailers list = call.execute().body();
		if (list != null) {
			result = list.results();
		}
		return result;
	}
}
