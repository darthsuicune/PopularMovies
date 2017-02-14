package com.dlgdev.popularmovies.data.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import java.util.Random;

public class MovieUtils {

	public static Uri insertMovies(ContentResolver resolver, int count)
			throws InterruptedException {
		Uri uri = MoviesContract.Movies.CONTENT_URI;
		return insert(resolver, uri, count);
	}

	private static Uri insert(ContentResolver resolver, Uri uri, int count)
			throws InterruptedException {
		Uri lastResult = null;
		for (int i = 0; i < count; i++) {
			lastResult = resolver.insert(uri, makeUpAMovie());
			long id = ContentUris.parseId(lastResult);
			if(id == -1) {
				throw new RuntimeException("Error while inserting");
			}
			insertATrailer(resolver, id);
			insertAReview(resolver, id);
		}
		return lastResult;
	}

	public static ContentValues makeUpAMovie() throws InterruptedException {
		String randomStuff = "a";
		Random random = new Random();
		for (int i = 1, count = random.nextInt(5); i < count; i++) {
			randomStuff += random.nextInt();
		}
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Movies.POSTER_PATH, randomStuff);
		values.put(MoviesContract.Movies.TITLE, randomStuff);
		values.put(MoviesContract.Movies.PLOT_OVERVIEW, randomStuff);
		values.put(MoviesContract.Movies.RELEASE_DATE, randomStuff);
		values.put(MoviesContract.Movies.VOTE_AVERAGE, randomStuff);
		values.put(MoviesContract.Movies.REMOTE_ID, random.nextInt());
		return values;
	}

	private static void insertAReview(ContentResolver resolver, long movieId) {
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Reviews.CONTENT, "asdf");
		values.put(MoviesContract.Reviews.MOVIE_ID, movieId);
		values.put(MoviesContract.Reviews.AUTHOR, "me");
		values.put(MoviesContract.Reviews.URL, "none");
		values.put(MoviesContract.Reviews.REMOTE_ID, "none");
		resolver.insert(MoviesContract.Reviews.CONTENT_URI, values);
	}

	private static void insertATrailer(ContentResolver resolver, long movieId) {
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Trailers.KEY, "asdf");
		values.put(MoviesContract.Trailers.MOVIE_ID, movieId);
		values.put(MoviesContract.Trailers.SIZE, 5);
		values.put(MoviesContract.Trailers.TYPE, "x");
		values.put(MoviesContract.Trailers.REMOTE_ID, "asdf");
		values.put(MoviesContract.Trailers.SITE, "youtube");
		values.put(MoviesContract.Trailers.NAME, "a");
		values.put(MoviesContract.Trailers.ISO, "en");
		resolver.insert(MoviesContract.Trailers.CONTENT_URI, values);
	}
}
