package com.dlgdev.popularmovies.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.db.DBUtils;
import com.dlgdev.popularmovies.data.movies.Movie;
import com.dlgdev.popularmovies.data.movies.MovieProvider;
import com.dlgdev.popularmovies.data.movies.MovieProviderImpl;
import com.dlgdev.popularmovies.data.db.MovieUtils;
import com.dlgdev.popularmovies.data.db.MoviesContract;
import com.dlgdev.popularmovies.ui.Stage1UITests;
import com.dlgdev.popularmovies.ui.movies.MoviePostersActivity;
import com.dlgdev.popularmovies.ui.movies.MoviesAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class Stage2DataPersistanceTests {
	MovieProvider        moviedb;
	Movie                movie;
	ContentResolver      resolver;
	Uri                  uri;
	MoviePostersActivity activity;

	@Rule public ActivityTestRule<MoviePostersActivity> rule =
			new ActivityTestRule<>(MoviePostersActivity.class);

	@Before public void setup() throws InterruptedException {
		activity = rule.getActivity();
		resolver = activity.getContentResolver();
		moviedb = new MovieProviderImpl(resolver);
		uri = MovieUtils.insertMovies(resolver, 1);
		loadMovie();
		moviedb.favMovie(movie);
	}

	@After public void tearDown() {
		DBUtils.clearDatabase(activity);
	}

	@Test public void appPersistsAFavoritedMovieUsingTheMovieId() throws InterruptedException {
		loadMovie();
		//Default is false. This will be a new made up movie every time
		assertThat(movie.favorite, is(true));

	}

	private void loadMovie() {
		Cursor c = resolver.query(uri, null, null, null, null);
		if (c == null || !c.moveToFirst()) {
			fail();
		}
		movie = moviedb.loadMovie(c);
		c.close();
	}


	@Test public void whenFavoritesIsTheSortOrderFavMoviesAreSelected()
			throws InterruptedException {
		Stage1UITests.toggleSortOrderSelector(activity);
		onView(withText(R.string.favorites)).perform(click());
		//Get back to main activity
		Espresso.pressBack();
		Espresso.pressBack();

		onView(withId(R.id.fragment_movie_posters)).check(new ViewAssertion() {
			@Override public void check(View view, NoMatchingViewException noViewFoundException) {
				RecyclerView rv = (RecyclerView) view;
				MoviesAdapter adapter = (MoviesAdapter) rv.getAdapter();
				//Check that every entry available in the adapter is in fact marked as favorite.
				Cursor c = adapter.movies;
				assertThat(c.getCount(), not(0));
				for (int i = 0; i < c.getCount(); i++) {
					if(c.getInt(c.getColumnIndex(MoviesContract.Movies.FAVORITE)) == 0) {
						fail();
					}
				}
			}
		});
	}
}
