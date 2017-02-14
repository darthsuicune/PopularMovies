package com.dlgdev.popularmovies.data.movies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.dlgdev.popularmovies.data.ImagePosterProvider;
import com.dlgdev.popularmovies.data.db.MoviesContract;

public class MovieProviderImpl implements MovieProvider,ImagePosterProvider {
	String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w";

	ContentResolver resolver;

	public MovieProviderImpl(ContentResolver resolver) {
		this.resolver = resolver;
	}

	public void favMovie(Movie movie) {
		movie.favorite = !movie.favorite;
		Uri uri = MoviesContract.buildMovieUri(movie.rowId);
		String where = MoviesContract.Movies._ID + "=?";
		String[] whereArgs = {Long.toString(movie.rowId)};
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Movies.FAVORITE, movie.favorite);
		resolver.update(uri, values, where, whereArgs);
	}

	@Override public Movie loadMovie(Cursor c) {
		Movie movie = new Movie();
		movie.rowId = c.getLong(c.getColumnIndex(MoviesContract.Movies._ID));
		movie.title = c.getString(c.getColumnIndex(MoviesContract.Movies.TITLE));
		movie.posterPath = c.getString(c.getColumnIndex(MoviesContract.Movies.POSTER_PATH));
		movie.plotOverview = c.getString(c.getColumnIndex(MoviesContract.Movies.PLOT_OVERVIEW));
		movie.releaseDate = c.getString(c.getColumnIndex(MoviesContract.Movies.RELEASE_DATE));
		movie.remoteId = c.getInt(c.getColumnIndex(MoviesContract.Movies.REMOTE_ID));
		movie.voteAverage = c.getDouble(c.getColumnIndex(MoviesContract.Movies.VOTE_AVERAGE));
		movie.favorite = c.getInt(c.getColumnIndex(MoviesContract.Movies.FAVORITE)) == 1;
		return movie;
	}

	@Override public String posterUrl(Movie movie, int width) {
		return BASE_IMAGE_URL + width + movie.poster();
	}
}
