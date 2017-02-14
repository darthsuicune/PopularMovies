package com.dlgdev.popularmovies.ui;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.view.View;
import android.widget.TextView;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.db.DBUtils;
import com.dlgdev.popularmovies.data.db.MovieUtils;
import com.dlgdev.popularmovies.settings.Settings;
import com.dlgdev.popularmovies.ui.details.MovieDetailsActivity;
import com.dlgdev.popularmovies.ui.movies.MoviePostersActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.dlgdev.popularmovies.ui.details.MovieDetailsFragment.KEY_MOVIE;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;

public class Stage2UIFunctionPostersTests {
	@Rule public IntentsTestRule<MoviePostersActivity> rule =
			new IntentsTestRule<>(MoviePostersActivity.class);

	private MoviePostersActivity activity;
	private String               filmName;
	private String               filmName1;

	@Before public void setUp() throws Exception {
		activity = rule.getActivity();
		ContentResolver resolver = activity.getContentResolver();
		MovieUtils.insertMovies(resolver, 2);
	}

	@After public void tearDown() throws Exception {
		DBUtils.clearDatabase(activity);
	}

	@Test public void uiIsUpdatedAfterChangingWhichFilmsToShow() throws InterruptedException {
		onView(withId(R.id.fragment_movie_posters))
				.perform(actionOnItemAtPosition(0, new ViewAction() {
			@Override public Matcher<View> getConstraints() {
				return null;
			}

			@Override public String getDescription() {
				return null;
			}

			@Override public void perform(UiController uiController, View view) {
				filmName = ((TextView) view.findViewById(R.id.title)).getText().toString();
			}
		}));
		Stage2UILayoutTests.toggleSortOrderSelector(activity);
		changeSortOrder();

		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, new ViewAction() {
			@Override public Matcher<View> getConstraints() {
				return null;
			}

			@Override public String getDescription() {
				return null;
			}

			@Override public void perform(UiController uiController, View view) {
				filmName1 = ((TextView) view.findViewById(R.id.title)).getText().toString();
			}
		}));
		assertNotEquals(filmName, filmName1);
	}

	private void changeSortOrder() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		String movieType = prefs.getString(Settings.MOVIES_TYPE, Settings.POPULAR);
		onView(withText((movieType.equals("popular")) ? R.string.top_rated : R.string.popular))
				.perform(click());
		Espresso.pressBack();
		Espresso.pressBack();
	}

	@Test public void phoneLaunchesDetailActivityWhenTappingThumbnail()
			throws InterruptedException {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, click()));
		intended(allOf(hasExtra(KEY_MOVIE, anyLong()),
		               hasComponent(hasClassName(MovieDetailsActivity.class.getName()))));

	}
}
