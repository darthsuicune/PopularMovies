package com.dlgdev.popularmovies.data.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
	static final String AUTHORITY         = "com.dlgdev.popularmovies.movies";
	static final String PATH_MOVIES       = "movies";
	static final String PATH_GENRES       = "genres";
	static final String PATH_MOVIE_GENRES = "moviegenres";
	static final String PATH_REVIEWS      = "reviews";
	static final String PATH_TRAILERS     = "trailers";
	static final String PATH_TYPES        = "types";

	private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	public static Uri buildMovieUri(long id) {
		return ContentUris.withAppendedId(Movies.CONTENT_URI, id);
	}

	public static Uri buildGenreUri(long id) {
		return ContentUris.withAppendedId(Genres.CONTENT_URI, id);
	}

	public static Uri buildMovieGenreUri(long id) {
		return ContentUris.withAppendedId(MovieGenres.CONTENT_URI, id);
	}

	public static Uri buildReviewUri(long id) {
		return ContentUris.withAppendedId(Reviews.CONTENT_URI, id);
	}

	public static Uri buildTrailerUri(long id) {
		return ContentUris.withAppendedId(Trailers.CONTENT_URI, id);
	}

	public static Uri buildTypeUri(long id) {
		return ContentUris.withAppendedId(Types.CONTENT_URI, id);
	}

	public static Uri buildMovieUriForType(String movieType) {
		return Movies.CONTENT_URI.buildUpon().appendPath(movieType).build();
	}

	public static String[] projectionForMovieDetails() {
		return new String[]{MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies._ID,
		                    MoviesContract.Movies.TITLE,
		                    MoviesContract.Movies.POSTER_PATH,
		                    MoviesContract.Movies.PLOT_OVERVIEW,
		                    MoviesContract.Movies.RELEASE_DATE,
		                    MoviesContract.Movies.REMOTE_ID,
		                    MoviesContract.Movies.VOTE_AVERAGE,
		                    MoviesContract.Movies.FAVORITE};
	}

	public static class Movies implements BaseColumns {
		public static final String CONTENT_DIR_TYPE  =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;
		public static final Uri    CONTENT_URI       =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

		public static final String TABLE_NAME = "movies";

		//Columns
		public static final String TITLE          = "title";
		public static final String POSTER_PATH    = "poster_path";
		public static final String PLOT_OVERVIEW  = "plotOverview";
		public static final String RELEASE_DATE   = "release_date";
		public static final String REMOTE_ID      = "remote_id";
		public static final String VOTE_AVERAGE   = "vote_average";
		public static final String ORIGINAL_TITLE = "original_title";
		public static final String BACKDROP_PATH  = "backdrop_path";
		public static final String POPULARITY     = "popularity";
		public static final String VOTE_COUNT     = "vote_count";
		public static final String VIDEO          = "video";
		public static final String ADULT          = "adult";
		public static final String FAVORITE       = "favorite";
	}

	public static class Genres implements BaseColumns {
		public static final String CONTENT_DIR_TYPE  =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_GENRES;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_GENRES;
		public static final Uri    CONTENT_URI       =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRES).build();

		public static final String TABLE_NAME = "genres";

		//Columns
		public static final String GENRE_ID   = "genre_id";
		public static final String GENRE_NAME = "genre_name";
	}

	public static class MovieGenres implements BaseColumns {
		public static final String CONTENT_DIR_TYPE  =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE_GENRES;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE_GENRES;
		public static final Uri    CONTENT_URI       =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_GENRES).build();

		public static final String TABLE_NAME = "moviegenres";

		//Columns
		public static final String GENRE_ID = "genre_id";
		public static final String MOVIE_ID = "movie_id";
	}

	public static class Reviews implements BaseColumns {
		public static final String CONTENT_DIR_TYPE  =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_REVIEWS;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_REVIEWS;
		public static final Uri    CONTENT_URI       =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

		public static final String TABLE_NAME = "reviews";

		//Columns
		public static final String MOVIE_ID  = "movie_id";
		public static final String AUTHOR    = "author";
		public static final String CONTENT   = "content";
		public static final String URL       = "url";
		public static final String REMOTE_ID = "remote_id";
	}

	public static class Trailers implements BaseColumns {
		public static final String CONTENT_DIR_TYPE  =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_TRAILERS;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_TRAILERS;
		public static final Uri    CONTENT_URI       =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

		public static final String TABLE_NAME = "trailers";

		//Columns
		public static final String MOVIE_ID  = "movie_id";
		public static final String ISO       = "iso_639_1";
		public static final String SITE      = "site";
		public static final String KEY       = "trailer_key";
		public static final String NAME      = "name";
		public static final String SIZE      = "size";
		public static final String TYPE      = "type";
		public static final String REMOTE_ID = "remote_id";
	}

	public static class Types implements BaseColumns {
		public static final String CONTENT_DIR_TYPE  =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_TYPES;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_TYPES;
		public static final Uri    CONTENT_URI       =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_TYPES).build();

		public static final String TABLE_NAME = "types";

		//Columns
		public static final String MOVIE_ID  = "movie_id";
		public static final String TYPE_NAME = "type_name";
	}
}
