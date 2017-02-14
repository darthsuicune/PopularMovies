package com.dlgdev.popularmovies.data.reviews;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Reviews {
	int id;
	int page;
	int totalPages;
	int totalResults;
	List<Review> results = new ArrayList<>();

	public static List<Review> fromCursor(Cursor data) {
		List<Review> reviews = new ArrayList<>(data.getCount());
		do {
			reviews.add(new Review(data));
		} while (data.moveToNext());
		return reviews;
	}

	public List<Review> results() {
		return results;
	}
}
