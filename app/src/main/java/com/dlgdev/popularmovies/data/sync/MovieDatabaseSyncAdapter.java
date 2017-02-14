package com.dlgdev.popularmovies.data.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.dlgdev.popularmovies.data.movies.Movie;
import com.dlgdev.popularmovies.data.TheMovieDatabase;
import com.dlgdev.popularmovies.data.movies.MovieList;
import com.dlgdev.popularmovies.data.reviews.Review;
import com.dlgdev.popularmovies.data.trailers.Trailer;
import com.dlgdev.popularmovies.data.db.MoviesContract;
import com.dlgdev.popularmovies.settings.Settings;

import java.io.IOException;
import java.util.List;


public class MovieDatabaseSyncAdapter extends AbstractThreadedSyncAdapter {
	private static final String SYNC_ADAPTER_TAG = "Movies Sync Adapter";
	ContentResolver   resolver;
	TheMovieDatabase  db;
	SharedPreferences prefs;

	public MovieDatabaseSyncAdapter(Context context, boolean autoInitialize, TheMovieDatabase db,
									SharedPreferences prefs, ContentResolver resolver) {
		super(context, autoInitialize);
		this.db = db;
		this.prefs = prefs;
		this.resolver = resolver;
	}

	@Override public void onPerformSync(Account account, Bundle bundle, String s,
										ContentProviderClient contentProviderClient,
										SyncResult syncResult) {
		final String movieType = prefs.getString(Settings.MOVIES_TYPE, Settings.POPULAR);
		if(movieType.equals(Settings.FAVORITES)) {
			return;
		}
		Log.d(SYNC_ADAPTER_TAG, "Requesting films for type " + movieType);
		try {
			downloadMovies(db, movieType);
			downloadTrailersAndReviews(db);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void downloadMovies(TheMovieDatabase db, String movieType) throws IOException {
		MovieList movieList = db.fetchMovies(movieType);
		if(movieList != null) {
			storeMovies(movieList, movieType);
		} else {
			Log.e(SYNC_ADAPTER_TAG, "Error while loading list.");
		}
	}

	private void storeMovies(MovieList movieList, String movieType) {
		for (Movie movie : movieList) {
			long id = findOrCreateMovie(movieAsValues(movie));
			assignTypeToMovie(movieType, id);
		}
	}

	private long findOrCreateMovie(ContentValues movie) {
		long id;
		Uri uri = MoviesContract.Movies.CONTENT_URI;
		String[] projection = {MoviesContract.Movies._ID};
		String selection = MoviesContract.Movies.TITLE + "=?";
		String[] selectionArgs = {movie.getAsString(MoviesContract.Movies.TITLE)};
		Cursor c = resolver.query(uri, projection, selection, selectionArgs, null);
		if (c != null && c.getCount() > 0 && c.moveToFirst()) {
			id = c.getLong(0);
		} else {
			Uri newEntry = resolver.insert(uri, movie);
			id = ContentUris.parseId(newEntry);
		}
		if (c != null) {
			c.close();
		}
		return id;
	}

	private void assignTypeToMovie(String movieType, long id) {
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Types.MOVIE_ID, id);
		values.put(MoviesContract.Types.TYPE_NAME, movieType);
		Uri movieTypes = MoviesContract.Types.CONTENT_URI;
		resolver.insert(movieTypes, values);
	}

	public static ContentValues movieAsValues(Movie movie) {
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Movies.TITLE, movie.title);
		values.put(MoviesContract.Movies.POSTER_PATH, movie.posterPath);
		values.put(MoviesContract.Movies.PLOT_OVERVIEW, movie.plotOverview);
		values.put(MoviesContract.Movies.RELEASE_DATE, movie.releaseDate);
		values.put(MoviesContract.Movies.REMOTE_ID, movie.remoteId);
		values.put(MoviesContract.Movies.VOTE_AVERAGE, movie.voteAverage);
		return values;
	}

	private void downloadTrailersAndReviews(TheMovieDatabase db) throws IOException {
		Uri uri = MoviesContract.Movies.CONTENT_URI;
		String[] projection = {MoviesContract.Movies._ID, MoviesContract.Movies.REMOTE_ID};
		Cursor c = resolver.query(uri, projection, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				downloadTrailersAndMoviesForFilm(db, c.getLong(0), c.getInt(1));
			} while (c.moveToNext());
		}
		if (c != null) {
			c.close();
		}
	}

	private void downloadTrailersAndMoviesForFilm(TheMovieDatabase db, long movieId, int remoteId)
			throws IOException {
		List<Trailer> trailers = db.fetchTrailers(remoteId);
		List<Review> reviews = db.fetchReviews(remoteId);
		storeTrailers(movieId, trailers);
		storeReviews(movieId, reviews);
	}

	private void storeTrailers(long movieId, List<Trailer> trailers) {
		int i = 0;
		ContentValues[] values = new ContentValues[trailers.size()];
		for (Trailer trailer : trailers) {
			values[i++] = trailerAsValues(movieId, trailer);
		}
		resolver.bulkInsert(MoviesContract.Trailers.CONTENT_URI, values);
	}

	private ContentValues trailerAsValues(long movieId, Trailer trailer) {
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Trailers.MOVIE_ID, movieId);
		values.put(MoviesContract.Trailers.KEY, trailer.key);
		values.put(MoviesContract.Trailers.ISO, trailer.iso);
		values.put(MoviesContract.Trailers.NAME, trailer.name);
		values.put(MoviesContract.Trailers.SITE, trailer.site);
		values.put(MoviesContract.Trailers.REMOTE_ID, trailer.remoteId);
		values.put(MoviesContract.Trailers.SIZE, trailer.size);
		values.put(MoviesContract.Trailers.TYPE, trailer.type);
		return values;
	}

	private void storeReviews(long movieId, List<Review> reviews) {
		int i = 0;
		ContentValues[] values = new ContentValues[reviews.size()];
		for (Review review : reviews) {
			values[i++] = reviewAsValues(movieId, review);
		}
		resolver.bulkInsert(MoviesContract.Reviews.CONTENT_URI, values);
	}

	private ContentValues reviewAsValues(long movieId, Review review) {
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Reviews.AUTHOR, review.author);
		values.put(MoviesContract.Reviews.CONTENT, review.content);
		values.put(MoviesContract.Reviews.MOVIE_ID, movieId);
		values.put(MoviesContract.Reviews.REMOTE_ID, review.remoteId);
		values.put(MoviesContract.Reviews.URL, review.url);
		return values;
	}
}
