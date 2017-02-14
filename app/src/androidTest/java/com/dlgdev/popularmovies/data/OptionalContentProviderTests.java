package com.dlgdev.popularmovies.data;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;

import com.dlgdev.popularmovies.data.db.DBUtils;
import com.dlgdev.popularmovies.data.db.MovieUtils;
import com.dlgdev.popularmovies.data.db.MoviesContract;
import com.dlgdev.popularmovies.data.movies.Movie;
import com.dlgdev.popularmovies.data.movies.MovieProvider;
import com.dlgdev.popularmovies.data.movies.MovieProviderImpl;
import com.dlgdev.popularmovies.ui.movies.MoviePostersActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class OptionalContentProviderTests {
	ContentResolver resolver;
	Uri uri;
	MovieProvider db;
	Activity activity;

	@Rule public ActivityTestRule<MoviePostersActivity> rule =
			new ActivityTestRule<>(MoviePostersActivity.class);

	@Before public void setUp() throws Exception {
		activity = rule.getActivity();
		resolver = activity.getContentResolver();
		uri = MovieUtils.insertMovies(resolver, 1);
		db = new MovieProviderImpl(resolver);
	}

	@After public void tearDown() throws Exception {
		DBUtils.clearDatabase(activity);
	}

	@Test public void favoritesArePersistedInDatabase() {
		Cursor c = null;
		try {
			c = query();
			Movie m = db.loadMovie(c);
			db.favMovie(m);
			c.close();
			c = query();

			assertThat(c.getCount(), is(1));
			assertThat(c.getInt(c.getColumnIndex(MoviesContract.Movies.FAVORITE)), is(1));
		} finally {
			if (c != null) {
				c.close();
			}
		}
	}

	Cursor query() {
		Cursor c = resolver.query(uri, null, null, null, null);
		if (c == null || !c.moveToFirst()) {
			fail("Couldn't load data from db");
		}
		return c;
	}

	@Test public void appDisplaysFavoriteDetailsWhenOffline() {
		//All the data is stored locally, not juts favorites, so all can be loaded offline
		//except for the poster, which isn't saved locally. I'm not willing to
		//make the application handle wifi/connectivity states needed *just* for testing.
		//Can be easily tested manually (turn on airplane mode, open list).
	}

	@Ignore @Test public void appUsesContentProviderToPopulateFavoriteDetails() {
		//I honestly don't know how to automatically test that this is happening
	}
}
