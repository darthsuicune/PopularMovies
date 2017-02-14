package com.dlgdev.popularmovies.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dlgdev.popularmovies.data.db.MoviesContract.Genres;
import com.dlgdev.popularmovies.data.db.MoviesContract.MovieGenres;
import com.dlgdev.popularmovies.data.db.MoviesContract.Movies;
import com.dlgdev.popularmovies.data.db.MoviesContract.Reviews;
import com.dlgdev.popularmovies.data.db.MoviesContract.Trailers;
import com.dlgdev.popularmovies.data.db.MoviesContract.Types;


public class MovieDbOpenHelper extends SQLiteOpenHelper {
	static final String DB_NAME = "movies.db";
	private static final int DB_VERSION = 3;

	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
	private static final String BOOLEAN_KEY = " BOOLEAN";
	private static final String INTEGER_KEY = " INTEGER";
	private static final String DOUBLE_KEY = " DOUBLE";
	private static final String TEXT_KEY = " TEXT";
	private static final String UNIQUE = " UNIQUE";
	private static final String NOT_NULL = " NOT NULL";
	private static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT";

	MovieDbOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override public void onCreate(SQLiteDatabase db) {
		createGenresTable(db);
		createMoviesTable(db);
		createMovieGenresTable(db);
		createReviewsTable(db);
		createTrailersTable(db);
		createTypesTable(db);
	}

	private void createGenresTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + Genres.TABLE_NAME + " (" +
				   Genres._ID + INTEGER_KEY + PRIMARY_KEY + "," +
				   Genres.GENRE_ID + INTEGER_KEY + NOT_NULL + UNIQUE + "," +
				   Genres.GENRE_NAME + TEXT_KEY + NOT_NULL +
				   ")");
	}

	private void createMoviesTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + Movies.TABLE_NAME + " (" +
				   Movies._ID + INTEGER_KEY + PRIMARY_KEY + "," +
				   Movies.TITLE + TEXT_KEY + NOT_NULL + "," +
				   Movies.POSTER_PATH + TEXT_KEY + NOT_NULL + "," +
				   Movies.ADULT + BOOLEAN_KEY + "," +
				   Movies.PLOT_OVERVIEW + TEXT_KEY + NOT_NULL + "," +
				   Movies.RELEASE_DATE + TEXT_KEY + NOT_NULL + "," +
				   Movies.REMOTE_ID + INTEGER_KEY + UNIQUE + "," +
				   Movies.ORIGINAL_TITLE + TEXT_KEY + "," +
				   Movies.BACKDROP_PATH + TEXT_KEY + "," +
				   Movies.POPULARITY + DOUBLE_KEY + "," +
				   Movies.VOTE_COUNT + INTEGER_KEY + "," +
				   Movies.VIDEO + BOOLEAN_KEY + "," +
				   Movies.VOTE_AVERAGE + DOUBLE_KEY + NOT_NULL + "," +
				   Movies.FAVORITE + BOOLEAN_KEY +
				   ")");
	}

	private void createMovieGenresTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + MovieGenres.TABLE_NAME + " (" +
				   MovieGenres._ID + INTEGER_KEY + PRIMARY_KEY + "," +
				   MovieGenres.GENRE_ID + INTEGER_KEY + NOT_NULL + "," +
				   MovieGenres.MOVIE_ID + INTEGER_KEY + NOT_NULL + "," +
				   foreignKey(MovieGenres.GENRE_ID, Genres.TABLE_NAME, Genres.GENRE_ID) + "," +
				   foreignKey(MovieGenres.MOVIE_ID, Movies.TABLE_NAME, Movies._ID) +
				   ")");
		createUniqueIndex(db, MovieGenres.TABLE_NAME, MovieGenres.GENRE_ID, MovieGenres.MOVIE_ID);
	}

	private void createUniqueIndex(SQLiteDatabase db, String tableName, String... fields) {
		String name = "index" + tableName;
		StringBuilder builder = new StringBuilder("CREATE UNIQUE INDEX ");
		builder.append(name);
		builder.append(" ON ");
		builder.append(tableName);
		builder.append("(");
		for (String field : fields) {
			builder.append(field);
			builder.append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		db.execSQL(builder.toString());
	}

	private String foreignKey(String childId, String parentTableName, String parentId) {
		return String
				.format("FOREIGN KEY (%s) REFERENCES %s(%s)", childId, parentTableName, parentId);
	}

	private void createReviewsTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + Reviews.TABLE_NAME + " (" +
				   Reviews._ID + INTEGER_KEY + PRIMARY_KEY + "," +
				   Reviews.MOVIE_ID + INTEGER_KEY + NOT_NULL + "," +
				   Reviews.AUTHOR + TEXT_KEY + NOT_NULL + "," +
				   Reviews.CONTENT + TEXT_KEY + NOT_NULL + "," +
				   Reviews.URL + TEXT_KEY + "," +
				   Reviews.REMOTE_ID + TEXT_KEY + UNIQUE + "," +
				   foreignKey(Reviews.MOVIE_ID, Movies.TABLE_NAME, Movies._ID) +
				   ")");
	}

	private void createTrailersTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + Trailers.TABLE_NAME + " (" +
				   Trailers._ID + INTEGER_KEY + PRIMARY_KEY + "," +
				   Trailers.MOVIE_ID + INTEGER_KEY + NOT_NULL + "," +
				   Trailers.ISO + TEXT_KEY + NOT_NULL + "," +
				   Trailers.SITE + TEXT_KEY + NOT_NULL + "," +
				   Trailers.KEY + TEXT_KEY + NOT_NULL + "," +
				   Trailers.NAME + TEXT_KEY + "," +
				   Trailers.SIZE + INTEGER_KEY + "," +
				   Trailers.TYPE + TEXT_KEY + "," +
				   Trailers.REMOTE_ID + TEXT_KEY + UNIQUE + "," +
				   foreignKey(Trailers.MOVIE_ID, Movies.TABLE_NAME, Movies._ID) +
				   ")");
	}

	private void createTypesTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + Types.TABLE_NAME + " (" +
				   Types._ID + INTEGER_KEY + PRIMARY_KEY + "," +
				   Types.MOVIE_ID + INTEGER_KEY + NOT_NULL + "," +
				   Types.TYPE_NAME + TEXT_KEY + NOT_NULL + "," +
				   foreignKey(Types.MOVIE_ID, Movies.TABLE_NAME, Movies._ID) +
				   ")");
		createUniqueIndex(db, Types.TABLE_NAME, Types.MOVIE_ID, Types.TYPE_NAME);
	}

	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
			case 1:
				db.execSQL("ALTER TABLE " + Movies.TABLE_NAME + " ADD COLUMN " + Movies.FAVORITE +
						   BOOLEAN_KEY);
				createReviewsTable(db);
				createTrailersTable(db);
				createTypesTable(db);
			case 2:
				createUniqueIndex(db, MovieGenres.TABLE_NAME, MovieGenres.GENRE_ID,
						MovieGenres.MOVIE_ID);
				createUniqueIndex(db, Types.TABLE_NAME, Types.MOVIE_ID, Types.TYPE_NAME);
			default:

		}
	}
}
