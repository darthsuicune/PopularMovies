package com.dlgdev.popularmovies.data.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dlgdev.popularmovies.ui.movies.MoviePostersActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.dlgdev.popularmovies.data.db.MovieUtils.insertMovies;
import static com.dlgdev.popularmovies.data.db.MovieUtils.makeUpAMovie;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class) public class MovieProviderTest {

	@Rule public ActivityTestRule<MoviePostersActivity> rule =
			new ActivityTestRule<>(MoviePostersActivity.class);
	ContentResolver resolver;
	Activity activity;

	@Before public void setUp() throws Exception {
		activity = rule.getActivity();
		resolver = activity.getContentResolver();
	}

	@After public void tearDown() throws Exception {
		DBUtils.clearDatabase(activity);
	}

	@Test public void testQueryWithGroup() throws InterruptedException {
		insertMovies(resolver, 3);
		Uri uri = MoviesContract.Movies.CONTENT_URI;
		Cursor c = resolver.query(uri, null, null, null, null);
		if(c != null) {
			assertThat(c.getCount(), is(3));
			c.close();
		} else {
			fail();
		}
	}

	@Test public void testQueryForIndividualMovie() throws InterruptedException {
		insertMovies(resolver, 3);
		Uri uri = MoviesContract.buildMovieUri(1);
		Cursor c = resolver.query(uri, null, null, null, null);
		if(c != null) {
			assertThat(c.getCount(), is(1));
			c.close();
		}
	}

	@Test public void testInsert() throws InterruptedException {
		Uri uri = MoviesContract.Movies.CONTENT_URI;
		Uri result = resolver.insert(uri, makeUpAMovie());
		assertThat(result, is(notNullValue()));
	}

	@Test public void testUpdate() throws InterruptedException {
		insertMovies(resolver, 3);
		Uri uri = MoviesContract.Movies.CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(MoviesContract.Movies.TITLE, "Lolnope");
		String selection = MoviesContract.Movies._ID + ">?";
		String[] selectionArgs = { "1" };
		int count = resolver.update(uri, values, selection, selectionArgs);
		assertThat(count, anyOf(is(2), greaterThan(2)));
	}

	@Test public void testDelete() throws InterruptedException {
		insertMovies(resolver, 2);
		Uri uri = MoviesContract.Movies.CONTENT_URI;
		String selection = BaseColumns._ID + "=?";
		String[] selectionArgs = {"1"};
		int count = resolver.delete(uri, selection, selectionArgs);
		assertThat(count, is(1));
	}
}