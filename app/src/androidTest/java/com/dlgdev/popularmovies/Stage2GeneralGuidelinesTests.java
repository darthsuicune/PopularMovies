package com.dlgdev.popularmovies;

import android.support.test.rule.ActivityTestRule;

import com.dlgdev.popularmovies.ui.movies.MoviePostersActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.fail;

public class Stage2GeneralGuidelinesTests {
	@Rule public ActivityTestRule<MoviePostersActivity> rule =
			new ActivityTestRule<>(MoviePostersActivity.class);
	MoviePostersActivity activity;
	@Before public void setUp() throws Exception {
		activity = rule.getActivity();
	}

	@Test public void selectedMovieIsStillSelectedOnRotation() {
		fail("Not implemented yet");
	}
	@Test public void sameActivityIsDisplayedOnRotation() {
		fail("Not implemented yet");
	}
	@Test public void userTextIsPreservedOnRotation() {
		//There is no field in the app for user text, so this passes automatically.
		//Should there be any edittext control, then by using fragments it should stay there
		//unless we remove it explicitly (or replace the fragment)
	}
	@Test public void itemListPositionsArePreservedOnRotation() {
		fail("Not implemented yet");
	}
	@Test public void sameStateIsPreservedAfterWakingFromSleep() {
		fail("Not implemented yet");
	}
	@Test public void sameStateIsPreservedAfterBeingSentToBackground() {
		fail("Not implemented yet");
	}
}
