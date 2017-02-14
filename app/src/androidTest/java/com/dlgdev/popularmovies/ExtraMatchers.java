package com.dlgdev.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ExtraMatchers {

	public static Matcher<View> withRecyclerOnPosition(final RecyclerView recycler,
														final int position) {
		return new TypeSafeMatcher<View>() {
			@Override protected boolean matchesSafely(View item) {
				View v = recycler.getChildAt(position);

				return item.getId() == v.getId() &&
					   recycler.getChildAdapterPosition(item) == position;
			}

			@Override public void describeTo(Description description) {
				description.appendText("has a child at position " + position);
			}
		};
	}

	public static <T> Matcher<T> first(final Matcher<T> matcher) {
		return new BaseMatcher<T>() {
			boolean isFirst = true;

			@Override public boolean matches(final Object item) {
				if (isFirst && matcher.matches(item)) {
					isFirst = false;
					return true;
				}

				return false;
			}

			@Override public void describeTo(final Description description) {
				description.appendText("should return first matching item");
			}
		};
	}
}
