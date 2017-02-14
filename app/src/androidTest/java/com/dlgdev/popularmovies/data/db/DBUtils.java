package com.dlgdev.popularmovies.data.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DBUtils {
	static ContentResolver resolver;
	static MovieDbOpenHelper helper;

	public static void clearDatabase(Context context) {
		helper = new MovieDbOpenHelper(context);
		resolver = context.getContentResolver();
		delete(MoviesContract.Movies.CONTENT_URI, MoviesContract.Movies.TABLE_NAME);
		delete(MoviesContract.Genres.CONTENT_URI, MoviesContract.Genres.TABLE_NAME);
		delete(MoviesContract.MovieGenres.CONTENT_URI, MoviesContract.MovieGenres.TABLE_NAME);
		delete(MoviesContract.Reviews.CONTENT_URI, MoviesContract.Reviews.TABLE_NAME);
		delete(MoviesContract.Trailers.CONTENT_URI, MoviesContract.Trailers.TABLE_NAME);

		}

	private static void delete(Uri contentUri, String tableName) {
		resolver.delete(contentUri, null, null);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("DELETE FROM sqlite_sequence WHERE name=\"" +
		           tableName + "\"");
	}
}
