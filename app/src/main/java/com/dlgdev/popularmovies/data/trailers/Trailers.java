package com.dlgdev.popularmovies.data.trailers;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Trailers {
	int id;

	List<Trailer> results = new ArrayList<>();

	public static List<Trailer> fromCursor(Cursor data) {
		List<Trailer> trailers = new ArrayList<>(data.getCount());
		do {
			trailers.add(new Trailer(data));
		} while (data.moveToNext());
		return trailers;
	}

	public List<Trailer> results() {
		return results;
	}
}
