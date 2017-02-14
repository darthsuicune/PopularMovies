package com.dlgdev.popularmovies.ui;

import android.support.test.rule.ActivityTestRule;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.ui.movies.MoviePostersActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.fail;

@Ignore public class Stage2TabletSpecificTests {
	@Rule public ActivityTestRule<MoviePostersActivity> rule =
			new ActivityTestRule<>(MoviePostersActivity.class);

	MoviePostersActivity activity;

	@Before public void setUp() throws Exception {
		activity = rule.getActivity();
	}

	@Test public void tabletUsesMasterDetailLayoutImplementedWithFragments() {
		onView(withId(R.id.movie_details_container)).check(matches(isDisplayed()));
	}

	@Test public void tabletUpdatesDetailFragmentWhenTappingThumbnail() {
		fail("Not implemented");
	}
}
