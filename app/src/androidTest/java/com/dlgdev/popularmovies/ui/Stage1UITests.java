package com.dlgdev.popularmovies.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.settings.Settings;
import com.dlgdev.popularmovies.ui.movies.MoviePostersActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.dlgdev.popularmovies.ExtraMatchers.withRecyclerOnPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
@Ignore("Tests from stage 1 can fail from reqs of stage 2") public class Stage1UITests {

	@Rule public ActivityTestRule<MoviePostersActivity> rule =
			new ActivityTestRule<>(MoviePostersActivity.class);
	MoviePostersActivity activity;
	String filmName;
	String filmName1;

	@Before public void setUp() throws Exception {
		activity = rule.getActivity();
	}

	@Test public void moviesAreDisplayedWithThumbnails() throws Throwable {
		RecyclerView recycler = (RecyclerView) activity.findViewById(R.id.fragment_movie_posters);
		onView(allOf(withParent(withRecyclerOnPosition(recycler, 0)),
				withId(R.id.poster)))
				.check(matches(isDisplayed()));
	}

	@Test public void uiContainsElementToToggleSortOrder() {
		toggleSortOrderSelector(activity);

		onView(withText(R.string.popular)).check(matches(isDisplayed()));
		onView(withText(R.string.top_rated)).check(matches(isDisplayed()));
		onView(withText(R.string.favorites)).check(matches(isDisplayed()));
	}

	public static void toggleSortOrderSelector(Context context) {
		Espresso.openActionBarOverflowOrOptionsMenu(context);
		onView(allOf(withId(R.id.title), withText("Settings"), isDisplayed())).perform(click());

		onView(allOf(withClassName(is("android.widget.LinearLayout")),
						withParent(allOf(withId(android.R.id.list),
								withParent(withClassName(is("android.widget.LinearLayout"))))),
						isDisplayed())).perform(click());

		onView(allOf(withClassName(is("android.widget.LinearLayout")),
						withParent(withId(android.R.id.list)),
						isDisplayed())).perform(click());
	}

	@Test public void uiContainsScreenForDetails() {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, click()));
		onView(withId(R.id.release_date)).check(matches(isDisplayed()));
	}

	@Test public void titleReleaseDatePosterVoteAverageAndSynopsisAreDisplayedInDetailsActivity() {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, click()));

		onView(withId(R.id.title)).check(matches(isDisplayed()));
		onView(withId(R.id.release_date)).check(matches(isDisplayed()));
		onView(withId(R.id.poster)).check(matches(isDisplayed()));
		onView(withId(R.id.vote_average)).check(matches(isDisplayed()));
		onView(withId(R.id.plot_synopsis)).check(matches(isDisplayed()));
	}

	@Test public void uiGetsUpdatedAfterChangingSearchCriteria() {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, new ViewAction() {
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
		toggleSortOrderSelector(activity);
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

	//Basically tests the same. Just added to reflect every requirement with a test.
	@Test public void clickingOnThumbnailOpensDetailsScreen() {
		uiContainsScreenForDetails();
	}

}