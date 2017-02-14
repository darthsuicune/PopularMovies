package com.dlgdev.popularmovies.data.movies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieList implements Iterable<Movie>{
	int page;
	int total_results;
	int total_pages;
	List<Movie> results = new ArrayList<>();

	@Override public Iterator<Movie> iterator() {
		return new Iterator<Movie>() {
			int index = 0;

			@Override public boolean hasNext() {
				return index < results.size();
			}

			@Override public Movie next() {
				return results.get(index++);
			}
		};
	}
}
