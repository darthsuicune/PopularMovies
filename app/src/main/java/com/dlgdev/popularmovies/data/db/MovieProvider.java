package com.dlgdev.popularmovies.data.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.dlgdev.popularmovies.settings.Settings;

import static com.dlgdev.popularmovies.data.db.MoviesContract.PATH_GENRES;
import static com.dlgdev.popularmovies.data.db.MoviesContract.PATH_MOVIES;
import static com.dlgdev.popularmovies.data.db.MoviesContract.PATH_MOVIE_GENRES;
import static com.dlgdev.popularmovies.data.db.MoviesContract.PATH_REVIEWS;
import static com.dlgdev.popularmovies.data.db.MoviesContract.PATH_TRAILERS;
import static com.dlgdev.popularmovies.data.db.MoviesContract.PATH_TYPES;

public class MovieProvider extends ContentProvider {
	static final int CODE_MOVIES = 101;
	static final int CODE_MOVIE = 102;
	static final int CODE_MOVIE_BY_TYPE = 103;
	static final int CODE_GENRES = 201;
	static final int CODE_GENRE = 202;
	static final int CODE_MOVIE_GENRES = 301;
	static final int CODE_MOVIE_GENRE = 302;
	static final int CODE_REVIEWS = 401;
	static final int CODE_REVIEW = 402;
	static final int CODE_TRAILERS = 501;
	static final int CODE_TRAILER = 502;
	static final int CODE_TYPES = 701;
	static final int CODE_TYPE = 702;

