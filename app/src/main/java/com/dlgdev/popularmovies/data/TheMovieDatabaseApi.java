package com.dlgdev.popularmovies.data;

import com.dlgdev.popularmovies.data.movies.MovieList;
import com.dlgdev.popularmovies.data.reviews.Reviews;
import com.dlgdev.popularmovies.data.trailers.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDatabaseApi {
	@GET("/3/movie/now_playing")
	Call<MovieList> currentMovies(@Query("api_key") String apikey);

	@GET("/3/movie/popular")
	Call<MovieList> popularMovies(@Query("api_key") String apikey);

	@GET("/3/movie/top_rated")
	Call<MovieList> topRatedMovies(@Query("api_key") String apikey);

	@GET("/3/movie/{id}/reviews")
	Call<Reviews> reviewsForMovie(@Path("id") long movieId, @Query("api_key") String apikey);

	@GET("/3/movie/{id}/videos")
	Call<Trailers> trailersForMovie(@Path("id") long movieId, @Query("api_key") String apikey);
}
