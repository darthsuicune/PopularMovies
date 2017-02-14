package com.dlgdev.popularmovies.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class) public class MovieDbOpenHelperTest {
	SQLiteDatabase db;

	@Before public void setUp() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		DBUtils.clearDatabase(context);

		db = new MovieDbOpenHelper(context).getWritableDatabase();
	}

	@Test public void testAllTablesAreCreated() throws Exception {
		Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		assertTrue(c.moveToFirst());

		HashSet<String> tablenames = new HashSet<>(3);
		tablenames.add(MoviesContract.Genres.TABLE_NAME);
		tablenames.add(MoviesContract.Movies.TABLE_NAME);
		tablenames.add(MoviesContract.MovieGenres.TABLE_NAME);
		do {
			tablenames.remove(c.getString(0));
		} while(c.moveToNext());
		assertTrue(tablenames.size() == 0);
		c.close();
	}
}