	static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_MOVIES, CODE_MOVIES);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_MOVIES + "/#", CODE_MOVIE);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_MOVIES + "/*", CODE_MOVIE_BY_TYPE);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_GENRES, CODE_GENRES);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_GENRES + "/#", CODE_GENRE);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_MOVIE_GENRES, CODE_MOVIE_GENRES);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_MOVIE_GENRES + "/#", CODE_MOVIE_GENRE);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_REVIEWS, CODE_REVIEWS);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_REVIEWS + "/#", CODE_REVIEW);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_TRAILERS, CODE_TRAILERS);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_TRAILERS + "/#", CODE_TRAILER);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_TYPES, CODE_TYPES);
		uriMatcher.addURI(MoviesContract.AUTHORITY, PATH_TYPES + "/#", CODE_TYPE);
	}

	private MovieDbOpenHelper dbHelper;
	private Context context;
	private ContentResolver resolver;

	public MovieProvider() {
	}

	@Override public boolean onCreate() {
		context = getContext();
		dbHelper = new MovieDbOpenHelper(context);
		resolver = context.getContentResolver();
		return true;
	}

	@Override public String getType(@NonNull Uri uri) {
		switch (uriMatcher.match(uri)) {
			case CODE_MOVIE:
				return MoviesContract.Movies.CONTENT_ITEM_TYPE;
			case CODE_MOVIES:
			case CODE_MOVIE_BY_TYPE:
				return MoviesContract.Movies.CONTENT_DIR_TYPE;
			case CODE_GENRE:
				return MoviesContract.Genres.CONTENT_ITEM_TYPE;
			case CODE_GENRES:
				return MoviesContract.Genres.CONTENT_DIR_TYPE;
			case CODE_MOVIE_GENRE:
				return MoviesContract.MovieGenres.CONTENT_ITEM_TYPE;
			case CODE_MOVIE_GENRES:
				return MoviesContract.MovieGenres.CONTENT_DIR_TYPE;
			case CODE_REVIEW:
				return MoviesContract.Reviews.CONTENT_ITEM_TYPE;
			case CODE_REVIEWS:
				return MoviesContract.Reviews.CONTENT_DIR_TYPE;
			case CODE_TRAILER:
				return MoviesContract.Trailers.CONTENT_ITEM_TYPE;
			case CODE_TRAILERS:
				return MoviesContract.Trailers.CONTENT_DIR_TYPE;
			case CODE_TYPE:
				return MoviesContract.Types.CONTENT_ITEM_TYPE;
			case CODE_TYPES:
				return MoviesContract.Types.CONTENT_DIR_TYPE;
			default:
				throw new UnsupportedOperationException("Unsupported Uri: " + uri.toString());
		}
	}

	@Override public Cursor query(@NonNull Uri uri, String[] projection, String selection,
								  String[] selectionArgs, String sortOrder) {
		Cursor c;
		switch (uriMatcher.match(uri)) {
			case CODE_GENRE:
			case CODE_MOVIE:
			case CODE_MOVIE_GENRE:
			case CODE_REVIEW:
			case CODE_TRAILER:
			case CODE_TYPE:
				c = getItem(uri, projection);
				break;
			case CODE_MOVIES:
			case CODE_GENRES:
			case CODE_MOVIE_GENRES:
			case CODE_REVIEWS:
			case CODE_TRAILERS:
			case CODE_TYPES:
				c = queryDb(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case CODE_MOVIE_BY_TYPE:
				c = loadMovieByType(uri, projection, sortOrder);
				break;
			default:
				throw new UnsupportedOperationException("Unsupported Uri: " + uri.toString());
		}
		c.setNotificationUri(resolver, uri);
		return c;
	}

	private Cursor loadMovieByType(Uri uri, String[] projection, String sortOrder) {
		String movieType = uri.getLastPathSegment();
		String selection;
		String[] selectionArgs = {};
		switch (movieType) {
			case Settings.FAVORITES:
				selection = MoviesContract.Movies.FAVORITE + "=1";
				break;
			case Settings.POPULAR:
			case Settings.TOP_RATED:
				selection = MoviesContract.Types.TYPE_NAME + "=?";
				selectionArgs = new String[]{movieType};
				break;
			default:
				throw new UnsupportedOperationException("Unsupported Movie Type: " + movieType);
		}
		return queryDb(uri, projection, selection, selectionArgs, sortOrder);
	}

	@NonNull private Cursor getItem(Uri uri, String[] projection) {
		String selection = BaseColumns._ID + "=?";
		String[] selectionArgs = {Long.toString(ContentUris.parseId(uri))};
		return queryDb(uri, projection, selection, selectionArgs, null);
	}

	private Cursor queryDb(Uri uri, String[] projection, String selection, String[] selectionArgs,
						   String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.query(getTable(uri), projection, selection, selectionArgs, null, null, sortOrder);
	}

	private String getTable(Uri uri) {
		switch (uriMatcher.match(uri)) {
			case CODE_MOVIE:
			case CODE_MOVIES:
				return MoviesContract.Movies.TABLE_NAME;
			case CODE_GENRE:
			case CODE_GENRES:
				return MoviesContract.Genres.TABLE_NAME;
			case CODE_MOVIE_GENRE:
			case CODE_MOVIE_GENRES:
				return MoviesContract.MovieGenres.TABLE_NAME;
			case CODE_REVIEW:
			case CODE_REVIEWS:
				return MoviesContract.Reviews.TABLE_NAME;
			case CODE_TRAILER:
			case CODE_TRAILERS:
				return MoviesContract.Trailers.TABLE_NAME;
			case CODE_TYPE:
			case CODE_TYPES:
				return MoviesContract.Types.TABLE_NAME;
			case CODE_MOVIE_BY_TYPE:
				return String.format("%s JOIN %s ON %s.%s = %s.%s", MoviesContract.Movies.TABLE_NAME,
						MoviesContract.Types.TABLE_NAME, MoviesContract.Movies.TABLE_NAME,
						MoviesContract.Movies._ID, MoviesContract.Types.TABLE_NAME,
						MoviesContract.Types.MOVIE_ID);
			default:
				throw new UnsupportedOperationException("Unsupported Uri: " + uri.toString());
		}
	}

	@Override public Uri insert(@NonNull Uri uri, ContentValues values) {
		long id = dbHelper.getWritableDatabase().insert(getTable(uri), null, values);
		Uri newEntry = buildUriFor(uri, id);
		resolver.notifyChange(uri, null);
		return newEntry;
	}

	private Uri buildUriFor(Uri uri, long id) {
		switch (uriMatcher.match(uri)) {
			case CODE_MOVIE:
			case CODE_MOVIES:
				return MoviesContract.buildMovieUri(id);
			case CODE_GENRE:
			case CODE_GENRES:
				return MoviesContract.buildGenreUri(id);
			case CODE_MOVIE_GENRE:
			case CODE_MOVIE_GENRES:
				return MoviesContract.buildMovieGenreUri(id);
			case CODE_REVIEW:
			case CODE_REVIEWS:
				return MoviesContract.buildReviewUri(id);
			case CODE_TRAILER:
			case CODE_TRAILERS:
				return MoviesContract.buildTrailerUri(id);
			case CODE_TYPE:
			case CODE_TYPES:
				return MoviesContract.buildTypeUri(id);
			default:
				throw new UnsupportedOperationException("Unsupported Uri: " + uri.toString());
		}
	}

	@Override public int update(@NonNull Uri uri, ContentValues values, String selection,
								String[] selectionArgs) {
		int count = dbHelper.getWritableDatabase()
				.update(getTable(uri), values, selection, selectionArgs);
		if (count > 0) {
			resolver.notifyChange(uri, null);
		}
		return count;
	}

	@Override public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
		int count = dbHelper.getWritableDatabase().delete(getTable(uri), selection, selectionArgs);
		if (count > 0) {
			resolver.notifyChange(uri, null);
		}
		return count;
	}

	@Override public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
		int count = 0;

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (ContentValues value : values) {
				long id = db.insert(getTable(uri), null, value);
				if (id != -1) {
					count++;
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		resolver.notifyChange(uri, null);
		return count;
	}
}
